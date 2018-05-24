package com.rcx.powerglove.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Exit extends Command {

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals("96664175350149120"))
			return;

		event.getChannel().sendMessage("Shutting down.").queue();
		System.out.println("shutting down");
		System.exit(0);
	}
}
