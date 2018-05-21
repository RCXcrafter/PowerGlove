package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.code.chatterbotapi.ChatterBotSession;
import com.rcx.powerglove.commands.Settings;
import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TalkListener extends ListenerAdapter {

	public static Map<String, ChatterBotSession> chats = new HashMap<String, ChatterBotSession>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Setting settings = Settings.settings.getOrDefault(event.getGuild().getId(), Settings.settings.get("default"));
		if ((event.getAuthor().isBot() && !settings.talktobots) || event.getAuthor().getId().equals("439435998078959616"))
			return;
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		//System.out.println(content);

		if (chats.containsKey(event.getGuild().getId()  + " " + channel.getId())) {
			if (content.startsWith(PowerGlove.prefix) && content.length() > PowerGlove.prefix.length() || content.startsWith(settings.prefix) && content.length() > settings.prefix.length())
				return;
			try {
				event.getChannel().sendTyping().queue();
				channel.sendMessage(chats.get(event.getGuild().getId()  + " " + channel.getId()).think(content).replace("<br> ", "\n")).queue();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (content.toLowerCase().equals("delete this message")) {
			event.getChannel().sendTyping().queue();
			//channel.sendMessage("Alright, if that's what you want").queue();
			//Long myMessage = channel.getLatestMessageIdLong();
			channel.deleteMessageById(message.getId()).queueAfter(10, TimeUnit.SECONDS);
			//channel.deleteMessageById(myMessage).queue();
		}

		if (content.toLowerCase().endsWith("des")) {
			event.getChannel().sendTyping().queue();
			channel.sendMessage("pa").queue();
			event.getChannel().sendTyping().queue();
			channel.sendMessage("cito").queueAfter(1, TimeUnit.SECONDS);
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(channel.getMessageById(channel.getLatestMessageId()).complete().getContentRaw());
			if (!channel.getMessageById(channel.getLatestMessageId()).complete().getContentRaw().contains("cito"))*/
		}

		if (content.toLowerCase().contains("<@439435998078959616>")) {
			if (content.toLowerCase().contains("help")) {
				event.getChannel().sendTyping().queue();
				channel.sendMessage("Use: pow help\nfor a list of commands and other help.").queue();
			} else if (content.toLowerCase().contains("hello")) {
				event.getChannel().sendTyping().queue();
				channel.sendMessage("Yes hello, this is Power Glove.").queue();
			} else if (message.getGuild() != null){
				message.addReaction(new EmoteImpl(445609116337963008l, (JDAImpl) event.getJDA())).queue();
			}
		}

		if (content.toLowerCase().startsWith("poll:")) {
			message.addReaction("\uD83DuDC4D").queue();
			message.addReaction("\uD83DuDC4E").queue();
		}

		if (content.toLowerCase().contains("that was easy")) {
			message.addReaction(new EmoteImpl(445609298366824459l, (JDAImpl) event.getJDA())).queue();
		}

		if (content.toLowerCase().contains("power")) {
			message.addReaction(new EmoteImpl(447839588002824192l, (JDAImpl) event.getJDA())).queue();
		}

		if (content.toLowerCase().contains("look") && content.toLowerCase().contains("nothing")) {
			message.addReaction(new EmoteImpl(445609116145287169l, (JDAImpl) event.getJDA())).queue();
		}
	}
}
