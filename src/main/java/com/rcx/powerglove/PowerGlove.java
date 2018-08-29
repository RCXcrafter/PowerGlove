package com.rcx.powerglove;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rcx.powerglove.commands.*;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDA.Status;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

public class PowerGlove {

	public static int shardAmount = 0;
	public static Boolean autoShutdown = true;
	public static String token = "insert token";
	public static String dblToken = "insert token";
	public static String dBotsToken = "insert token";
	public static String bots4DToken = "insert token";
	public static String dServToken = "insert token";
	public static String botListToken = "insert token";
	public static String botWorldToken = "insert token";
	public static String listcordToken = "insert token";
	public static String botsOnDToken = "insert token";
	public static String dBotsGroupToken = "insert token";
	public static String boatsClubToken = "insert token";
	public static String dbl2Token = "insert token";
	public static String dbBotsToken = "insert token";
	public static String ddBotsToken = "insert token";
	public static String prefix = "pow ";

	public static ShardManager api = null;
	public static Map<String, Guild> servers = new HashMap<String, Guild>();

	public static void main(String[] args) throws Exception {
		if (readConfigs())
			return;
		api = new DefaultShardManagerBuilder().setToken(token).setShardsTotal(-1).setGame(Game.playing("with power")).build();

		for (JDA shard : api.getShards()) {
			while (shard.getStatus().ordinal() < Status.CONNECTED.ordinal()) {
				Thread.sleep(50);
			}
			for (Guild server : shard.getGuilds())
				servers.put(server.getId(), server);
		}

		SecretStuff.secretMethod();

		System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		CommandListener.commands.put("help", new Help());
		CommandListener.commands.put("settings", new Settings());
		CommandListener.commands.put("science", new Science());
		CommandListener.commands.put("type", new Empty());
		CommandListener.commands.put("info", new CommandSimpleEmbed(new EmbedBuilder().setColor(0x419399).setAuthor("Info", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
				"This bot is made by <:rcxpick:445610943112806400> RCXcrafter#3845"
				+ "\nYou can join RCXcrafter's server here: https://discord.gg/SthsknG"
				+ "\nThe source code can be found here: https://github.com/RCXcrafter/PowerGlove"
				+ "\nYou can add this bot to your own server here: https://discordapp.com/oauth2/authorize?client_id=439435998078959616&scope=bot&permissions=104332352").build()));
		CommandListener.commands.put("anthem", new CommandSimpleString("All rise for the official power glove anthem.\nhttps://soundcloud.com/knifepartyinc/knife-party-power-glove"));
		CommandListener.commands.put("mlg", new MakeMLG());
		CommandListener.commands.put("smiles", new RenderSmiles());
		CommandListener.commands.put("disgusting", new AbsolutelyDisgusting());
		CommandListener.commands.put("dong", new DongFont());
		CommandListener.commands.put("talk", new Talk());
		CommandListener.commands.put("afk", new Afk());
		CommandListener.commands.put("stop", new Exit());
		CommandListener.commands.put("quote", new InspirationalQuote());
		CommandListener.commands.put("mastermind", new Mastermind());
		CommandListener.commands.put("guess", new MastermindGuess());
		CommandListener.commands.put("nsfw", new Nsfw());
		CommandListener.commands.put("color", new RandomColor());

		api.addEventListener(new CommandListener());

		updateAllStats();

		if (autoShutdown)
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					System.out.println("shutting down");
					System.exit(0);
				}
			}, 1, TimeUnit.DAYS);
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
		options.putIfAbsent("dblToken", dblToken);
		options.putIfAbsent("dBotsToken", dBotsToken);
		options.putIfAbsent("bots4DToken", bots4DToken);
		options.putIfAbsent("dServToken", dServToken);
		options.putIfAbsent("botListToken", botListToken);
		options.putIfAbsent("botWorldToken", botWorldToken);
		options.putIfAbsent("listcordToken", listcordToken);
		options.putIfAbsent("botsOnDToken", botsOnDToken);
		options.putIfAbsent("dBotsGroupToken", dBotsGroupToken);
		options.putIfAbsent("boatsClubToken", boatsClubToken);
		options.putIfAbsent("dbl2Token", dbl2Token);
		options.putIfAbsent("dbBotsToken", dbBotsToken);
		options.putIfAbsent("ddBotsToken", ddBotsToken);
		options.putIfAbsent("autoShutdown", autoShutdown.toString());

		shardAmount = Integer.parseInt((String) options.get("shardAmount"));
		token = (String) options.get("token");
		prefix = (String) options.get("prefix");
		dblToken = (String) options.get("dblToken");
		dBotsToken = (String) options.get("dBotsToken");
		bots4DToken = (String) options.get("bots4DToken");
		dServToken = (String) options.get("dServToken");
		botListToken = (String) options.get("botListToken");
		botWorldToken = (String) options.get("botWorldToken");
		listcordToken = (String) options.get("listcordToken");
		botsOnDToken = (String) options.get("botsOnDToken");
		dBotsGroupToken = (String) options.get("dBotsGroupToken");
		boatsClubToken = (String) options.get("boatsClubToken");
		dbl2Token = (String) options.get("dbl2Token");
		dbBotsToken = (String) options.get("dbBotsToken");
		ddBotsToken = (String) options.get("ddBotsToken");
		autoShutdown = Boolean.parseBoolean((String) options.get("autoShutdown"));

		FileWriter file = new FileWriter("config.json");
		file.write(new org.json.JSONObject(config.toJSONString()).toString(4));
		file.close();
		return token.equals("insert token");
	}
	
	public static void updateAllStats() {
		System.out.println("Posting server count...");
		if (!dblToken.equals("insert token"))
			postGuildCount("https://discordbots.org/api/bots/439435998078959616/stats", dblToken, "server_count");
		if (!dBotsToken.equals("insert token"))
			postGuildCount("https://bots.discord.pw/api/bots/439435998078959616/stats", dBotsToken, "server_count");
		if (!bots4DToken.equals("insert token"))
			postGuildCount("https://botsfordiscord.com/api/v1/bots/439435998078959616", bots4DToken, "count");
		if (!dServToken.equals("insert token"))
			postGuildCount("https://discord.services/api/bots/439435998078959616", dServToken, "guild_count");
		if (!botListToken.equals("insert token"))
			postGuildCount("https://botlist.space/api/bots/439435998078959616", botListToken, "server_count");
		if (!botWorldToken.equals("insert token"))
			postGuildCount("https://discordbot.world/api/bot/439435998078959616/stats", botWorldToken, "guild_count");
		if (!listcordToken.equals("insert token"))
			postGuildCount("https://listcord.com/api/bot/439435998078959616/guilds", listcordToken, "guilds");
		if (!botsOnDToken.equals("insert token"))
			postGuildCount("https://bots.ondiscord.xyz/bot-api/bots/439435998078959616/guilds", botsOnDToken, "guildCount");
		if (!dBotsGroupToken.equals("insert token"))
			postGuildCount("https://discordbots.group/api/bot/439435998078959616", dBotsGroupToken, "count");
		if (!boatsClubToken.equals("insert token"))
			postGuildCount("https://discordboats.club/api/public/bot/stats", boatsClubToken, "server_count");
		if (!dbl2Token.equals("insert token"))
			postGuildCount("https://discordbotlist.com/api/bots/439435998078959616/stats", dbl2Token, "guilds");
		if (!dbBotsToken.equals("insert token"))
			postGuildCount("https://discordsbestbots.xyz/api/bots/439435998078959616", dbBotsToken, "guilds");
		if (!ddBotsToken.equals("insert token"))
			postGuildCount("https://bots-discord.tk/bots/439435998078959616/stats", ddBotsToken, "server_count");
		System.out.println("Server count posted.");
	}

	@SuppressWarnings("unchecked")
	public static void postGuildCount(String url, String webToken, String countName) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL(url).openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", webToken);
			site.setRequestMethod("POST");
			site.setDoOutput(true);
			site.setDoInput(true);
			site.connect();
			
			DataOutputStream wr = new DataOutputStream (site.getOutputStream());
			JSONObject put = new JSONObject();
			if (url.equals("https://discordbotlist.com/api/bots/439435998078959616/stats"))
				put.put("shard_id", Integer.toString(0));
			if (url.equals("https://bots-discord.tk/bots/439435998078959616/stats") || url.equals("https://discordsbestbots.xyz/api/bots/439435998078959616"))
				put.put("shards", api.getShardsTotal());
			put.put(countName, Integer.toString(servers.size()));
			wr.writeBytes(put.toJSONString());
			wr.close();

			DataInputStream rd = new DataInputStream (site.getInputStream());
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			while ((line = d.readLine()) != null)
				System.out.println(line);
			d.close();
			rd.close();
			site.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
