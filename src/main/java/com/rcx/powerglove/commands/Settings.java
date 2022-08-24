package com.rcx.powerglove.commands;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rcx.powerglove.PowerGlove;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.internal.utils.PermissionUtil;

public class Settings extends Command {

	public static Map<String, Setting> settings = new HashMap<String, Setting>();
	static JSONObject config;

	MessageEmbed embed = new EmbedBuilder().setColor(0x419399).setAuthor("Power Glove Settings", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
			"Here's a list of all the settings:\n"
					+ "\n\u2022 **prefix [prefix] [space]:** Add an alternate prefix for the bot to use. If you want to have a space between the prefix and the command, add \"space\" as the second argument"
					+ "\n\u2022 **talktobots [true/false]:** If the bot should respond to other bots. default: false"
					+ "\n\u2022 **eastereggs [true/false]:** Enable or disable some dumb easter eggs, these could get annoying over time. default: false"
					+ "\n\u2022 **reset [setting name or \"all\"]:** Reset a specific setting or all of them.").build();

	@SuppressWarnings("unchecked")
	public Settings() {
		try {
			config = (JSONObject) new JSONParser().parse(new FileReader("serversettings.json"));
		} catch (IOException | ParseException e) {
			config = new JSONObject();
		}

		config.putIfAbsent("default", new JSONObject());
		Set<String> goneServers = new HashSet<String>();

		for (String serverID : (Set<String>) config.keySet()) {
			if (!serverID.equals("default") && !PowerGlove.servers.containsKey(serverID) )
				goneServers.add(serverID);
		}
		for (String serverID : goneServers)
			config.remove(serverID);

		reloadSettings();
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (!PermissionUtil.checkPermission(event.getGuild().getMember(event.getAuthor()), Permission.MANAGE_SERVER)) {
			event.getChannel().sendMessage("\u26A0 Only users with the \"Manage Server\" permission can change the settings.").queue();
			return;
		}

		if (arguments.length < 2) {
			try {
				event.getChannel().sendMessageEmbeds(embed).queue();
			} catch (InsufficientPermissionException e) {
				event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions.\n" + embed.getDescription()).queue();
			}
			return;
		}

		if (arguments.length < 3) {
			event.getChannel().sendMessage("\u26A0 Not enough arguments.").queue();
			return;
		}

		if (arguments[1].toLowerCase().equals("prefix")) {
			if (arguments.length > 3 && arguments[3].toLowerCase().equals("space"))
				arguments[2] += " ";
			changeSetting("prefix", arguments[2].toLowerCase(), event);
		} else if (arguments[1].equals("talktobots")) {
			if (arguments[2].toLowerCase().equals("true") || arguments[2].toLowerCase().equals("false"))
				changeSetting("talktobots", arguments[2].toLowerCase(), event);
			else
				event.getChannel().sendMessage("\u26A0 Setting can only be true or false.").queue();
		} else if (arguments[1].toLowerCase().equals("eastereggs")) {
			if (arguments[2].toLowerCase().equals("true") || arguments[2].toLowerCase().equals("false"))
				changeSetting("eastereggs", arguments[2].toLowerCase(), event);
			else
				event.getChannel().sendMessage("\u26A0 Setting can only be true or false.").queue();
		} else if (arguments[1].toLowerCase().equals("reset")) {
			String serverID = event.getGuild().getId();
			if (arguments[2].toLowerCase().equals("all")) {
				if (!config.containsKey(serverID)) {
					event.getChannel().sendMessage("\u26A0 No settings could be found for this server.").queue();
					return;
				}
				config.remove(serverID);
				event.getChannel().sendMessage("\u2705 Reset all settings for this server.").queue();
			} else {
				if (!config.containsKey(serverID)) {
					event.getChannel().sendMessage("\u26A0 No settings could be found for this server.").queue();
					return;
				}
				JSONObject server = (JSONObject) config.get(serverID);
				if (!server.containsKey(arguments[2].toLowerCase())) {
					event.getChannel().sendMessage("\u26A0 That setting does not exist or it has already been reset.").queue();
					return;
				}
				server.remove(arguments[2].toLowerCase());
				if (server.keySet().isEmpty())
					config.remove(serverID);
				event.getChannel().sendMessage("\u2705 Setting \"" + arguments[2].toLowerCase() + "\" has been reset.").queue();
			}
			reloadSettings();
		} else {
			event.getChannel().sendMessage("\u26A0 Unknown setting.").queue();
		}
	}

	@SuppressWarnings("unchecked")
	public void changeSetting(String setting, String value, MessageReceivedEvent event) {
		String serverID = event.getGuild().getId();

		config.putIfAbsent(serverID, new JSONObject());
		JSONObject server = (JSONObject) config.get(serverID);
		server.put(setting, value);
		reloadSettings();

		event.getChannel().sendMessage("\u2705 Setting \"" + setting + "\" has been set to \"" + value + "\"").queue();
	}

	@SuppressWarnings("unchecked")
	public void reloadSettings() {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("serversettings.json");
			settings = new HashMap<String, Setting>();
			for (String serverID : (Set<String>) config.keySet()) {
				JSONObject server = (JSONObject) config.get(serverID);
				if (serverID.equals("default")) {
					server.putIfAbsent("prefix", "pow ");
					server.putIfAbsent("talktobots", Boolean.toString(false));
					server.putIfAbsent("eastereggs", Boolean.toString(false));
				}
				settings.put(serverID, new Setting((String) server.getOrDefault("prefix", "pow "), Boolean.parseBoolean((String) server.getOrDefault("talktobots", "false")), Boolean.parseBoolean((String) server.getOrDefault("eastereggs", "false"))));
			}
			fileWriter.write(config.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class Setting {
		public String prefix;
		public boolean talktobots;
		public boolean eastereggs;

		public Setting(String prefix, boolean talktobots, boolean eastereggs) {
			this.prefix = prefix;
			this.talktobots = talktobots;
			this.eastereggs = eastereggs;
		}
	}
}
