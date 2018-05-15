package com.rcx.powerglove;

import com.rcx.powerglove.commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

public class PowerGlove {

	public static int shardAmount = 0;
	public static String token = "insert token";
	public static String prefix = "pow";
	public static Boolean onlyTalkToPeople = true;

	public static JDA[] shards = null;
	public static JDA pane = null;

	public static void main(String[] arguments) throws Exception {
		readConfigs();
		JDABuilder api = new JDABuilder(AccountType.BOT).setToken(SecretStuff.tokenPowerGlove);
		api.addEventListener(new CommandListener());
		api.addEventListener(new TalkListener());

		if (shardAmount == 0) {
			pane = api.buildAsync();
			pane.getPresence().setGame(Game.playing("with power"));
		} else {
			shards = new JDA[shardAmount];
			for (int i = 0; i < shardAmount; i++) {
				shards[i] = api.useSharding(i, shardAmount).buildAsync();
				shards[i].getPresence().setGame(Game.playing("with power"));
			}
		}

		SecretStuff.secretMethod();

		System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		CommandListener.commands.put("help", new Help());
		CommandListener.commands.put("science", new Science());
		CommandListener.commands.put("type", new Empty());
		CommandListener.commands.put("info", new CommandSimpleString("This bot is made by <:rcxpick:445610943112806400> RCXcrafter#3845\nYou can join RCXcrafter's server here: https://discord.gg/SthsknG\nYou can add this bot to your own server here: https://discordapp.com/api/oauth2/authorize?client_id=439435998078959616&permissions=37223488&scope=bot"));
		CommandListener.commands.put("anthem", new CommandSimpleString("All rise for the official power glove anthem.\nhttps://soundcloud.com/knifepartyinc/knife-party-power-glove"));
		CommandListener.commands.put("mlg", new MakeMLG());
		CommandListener.commands.put("smiles", new RenderSmiles());
		CommandListener.commands.put("disgusting", new AbsolutelyDisgusting());
		CommandListener.commands.put("dong", new DongFont());
	}

	public static void readConfigs() {
		shardAmount = 1;
		token = "insert token";
		prefix = "pow";
		onlyTalkToPeople = true;
	}
}
