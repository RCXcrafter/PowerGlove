package com.rcx.powerglove.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSimpleString extends Command {

	String returns;

	public CommandSimpleString(String returns) {
		this.returns = returns;
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage(returns).queue();
	}
}
