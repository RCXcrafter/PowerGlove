package com.rcx.powerglove.commands;

import com.rcx.powerglove.PowerGlove;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Exit implements Command {

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals("96664175350149120"))
			return;

		event.getChannel().sendMessage("Shutting down.").queue();
		System.out.println("shutting down");
		PowerGlove.api.shutdown();
		for (JDA shard : PowerGlove.api.getShards()) {
			while (!shard.getStatus().equals(Status.SHUTDOWN)) {}
		}
		System.exit(0);
	}
}
