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

import org.discordbots.api.client.DiscordBotListAPI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rcx.powerglove.commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
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
	public static String dListToken = "insert token";
	public static String prefix = "pow ";

	public static Settings settings;
	public static JDA[] shards = null;
	public static JDA pane = null;
	public static DiscordBotListAPI dbl = null;
	public static HttpURLConnection dBots;
	public static HttpURLConnection bots4D;
	public static HttpURLConnection dServ;
	public static HttpURLConnection botList;
	public static HttpURLConnection botWorld;
	public static HttpURLConnection dList;
	public static Map<String, Guild> servers = new HashMap<String, Guild>();

	public static void main(String[] args) throws Exception {
		if (readConfigs())
			return;
		JDABuilder api = new JDABuilder(AccountType.BOT).setToken(token);
		api.addEventListener(new CommandListener());
		api.addEventListener(new TalkListener());

		if (shardAmount == 0) {
			pane = api.buildBlocking();
			pane.getPresence().setGame(Game.playing("with power"));
			for (Guild server : pane.getGuilds())
				servers.put(server.getId(), server);
		} else {
			shards = new JDA[shardAmount];
			for (int i = 0; i < shardAmount; i++) {
				shards[i] = api.useSharding(i, shardAmount).buildBlocking();
				shards[i].getPresence().setGame(Game.playing("with power"));
				for (Guild server : shards[i].getGuilds())
					servers.put(server.getId(), server);
			}
		}

		SecretStuff.secretMethod();

		settings = new Settings();
		System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		CommandListener.commands.put("help", new Help());
		CommandListener.commands.put("settings", settings);
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

		if (!dblToken.equals("insert token"))
			dbl = new DiscordBotListAPI.Builder().token(dblToken).build();
		if (!dBotsToken.equals("insert token"))
			dBots = makeConnection("https://bots.discord.pw/api/bots/439435998078959616/stats", dBotsToken);
		if (!bots4DToken.equals("insert token"))
			bots4D = makeConnection("https://botsfordiscord.com/api/v1/bots/439435998078959616", bots4DToken);
		if (!dServToken.equals("insert token"))
			dServ = makeConnection("https://discord.services/api/bots/439435998078959616", dServToken);
		if (!botListToken.equals("insert token"))
			botList = makeConnection("https://botlist.space/api/bots/439435998078959616", botListToken);
		if (!botWorldToken.equals("insert token"))
			botWorld = makeConnection("https://discordbot.world/api/bot/439435998078959616/stats", botWorldToken);
		if (!dListToken.equals("insert token")) {
			dList = makeConnection("https://discordbot.world/api/bot/439435998078959616/stats", dListToken);
			dList.setRequestProperty("token", dListToken);
			dList.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		}
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
		options.putIfAbsent("dListToken", dListToken);
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
		dListToken = (String) options.get("dListToken");
		autoShutdown = Boolean.parseBoolean((String) options.get("autoShutdown"));

		FileWriter file = new FileWriter("config.json");
		file.write(new org.json.JSONObject(config.toJSONString()).toString(4));
		file.close();
		return token.equals("insert token");
	}
	
	public static void updateAllStats() {
		System.out.println("Posting server count...");
		if (!PowerGlove.dblToken.equals("insert token"))
			PowerGlove.dbl.setStats("439435998078959616", PowerGlove.servers.size());
		if (!PowerGlove.dBotsToken.equals("insert token"))
			postGuildCount(dBots, "server_count");
		if (!bots4DToken.equals("insert token")) {
			postGuildCount(bots4D, "count");
			bots4D.disconnect();
			bots4D = makeConnection("https://botsfordiscord.com/api/v1/bots/439435998078959616", bots4DToken);
		}
		if (!dServToken.equals("insert token")) {
			postGuildCount(dServ, "guild_count");
			dServ.disconnect();
			dServ = makeConnection("https://discord.services/api/bots/439435998078959616", dServToken);
		}
		if (!botListToken.equals("insert token")) {
			postGuildCount(botList, "server_count");
			botList.disconnect();
			botList = makeConnection("https://botlist.space/api/bots/439435998078959616", botListToken);
		}
		if (!botWorldToken.equals("insert token"))
			postGuildCount(botWorld, "guild_count");
		if (!dListToken.equals("insert token")) {
			try {
				DataOutputStream wr = new DataOutputStream (dList.getOutputStream());
				wr.writeBytes("token=" + dListToken + "&servers=" + PowerGlove.servers.size());
				wr.close();

				DataInputStream rd = new DataInputStream (dList.getInputStream());
				BufferedReader d = new BufferedReader(new InputStreamReader(rd));
				String line;
				while ((line = d.readLine()) != null)
					System.out.println(line);
				d.close();
				rd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Server count posted.");
	}
	
	public static HttpURLConnection makeConnection(String url, String token) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("User-Agent", "PowerGlove");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", token);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	@SuppressWarnings("unchecked")
	public static void postGuildCount(HttpURLConnection site, String countName) {
		try {
			DataOutputStream wr = new DataOutputStream (site.getOutputStream());
			JSONObject put = new JSONObject();
			put.put(countName, Integer.toString(PowerGlove.servers.size()));
			wr.writeBytes(put.toJSONString());
			wr.close();

			DataInputStream rd = new DataInputStream (site.getInputStream());
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			while ((line = d.readLine()) != null)
				System.out.println(line);
			d.close();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
