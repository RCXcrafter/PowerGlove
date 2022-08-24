package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.alicebot.ab.Chat;

import com.rcx.powerglove.commands.Afk;
import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.emoji.RichCustomEmojiImpl;

public class TalkListener {

	public static Map<String, Chat> chats = new HashMap<String, Chat>();

	public static Random rand = new Random();

	public static void onMessageReceived(MessageReceivedEvent event, Setting settings) {
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();

		for (User mention : message.getMentions().getUsers()) {
			String ping = mention.getId();
			if (Afk.afkPeople.containsKey(ping)) {
				String reason = Afk.afkPeople.get(ping);
				String person;
				if (event.getGuild().isMember(mention))
					person = event.getGuild().getMember(mention).getEffectiveName();
				else
					person = mention.getName();
				channel.sendTyping().complete();
				if (reason.equals("afk"))
					channel.sendMessage(person + " is currently AFK.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				else
					channel.sendMessage(person + " is currently AFK: " + reason).complete().delete().queueAfter(10, TimeUnit.SECONDS);
			}
		}

		if (Afk.afkPeople.containsKey(message.getAuthor().getId()) && !content.startsWith(PowerGlove.prefix + "afk") && !content.startsWith(settings.prefix + "afk")) {
			Afk.afkPeople.remove(message.getAuthor().getId());
			channel.sendTyping().complete();
			channel.sendMessage("You are no longer AFK, " + message.getAuthor().getName()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

		if (chats.containsKey(event.getGuild().getId() + " " + channel.getId())) {
			try {
				channel.sendTyping().complete();
				String response = chats.get(event.getGuild().getId() + " " + channel.getId()).multisentenceRespond(content);
				if (response.equals(""))
					response = "I have nothing to say to that.";
				response = response.replaceAll("<br/>", "\n").replaceAll("ust surf somewhere else", "eez fine").replaceAll("@everyone", "everyone");
				String nickname = event.getGuild().getMemberById("439435998078959616").getNickname();
				if (nickname != null)
					response = response.replaceAll("Power Glove", nickname);
				if (response.length() > 2000) {
					response = "I don't say this often, but I'm done talking for now.";
					chats.remove(event.getGuild().getId() + " " + channel.getId());
				}
				channel.sendMessage(response).queueAfter(1, TimeUnit.SECONDS);
				String said = response.toLowerCase();
				if (said.contains("stop talking now") || said.contains("bye") & !said.contains("hello") || said.contains("adios") || said.contains("eez fine") || said.contains("ee you later") || said.contains("ttyl") || content.toLowerCase().contains("shut up"))
					chats.remove(event.getGuild().getId() + " " + channel.getId());
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (content.toLowerCase().contains("<@439435998078959616>")) {
			if (content.toLowerCase().equals("<@439435998078959616>") || content.toLowerCase().contains("help") || content.toLowerCase().contains("how") || content.toLowerCase().contains("what") || content.contains("?")) {
				channel.sendTyping().complete();
				channel.sendMessage("Use: pow help\nfor a list of commands and other help.").queue();
			} else if (content.toLowerCase().contains("hello")) {
				channel.sendTyping().complete();
				channel.sendMessage("Yes hello, this is Power Glove.").queue();
			} else if (content.toLowerCase().contains("pet")) {
				channel.sendTyping().complete();
				channel.sendMessage("I'm not your pet.").queue();
			} else if (content.toLowerCase().contains("pat")) {
				channel.sendTyping().complete();
				channel.sendMessage("That better be a pat on the shoulder or the back, I do not allow pats on my head.").queue();
			} else if (message.getGuild() != null){
				message.addReaction(new RichCustomEmojiImpl(445609116337963008l, (GuildImpl) event.getJDA().getGuildById(445601562186874891l))).queue();
			}
		}

		if (settings.eastereggs) {
			if (content.toLowerCase().equals("delete this message")) {
				channel.sendTyping().complete();
				try {
					message.delete().queueAfter(10, TimeUnit.SECONDS);
					channel.sendMessage("Alright, if that's what you want.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
					channel.sendTyping().complete();
				} catch (InsufficientPermissionException e) {
					channel.sendMessage("I'm sorry, I just can't.").queue();
				}
			}

			if (content.toLowerCase().contains("that was easy")) {
				message.addReaction(new RichCustomEmojiImpl(445609298366824459l, (GuildImpl) PowerGlove.servers.get("423797628040511490"))).queue();
			}

			if (content.toLowerCase().contains("power")) {
				message.addReaction(new RichCustomEmojiImpl(506113366780280852l, (GuildImpl) PowerGlove.servers.get("423797628040511490"))).queue();
			}

			if (content.toLowerCase().contains("look") && content.toLowerCase().contains("nothing")) {
				message.addReaction(new RichCustomEmojiImpl(445609116145287169l, (GuildImpl) PowerGlove.servers.get("423797628040511490"))).queue();
			}
		}
	}
}
