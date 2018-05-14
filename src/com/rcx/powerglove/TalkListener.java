package com.rcx.powerglove;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TalkListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() && CommandListener.onlyTalkToPeople || event.getAuthor().getId() == "439435998078959616")
			return;
		// We don't want to respond to bot accounts, or ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		//System.out.println(content);

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
				channel.sendMessage("Yes hello, this is Power GLove.").queue();
			} else if (message.getGuild() != null){
				message.addReaction(new EmoteImpl(445609116337963008l, (GuildImpl) event.getGuild())).queue();
			}
		}

		if (content.toLowerCase().startsWith("poll:")) {
			message.addReaction("👍").queue();
			message.addReaction("👎").queue();
		}
		
		if (content.toLowerCase().contains("that was easy")) {
			message.addReaction(new EmoteImpl(445609298366824459l, (GuildImpl) event.getGuild())).queue();
		}
	}
}
