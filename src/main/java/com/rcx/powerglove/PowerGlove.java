package com.rcx.powerglove;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rcx.powerglove.commands.AbsolutelyDisgusting;
import com.rcx.powerglove.commands.Afk;
import com.rcx.powerglove.commands.Cancel;
import com.rcx.powerglove.commands.CommandSimpleEmbed;
import com.rcx.powerglove.commands.DongFont;
import com.rcx.powerglove.commands.Empty;
import com.rcx.powerglove.commands.Exit;
import com.rcx.powerglove.commands.GifBPM;
import com.rcx.powerglove.commands.Help;
import com.rcx.powerglove.commands.MakeMLG;
import com.rcx.powerglove.commands.Mastermind;
import com.rcx.powerglove.commands.MastermindGuess;
import com.rcx.powerglove.commands.RandomColor;
import com.rcx.powerglove.commands.RenderSmiles;
import com.rcx.powerglove.commands.Science;
import com.rcx.powerglove.commands.Settings;
import com.rcx.powerglove.commands.Talk;
import com.rcx.powerglove.commands.Ventriloquism;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PowerGlove {

	public static int shardAmount = 0;
	public static Boolean autoShutdown = true;
	public static String token = "insert token";
	public static String prefix = "pow ";
	public static String id = "1011730784152981514";

	public static ShardManager api = null;
	public static Map<String, Guild> servers = new HashMap<String, Guild>();

	public static void main(String[] args) throws Exception {
		if (readConfigs())
			return;

		api = DefaultShardManagerBuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT).setShardsTotal(-1).setActivity(Activity.playing("with power")).setStatus(OnlineStatus.IDLE).build();

		for (JDA shard : api.getShards()) {
			while (shard.getStatus().ordinal() < Status.CONNECTED.ordinal()) {
				Thread.sleep(50);
			}
			for (Guild server : shard.getGuilds())
				servers.put(server.getId(), server);
		}

		System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
		CommandListener.commands.put("help", new Help());
		CommandListener.commands.put("settings", new Settings());
		CommandListener.commands.put("science", new Science());
		CommandListener.commands.put("type", new Empty());
		CommandListener.commands.put("info", new CommandSimpleEmbed(new EmbedBuilder().setColor(0x419399).setAuthor("Info", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
				"This bot is made by <:rcxpick:445610943112806400> RCXcrafter#3845"
						+ "\nYou can join RCXcrafter's server here: https://discord.gg/SthsknG"
						+ "\nThe source code can be found here: https://github.com/RCXcrafter/PowerGlove"
						+ "\nYou can add this bot to your own server here: https://discord.com/oauth2/authorize?client_id=1011730784152981514&scope=bot").build()));
		CommandListener.commands.put("mlg", new MakeMLG());
		CommandListener.commands.put("smiles", new RenderSmiles());
		CommandListener.commands.put("disgusting", new AbsolutelyDisgusting());
		CommandListener.commands.put("dong", new DongFont());
		CommandListener.commands.put("talk", new Talk());
		CommandListener.commands.put("afk", new Afk());
		CommandListener.commands.put("stop", new Exit());
		CommandListener.commands.put("mastermind", new Mastermind());
		CommandListener.commands.put("guess", new MastermindGuess());
		CommandListener.commands.put("color", new RandomColor());
		CommandListener.commands.put("cancel", new Cancel());
		CommandListener.commands.put("vent", new Ventriloquism());

		api.addEventListener(new CommandListener());
		new TalkListener();

		api.getShardById(0).updateCommands()
		.addCommands(Commands.slash("filebpm", "Change the bpm of a gif").addOptions(
				new OptionData(OptionType.ATTACHMENT, "gif", "The gif in question", true),
				new OptionData(OptionType.NUMBER, "bpm", "The bpm you want to change the gif to", true),
				new OptionData(OptionType.INTEGER, "beats", "The amount of beats in the gif", true)))
		.addCommands(Commands.slash("gifbpm", "Change the bpm of a gif").addOptions(
				new OptionData(OptionType.STRING, "gif", "The url of the gif or an animated emote", true),
				new OptionData(OptionType.NUMBER, "bpm", "The bpm you want to change the gif to", true),
				new OptionData(OptionType.INTEGER, "beats", "The amount of beats in the gif", true))).queue();
		/*.addCommands(Commands.slash("emotebpm", "Change the bpm of an animated emote").addOptions(
				new OptionData(OptionType.MENTIONABLE, "gif", "The animated emote", true),
				new OptionData(OptionType.NUMBER, "bpm", "The bpm you want to change the gif to", true),
				new OptionData(OptionType.INTEGER, "beats", "The amount of beats in the gif", true))).queue();*/
		//the client doesn't think emotes are mentionables >:(

		GifBPM gifbpm = new GifBPM();
		CommandListener.slashCommands.put("filebpm", gifbpm);
		CommandListener.slashCommands.put("gifbpm", gifbpm);
		CommandListener.slashCommands.put("emotebpm", gifbpm);

		if (autoShutdown)
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					System.out.println("shutting down");
					System.exit(0);
				}
			}, 1, TimeUnit.DAYS);

		api.setStatus(OnlineStatus.ONLINE);
	}

	@SuppressWarnings("unchecked")
	public static boolean readConfigs() throws Exception {
		JSONObject config;
		try {
			config = (JSONObject) new JSONParser().parse(new FileReader("config.json"));
		} catch (IOException e) {
			config = new JSONObject();
		}
		config.putIfAbsent("options", new JSONObject());
		JSONObject options = (JSONObject) config.get("options");

		options.putIfAbsent("shardAmount", Integer.toString(shardAmount));
		options.putIfAbsent("token", token);
		options.putIfAbsent("prefix", prefix);
		options.putIfAbsent("autoShutdown", autoShutdown.toString());

		shardAmount = Integer.parseInt((String) options.get("shardAmount"));
		token = (String) options.get("token");
		prefix = (String) options.get("prefix");
		autoShutdown = Boolean.parseBoolean((String) options.get("autoShutdown"));

		FileWriter file = new FileWriter("config.json");
		file.write(config.toJSONString());
		file.close();
		return token.equals("insert token");
	}
}
