package com.rcx.powerglove.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class CommandSimpleEmbed implements Command {

	MessageEmbed returns;

	public CommandSimpleEmbed(MessageEmbed returns) {
		this.returns = returns;
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessageEmbeds(returns).queue();
		} catch (InsufficientPermissionException e) {
			event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions.\n" + returns.getDescription()).queue();
		}
	}
}
