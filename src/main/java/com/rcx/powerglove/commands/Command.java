package com.rcx.powerglove.commands;

import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

	default public void execute(String[] arguments, MessageReceivedEvent event) {}

	default public void execute(String[] arguments, MessageReceivedEvent event, Setting settings) {
		execute(arguments, event);
	}
}
