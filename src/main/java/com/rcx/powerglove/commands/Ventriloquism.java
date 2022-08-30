package com.rcx.powerglove.commands;

import java.util.concurrent.TimeUnit;

import com.rcx.powerglove.PowerGlove;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ventriloquism implements Command {

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (event.getAuthor().getId().equals("96664175350149120")) {
			TextChannel channel = PowerGlove.api.getGuildById(arguments[1]).getTextChannelById(arguments[2]);
			channel.sendTyping().complete();
			String textString = arguments[3];
			for (int i = 4; i < arguments.length; i++)
				textString += " " + arguments[i];
			channel.sendMessage(textString).queueAfter(3, TimeUnit.SECONDS);
		}
	}
}
