package com.rcx.powerglove.commands;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotType;
import com.rcx.powerglove.TalkListener;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Talk extends Command {

	ChatterBot bot;
	ChatterBotFactory factory = new ChatterBotFactory();

	public Talk(){
		try {
			bot = factory.create(ChatterBotType.PANDORABOTS, "c9386f59ce345d8b");
			//"f5d922d97e345aa1" A.L.I.C.E.
			//"c34376751e34cf4d" Guigui The A'Bot
			//"aa0613bd7e35f089" LEMONBOT
			//"da4d216c0e345aa1" test
			//"d6dd41a29e3649d6" lucifer
			//"c9386f59ce345d8b" power glove
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		String id = event.getGuild().getId()  + " " + event.getChannel().getId();
		if (TalkListener.chats.containsKey(id)) {
			event.getChannel().sendMessage("Alright, I'll stop talking.").queue();
			TalkListener.chats.remove(id);
		} else {
			event.getChannel().sendMessage("Alright, let's talk.").queue();
			TalkListener.chats.put(id, bot.createSession());
		}
	}
}
