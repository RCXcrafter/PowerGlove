package com.rcx.powerglove.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommand {

	public void execute(SlashCommandInteractionEvent event);
}
