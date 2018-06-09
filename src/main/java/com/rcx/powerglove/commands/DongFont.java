package com.rcx.powerglove.commands;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DongFont extends Command {

	BufferedImage font;

	public DongFont() {
		try {
			font = ImageIO.read(getClass().getClassLoader().getResource("assets/dongfont/font.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (arguments.length < 2) {
			event.getChannel().sendMessage("\u26A0 Insert text to convert.").queue();
			return;
		}

		File picture = null;
		try {
			picture = File.createTempFile("expand", ".png");

			String message = event.getMessage().getContentRaw();
			message = message.substring(message.indexOf("dong") + 5);

			List<List<BufferedImage>> texts = convert(message.toLowerCase());
			int width = 0;
			for (List<BufferedImage> text : texts){
				width = Math.max(text.size(), width);
			}
			BufferedImage combined = new BufferedImage(width * 63, texts.size() * 72, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			int x = 0;
			int y = 0;
			for (List<BufferedImage> text : texts){
				for (BufferedImage img : text) {
					g.drawImage(img, x, y, null);
					x += img.getWidth();
				}
				y += 72;
				x = 0;
			}
			ImageIO.write(combined, "PNG", picture);
			event.getChannel().sendFile(picture).queue();
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		picture.delete();
	}

	public List<List<BufferedImage>> convert(String text) {

		List<List<BufferedImage>> imagess = new ArrayList<>();

		for (String line : text.split("\n")) {
			List<BufferedImage> images = new ArrayList<>();
			for (char c : line.toCharArray()) {
				c = Character.toUpperCase(c);
				int smudge = 9;
				int offset = -1;
				int width = 72;
				if (c >= 48 && c <= 57) {
					offset = c - 48;
				} else if (c >= 65 && c <= 90) {
					offset = c - 65 + 10;
					if (c == 73)
						smudge *= 5;
				} else if (c == 32) {
					offset = 48;
					smudge *= 2;
				}

				if (offset >= 0) {
					BufferedImage sub = font.getSubimage((offset * width) + smudge, 0, width - smudge, width);
					images.add(sub);
				}
			}
			imagess.add(images);
		}
		return imagess;
	}
}
