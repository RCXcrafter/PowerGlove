package com.rcx.powerglove.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.FileUpload;

public class GifBPM implements SlashCommand {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		String url = null;
		double bpm = event.getOption("bpm").getAsDouble();
		double beats = event.getOption("beats").getAsInt();

		OptionMapping option = event.getOption("gif");
		if (option.getType() == OptionType.ATTACHMENT && option.getAsAttachment().getUrl().endsWith(".gif")) {
			url = option.getAsAttachment().getUrl();
		} else if (option.getType() == OptionType.STRING) {
			String rawString = option.getAsString();

			Matcher matcher = Message.MentionType.EMOJI.getPattern().matcher(rawString);
			if (matcher.matches()) {
				if (!rawString.startsWith("<a")) {
					event.reply("\u26A0 That emote isn't animated").setEphemeral(true).queue();
					return;
				}
				url = Emoji.fromCustom(matcher.group(1), Long.parseUnsignedLong(matcher.group(2)), rawString.startsWith("<a")).getImageUrl();
			} else {
				if (!rawString.endsWith(".gif")) {
					url = rawString + ".gif";
				} else {
					url = rawString;
				}
			}
		}/* else if (attachment.getType() == OptionType.MENTIONABLE) {
			IMentionable emote = attachment.getAsMentionable();
			if (emote instanceof CustomEmoji) {
				if (((CustomEmoji) emote).isAnimated()) {
					url = ((CustomEmoji) emote).getImageUrl();
				} else {
					event.reply("\u26A0 That emote isn't animated").setEphemeral(true).queue();
					return;
				}
			} else {
				event.reply("\u26A0 You're supposed to put an emote there buddy").setEphemeral(true).queue();
				return;
			}
		} */

		if (url == null)
			event.reply("\u26A0 No gif url found").setEphemeral(true).queue();

		try {
			File gif = null;
			gif = File.createTempFile("gif", url.substring(url.lastIndexOf(".")));
			HttpURLConnection site = (HttpURLConnection) new URL(url).openConnection();

			site.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			site.setDoInput(true);
			site.connect();
			InputStream in = new BufferedInputStream(site.getInputStream());
			if (!URLConnection.guessContentTypeFromStream(in).equals("image/gif")) {
				event.reply("\u26A0 Unable to find gif from <" + url + ">").setEphemeral(true).queue();
				return;
			}

			ImageInputStream input = ImageIO.createImageInputStream(in);
			ImageOutputStream output = ImageIO.createImageOutputStream(gif);

			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = readers.next();

			reader.setInput(input);
			ImageWriter writer = ImageIO.getImageWriter(reader);
			writer.setOutput(output);
			writer.prepareWriteSequence(reader.getStreamMetadata());
			ImageWriteParam params = writer.getDefaultWriteParam();

			IIOMetadata newMeta = writer.getDefaultImageMetadata(reader.getRawImageType(reader.getMinIndex()), params);//reader.getStreamMetadata().co;
			writer.prepareWriteSequence(newMeta);

			int index = reader.getMinIndex();
			List<IIOImage> frames = new ArrayList<IIOImage>();
			try {
				while (true) {
					IIOImage image = reader.readAll(index, reader.getDefaultReadParam());
					frames.add(image);
					index++;
				}
			} catch (Exception e) {
				//this just means it's done, nothing bad happened
				//e.printStackTrace();
			}

			double framecount = frames.size();
			double delay = (100.0 / (bpm / 60.0)) / (framecount / beats);

			System.out.println("frames: " + framecount);
			System.out.println("delay ended up being: " + delay);
			System.out.println("leftover frametime: " + delay % 1);
			System.out.println("leftover total: " + (delay % 1) * framecount);
			double leftovers = Math.round((delay % 1) * framecount);
			System.out.println("leftover final: " + ((delay % 1) * framecount - leftovers));



			double counter = 0;
			for (int i = 0; i < framecount; i++) {
				int extra = 0;
				if (counter > 1) {
					extra++;
					counter--;
				}
				counter += leftovers / framecount;

				IIOImage frame = frames.get(i);
				IIOMetadata meta = writer.getDefaultImageMetadata(reader.getRawImageType(reader.getMinIndex()), params);
				String metaFormatName = meta.getNativeMetadataFormatName();
				meta.mergeTree(metaFormatName, frame.getMetadata().getAsTree(metaFormatName));
				IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(metaFormatName);

				IIOMetadataNode graphicsControlExtensionNodes = getNode(root, "GraphicControlExtension");
				graphicsControlExtensionNodes.setAttribute("delayTime", Integer.toString((int) delay + extra));
				meta.mergeTree(metaFormatName, root);

				frame.setMetadata(meta);
				writer.writeToSequence(frame, params);
			}
			writer.endWriteSequence();

			event.replyFiles(FileUpload.fromData(gif)).complete();
			input.close();
			in.close();
			output.close();
			site.disconnect();
			gif.delete();
			reader.dispose();
			writer.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			event.reply("\u26A0 Unable to download gif from <" + url + ">").setEphemeral(true).queue();
		} catch (InsufficientPermissionException e) {
			event.reply("\u26A0 This command normally results in an image, but I lack the permission to *Attach Files*").setEphemeral(true).queue();
		}
	}

	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++){
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return (IIOMetadataNode) rootNode.item(i);
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return(node);
	}
}
