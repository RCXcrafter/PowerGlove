package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import com.rcx.powerglove.commands.Command;
import com.rcx.powerglove.commands.Settings;
import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	static Map<String, Command> commands = new HashMap<String, Command>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Setting settings = Settings.settings.getOrDefault(event.getGuild().getId(), Settings.settings.get("default"));
		if ((event.getAuthor().isBot() && !settings.talktobots) || event.getAuthor().getId().equals("439435998078959616"))
			return;
		String message = event.getMessage().getContentRaw();
		String[] arguments = {"bluh"};

		if (message.startsWith(PowerGlove.prefix) && message.length() > PowerGlove.prefix.length()) {
			arguments = message.substring(PowerGlove.prefix.length()).split(" ");
		} else if (message.startsWith(settings.prefix) && message.length() > settings.prefix.length()) {
			if (!message.contains(" "))
				arguments[0] = message.substring(settings.prefix.length());
			else
				arguments = message.substring(settings.prefix.length()).split(" ");
		} else
			return;
		if (!commands.containsKey(arguments[0].toLowerCase()))
			return;
		event.getChannel().sendTyping().complete();
		commands.get(arguments[0].toLowerCase()).execute(arguments, event);
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		PowerGlove.servers.get("423797628040511490").getTextChannelById(448854472223883264l).sendMessage("Added to server:" + event.getGuild().getName() + event.getGuild().getIconUrl() + event.getGuild().getId()).queue();
		PowerGlove.servers.put(event.getGuild().getId(), event.getGuild());
		if (!PowerGlove.dbl.equals(null))
			PowerGlove.dbl.setStats("439435998078959616", PowerGlove.servers.size());
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		PowerGlove.servers.get("423797628040511490").getTextChannelById(448854472223883264l).sendMessage("Removed from server:" + event.getGuild().getName() + event.getGuild().getIconUrl() + event.getGuild().getId()).queue();
		PowerGlove.servers.remove(event.getGuild().getId());
		if (!PowerGlove.dbl.equals(null))
			PowerGlove.dbl.setStats("439435998078959616", PowerGlove.servers.size());
	}
}
