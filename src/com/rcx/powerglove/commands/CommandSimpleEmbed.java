package com.rcx.powerglove.commands;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSimpleEmbed extends Command {

	MessageEmbed returns;

	public CommandSimpleEmbed(MessageEmbed returns) {
		this.returns = returns;
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage(returns).queue();
	}
}
