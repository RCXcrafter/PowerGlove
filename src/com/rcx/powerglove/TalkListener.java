package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.code.chatterbotapi.ChatterBotSession;
import com.rcx.powerglove.commands.Afk;
import com.rcx.powerglove.commands.Settings;
import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
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

		String mentions = content;
		while (mentions.contains("<@") && mentions.contains(">")) {
			if (mentions.indexOf("<@") < mentions.indexOf(">")) {
				String ping = mentions.substring(mentions.indexOf("<@") + 2, mentions.indexOf(">"));
				ping = ping.replace("!", "");
				if (Afk.afkPeople.containsKey(ping)) {
					String reason = Afk.afkPeople.get(ping);
					String person = event.getGuild().getMemberById(ping).getEffectiveName();
					if (reason.equals("afk"))
						channel.sendMessage(person + " is currently AFK.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
					else
						channel.sendMessage(person + " is currently AFK: " + reason).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}
			}
			mentions = mentions.substring(mentions.indexOf(">") + 1);
		}

		if (Afk.afkPeople.containsKey(message.getAuthor().getId()) && !content.startsWith(PowerGlove.prefix + "afk") && !content.startsWith(settings.prefix + "afk")) {
			Afk.afkPeople.remove(message.getAuthor().getId());
			channel.sendMessage("You are no longer AFK, " + message.getAuthor().getName()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

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
			try {
				message.delete().queueAfter(10, TimeUnit.SECONDS);
				channel.sendMessage("Alright, if that's what you want.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				event.getChannel().sendTyping().queue();
			} catch (InsufficientPermissionException e) {
				channel.sendMessage("I'm sorry, I just can't.").queue();
			}
		}

		if (content.toLowerCase().startsWith("ninja:")) {
			message.delete().queue();
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
				message.addReaction(new EmoteImpl(445609116337963008l, (GuildImpl) event.getJDA().getGuildById(445601562186874891l))).queue();
			}
		}

		if (content.toLowerCase().startsWith("poll:")) {
			message.addReaction("\uD83DuDC4D").queue();
			message.addReaction("\uD83DuDC4E").queue();
		}

		if (content.toLowerCase().contains("that was easy")) {
			message.addReaction(new EmoteImpl(445609298366824459l, (GuildImpl) event.getJDA().getGuildById(445601562186874891l))).queue();
		}

		if (content.toLowerCase().contains("power")) {
			message.addReaction(new EmoteImpl(447839588002824192l, (GuildImpl) event.getJDA().getGuildById(445601562186874891l))).queue();
		}

		if (content.toLowerCase().contains("look") && content.toLowerCase().contains("nothing")) {
			message.addReaction(new EmoteImpl(445609116145287169l, (GuildImpl) event.getJDA().getGuildById(445601562186874891l))).queue();
		}
	}
}
