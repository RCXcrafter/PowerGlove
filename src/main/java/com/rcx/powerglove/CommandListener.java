package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import com.rcx.powerglove.commands.Command;
import com.rcx.powerglove.commands.Settings;
import com.rcx.powerglove.commands.Settings.Setting;
import com.rcx.powerglove.commands.SlashCommand;
import com.rcx.powerglove.commands.Ventriloquism;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	static Map<String, Command> commands = new HashMap<String, Command>();
	static Map<String, SlashCommand> slashCommands = new HashMap<String, SlashCommand>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Setting settings = Settings.settings.getOrDefault(event.getGuild().getId(), Settings.settings.get("default"));
		if (event.isWebhookMessage() || event.getAuthor().isSystem() || (event.getAuthor().isBot() && !settings.talktobots) || event.getAuthor().getId().equals(PowerGlove.id))
			return;
		String message = event.getMessage().getContentRaw();
		String[] arguments = {"bluh"};

		if (message.toLowerCase().startsWith(PowerGlove.prefix) && message.length() > PowerGlove.prefix.length()) {
			arguments = message.substring(PowerGlove.prefix.length()).split(" ");
		} else if (message.toLowerCase().startsWith(settings.prefix) && message.length() > settings.prefix.length()) {
			if (message.contains(" "))
				arguments = message.substring(settings.prefix.length()).split(" ");
			else
				arguments[0] = message.substring(settings.prefix.length());
		} else {
			TalkListener.onMessageReceived(event, settings);
			return;
		}
		if (!commands.containsKey(arguments[0].toLowerCase())) {
			TalkListener.onMessageReceived(event, settings);
			return;
		}
		Command command = commands.get(arguments[0].toLowerCase());
		if (!(command instanceof Ventriloquism))
			event.getChannel().sendTyping().complete();
		command.execute(arguments, event, settings);
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (slashCommands.containsKey(event.getName())) {
			slashCommands.get(event.getName()).execute(event);
		}
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		//PowerGlove.servers.get("423797628040511490").getTextChannelById(448854472223883264l).sendMessage("Added to server: " + event.getGuild().getName() + " " + event.getGuild().getIconUrl() + " "  + event.getGuild().getId()).queue();
		PowerGlove.servers.put(event.getGuild().getId(), event.getGuild());
		if (PowerGlove.servers.size() > 50) { //limit amount of guilds to 50
			event.getGuild().leave().queue();
		}
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		//PowerGlove.servers.get("423797628040511490").getTextChannelById(448854472223883264l).sendMessage("Removed from server: " + event.getGuild().getName() + " "  + event.getGuild().getIconUrl() + " "  + event.getGuild().getId()).queue();
		PowerGlove.servers.remove(event.getGuild().getId());
	}
}
