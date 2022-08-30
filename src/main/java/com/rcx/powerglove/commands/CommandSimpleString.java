package com.rcx.powerglove.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSimpleString implements Command {

	String returns;

	public CommandSimpleString(String returns) {
		this.returns = returns;
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage(returns).queue();
	}
}
