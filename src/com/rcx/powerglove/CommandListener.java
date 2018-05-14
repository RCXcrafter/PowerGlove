package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import com.rcx.powerglove.commands.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	static Map<String, Command> commands = new HashMap<String, Command>();
	public static String prefix = "pow";
	static Boolean onlyTalkToPeople = true;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() && onlyTalkToPeople || event.getAuthor().getId() == "439435998078959616")
			return;
		// We don't want to respond to bot accounts, or ourself
		if (!event.getMessage().getContentRaw().startsWith(prefix))
			return;
		String[] arguments = event.getMessage().getContentRaw().substring(prefix.length() + 1).split(" ");
		if (!commands.containsKey(arguments[0].toLowerCase()))
			return;
		event.getChannel().sendTyping().queue();
		commands.get(arguments[0].toLowerCase()).execute(arguments, event);
	}
}
