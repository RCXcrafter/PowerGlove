package com.rcx.powerglove.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class Mastermind implements Command {

	public static Map<String, MasterMindGame> games = new HashMap<String, MasterMindGame>();

	Random rand = new Random();
	String[] alphabet = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
	String[] numbers = "0 1 2 3 4 5 6 7 8 9".split(" ");
	String[] shapes = "\u274C \u2B55 \uD83D\uDD36 \uD83D\uDD32 \u2B50 \uD83D\uDD3A".split(" ");
	String[] standard = "\uD83C\uDF4A \uD83D\uDD35 \uD83D\uDD34 \uD83D\uDCC0 \uD83C\uDFBE \uD83D\uDE08".split(" ");

	MessageEmbed embed = new EmbedBuilder().setColor(0x419399).setAuthor("Mastermind", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
			"\nAll games automatically end after 10 minutes of inactivity.\n"
					+ "\nHow to use this command:\n"
					+ "\n**mastermind [start/stop] [codesize] [turns] [digits]:**"
					+ "\n If you leave out any of the arguments, it will use the default"
					+ "\n If you don't know the rules of mastermind, use \"pow mastermind rules\""
					+ "\n\u2022 **[start/stop]** Start or stop a game"
					+ "\n\u2022 **[codesize]** Change the length of the code, default: 4"
					+ "\n\u2022 **[turns]** The amount of turns you have to guess the code, default: 12"
					+ "\n\u2022 **[digits]** The \"digits\" that can be used for the code, default: 6 colors"
					+ "\nSimply enter all the words, letters or emotes you want to use divided by spaces or use a preset."
					+ "\nPresets:"
					+ "\n\u2022  **default:** The standard 6 colors."
					+ "\n\u2022  **shapes:** 6 different shapes, useful if you're colorblind."
					+ "\n\u2022  **numbers:** 10 different numbers."
					+ "\n\u2022  **alphabet:** 26 different letters.").build();

	MessageEmbed helpEmbed = new EmbedBuilder().setColor(0x419399).setAuthor("Mastermind", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
			"When you start a game, the bot generates a code which you have to guess."
					+ "\nThis code can contain duplicate colors."
					+ "\nBy default you get 12 turns to crack the code using \"pow guess\"."
					+ "\nAfter you guess the bot will show you your guess followed by black and/or white dots."
					+ "\n\u26AA A white dot indicates that one of the colors you guessed is in the code, but it's not in the correct position."
					+ "\n\u26AB A black dot indicates that one of the colors you guessed is in the code, and it's in the correct position."
					+ "\nIf you want to know more about mastermind, look here: [Mastermind on Wikipedia](https://en.wikipedia.org/w/index.php?title=Mastermind&oldid=838132629)").build();

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (arguments.length < 2) {
			try {
				event.getChannel().sendMessageEmbeds(embed).queue();
			} catch (InsufficientPermissionException e) {
				event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions.\n" + new String(embed.getDescription())).queue();
			}
			return;
		}
		String gameID = event.getGuild().getId() + event.getChannel().getId() + event.getAuthor().getId();

		if (arguments[1].equals("rules")) {
			try {
				event.getChannel().sendMessageEmbeds(helpEmbed).queue();
			} catch (InsufficientPermissionException e) {
				event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions.\n" + new String(helpEmbed.getDescription())).queue();
			}
		} else if (arguments[1].equals("stop")) {
			if (games.containsKey(gameID)) {
				games.remove(gameID);
				event.getChannel().sendMessage("Game stopped.").queue();
			} else {
				event.getChannel().sendMessage("\u26A0 You're not in a game right now.").queue();
			}
		} else if (arguments[1].equals("start")) {
			if (games.containsKey(gameID)) {
				try {
					event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
				} catch (InsufficientPermissionException e) {
				}
				event.getChannel().sendMessage("\u26A0 You're already in a game, stop or finish it before starting a new one.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				return;
			}
			String[] colors = standard;
			int codeSize = 4;
			int turns = 12;

			if (arguments.length > 2) {
				try {
					codeSize = Integer.parseInt(arguments[2]);
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage("\u26A0 \"" + arguments[2] + "\" is not a valid number.").queue();
				}
				if (arguments.length > 3) {
					try {
						turns = Integer.parseInt(arguments[3]);
					} catch (NumberFormatException e) {
						event.getChannel().sendMessage("\u26A0 \"" + arguments[3] + "\" is not a valid number.").queue();
					}
					if (arguments.length > 4) {
						if (arguments[4].equals("default") || arguments[4].equals("standard"))
							colors = standard;
						else if (arguments[4].equals("numbers") || arguments[4].equals("digits"))
							colors = numbers;
						else if (arguments[4].equals("letters") || arguments[4].equals("alphabet"))
							colors = alphabet;
						else if (arguments[4].equals("shapes") || arguments[4].equals("colorblind"))
							colors = shapes;
						else {
							colors = new String[arguments.length - 4];
							for (int i = 4; i < arguments.length; i++)
								colors[i - 4] = arguments[i];
						}
					}
				}
			}
			String [] code = new String[codeSize];
			for (int i = 0; i < codeSize; i++) {
				code[i] = colors[rand.nextInt(colors.length)];
			}

			String colorsString = "";
			for (String digit : colors)
				colorsString += digit;

			String aliasses = "";
			if (colors.equals(standard))
				aliasses = "\nInstead of using the emotes, you can also use: orange blue red yellow green purple";
			else if (colors.equals(shapes))
				aliasses = "\nInstead of using the emotes, you can also use: cross circle diamond square star triangle";
			games.put(gameID, new MasterMindGame(colors, codeSize, turns, code, gameID));
			event.getChannel().sendMessage("A code has been generated, use \"pow guess\" to take a guess at it."
					+ "\nThe code can consist of: " + colorsString + aliasses).queue();
		} else {
			event.getChannel().sendMessage("\u26A0 Invalid arguments.").queue();
		}
	}

	public class MasterMindGame {
		public String[] colors;
		public int codeSize;
		public int turns;
		public String[] code;
		public String id;
		public List<String[]> guesses = new ArrayList<String[]>();
		ScheduledFuture<?> stopTimer;

		public MasterMindGame(String[] colors, int codeSize, int turns, String[] code, String key) {
			this.colors = colors;
			this.codeSize = codeSize;
			this.turns = turns;
			this.code = code;
			this.id = key;
			this.stopTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					games.remove(id);
				}
			}, 10, TimeUnit.MINUTES);
		}
	}
}
