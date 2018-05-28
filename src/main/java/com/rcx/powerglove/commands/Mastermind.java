package com.rcx.powerglove.commands;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Mastermind extends Command {
	
	public static Map<String, MasterMindGame> games = new HashMap<String, MasterMindGame>();

	String alphabet = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
	String numbers = "0 1 2 3 4 5 6 7 8 9";
	String shapes = ":x: :red_circle: :large_orange_diamond: :black_square_button: :star: :small_red_triangle:";
	String standard = "\uD83C\uDF4A \uD83D\uDD35 \uD83D\uDD34 \uD83D\uDCC0 \uD83C\uDFBE \uD83D\uDE08";

	MessageEmbed embed = new EmbedBuilder().setColor(0x419399).setAuthor("Mastermind", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").appendDescription(
			"Here's a list of all the settings:\n"
					+ "\n\u2022 **prefix [prefix] [space]:** Add an alternate prefix for the bot to use. If you want to have a space between the prefix and the command, add \"space\" as the second argument"
					+ "\n\u2022 **talktobots [true/false]:** If the bot should respond to other bots. default: false"
					+ "\n\u2022 **reset [setting name or \"all\"]:** Reset a specific setting or all of them.").build();

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (arguments.length < 2) {
			event.getChannel().sendMessage(embed).queue();
			return;
		}
	}
	
	public void play(String[] guesses, MessageReceivedEvent event, MasterMindGame game) {
		
	}
	
	public class MasterMindGame {
		public String[] colors;
		public boolean someBOOLEAN;

		public MasterMindGame(String[] colors, boolean someBOOLEAN) {
			this.colors = colors;
			this.someBOOLEAN = someBOOLEAN;
		}
	}
}
