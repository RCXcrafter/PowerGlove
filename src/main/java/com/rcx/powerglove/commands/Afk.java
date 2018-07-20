package com.rcx.powerglove.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class Afk extends Command {

	public static Map<String, String> afkPeople = new HashMap<String, String>();

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		String afkReason;
		if (arguments.length < 2) {
			afkReason = "afk";
			event.getChannel().sendMessage(event.getAuthor().getName() + " is now AFK.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
		} else {
			afkReason = arguments[1];
			for (int i = 2; i < arguments.length; i++)
				afkReason += " " + arguments[i];
			event.getChannel().sendMessage(event.getAuthor().getName() + " is now AFK: " + afkReason).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
		try {
			event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
		} catch (InsufficientPermissionException e) {
		}
		if (afkPeople.containsKey(event.getAuthor().getId()))
			afkPeople.remove(event.getAuthor().getId());
		afkPeople.put(event.getAuthor().getId(), afkReason);
	}
}
