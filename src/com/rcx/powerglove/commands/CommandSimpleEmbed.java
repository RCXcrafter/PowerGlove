package com.rcx.powerglove.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSimpleEmbed extends Command {

	EmbedBuilder returns;

	public CommandSimpleEmbed(EmbedBuilder returns) {
		this.returns = returns;
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage(returns.build()).queue();
	}
}
