package com.rcx.powerglove.commands;

import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	
	public void execute(String[] arguments, MessageReceivedEvent event) {
		
	}

	public void execute(String[] arguments, MessageReceivedEvent event, Setting settings) {
		execute(arguments, event);
	}
}
