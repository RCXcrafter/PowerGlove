package com.rcx.powerglove.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MastermindGuess extends Command {

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		String gameID = event.getGuild().getId() + event.getChannel().getId() + event.getAuthor().getId();
		if (!Mastermind.games.containsKey(gameID)) {
			event.getChannel().sendMessage("\u26A0 You're not in a game right now.").queue();
			return;
		}

		String[] guess = new String[arguments.length - 1];
		for (int i = 1; i < arguments.length; i++)
			guess[i - 1] = arguments[i];

		Mastermind.MasterMindGame game = Mastermind.games.get(gameID);
		if (guess.length != game.codeSize) {
			try {
				event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
			} catch (Exception e) {}
			event.getChannel().sendMessage("\u26A0 Your guess is not the same length as the code, it should be " + game.codeSize + " long.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
			return;
		}

		for (int i = 0; i < guess.length; i++) {
			if (guess[i].toLowerCase().equals("orange")) {
				guess[i] = "\uD83C\uDF4A";
				continue;
			} else if (guess[i].toLowerCase().equals("blue")) {
				guess[i] = "\uD83D\uDD35";
				continue;
			} else if (guess[i].toLowerCase().equals("red")) {
				guess[i] = "\uD83D\uDD34";
				continue;
			} else if (guess[i].toLowerCase().equals("yellow")) {
				guess[i] = "\uD83D\uDCC0";
				continue;
			} else if (guess[i].toLowerCase().equals("green")) {
				guess[i] = "\uD83C\uDFBE";
				continue;
			} else if (guess[i].toLowerCase().equals("purple")) {
				guess[i] = "\uD83D\uDE08";
				continue;
			} else if (guess[i].toLowerCase().equals("cross")) {
				guess[i] = "\u274C";
				continue;
			} else if (guess[i].toLowerCase().equals("circle")) {
				guess[i] = "\u2B55";
				continue;
			} else if (guess[i].toLowerCase().equals("diamond")) {
				guess[i] = "\uD83D\uDD36";
				continue;
			} else if (guess[i].toLowerCase().equals("square")) {
				guess[i] = "\uD83D\uDD32";
				continue;
			} else if (guess[i].toLowerCase().equals("star")) {
				guess[i] = "\u2B50";
				continue;
			} else if (guess[i].toLowerCase().equals("triangle")) {
				guess[i] = "\uD83D\uDD3A";
				continue;
			}
		}

		for (String digit : guess) {
			Boolean match = false;
			for (String color : game.colors) {
				if (digit.equals(color)) {
					match = true;
					break;
				}
			}
			if (!match) {
				try {
					event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
				} catch (Exception e) {}
				event.getChannel().sendMessage("\u26A0 \"" + digit + "\" is not valid, try again.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				return;
			}
		}

		try {
			event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
		} catch (Exception e) {}
		game.guesses.add(guess);

		int blackPins = 0;
		int whitePins = 0;
		List<Integer> alreadyMatched = new ArrayList<Integer>();
		for (int i = 0; i < game.codeSize; i++) {
			for (int i2 = 0; i2 < game.codeSize; i2++) {
				if (!alreadyMatched.contains(i2) && game.code[i2].equals(guess[i])) {
					whitePins += 1;
					alreadyMatched.add(i2);
					break;
				}
			}
			if (game.code[i].equals(guess[i]))
				blackPins += 1;
		}

		whitePins -= blackPins;
		String pins = " ";
		for (int i = 0; i < blackPins; i++)
			pins += "\u26AB";
		for (int i = 0; i < whitePins; i++)
			pins += "\u26AA";

		String guessString = "";
		for (String digit : guess)
			guessString += digit;
		event.getChannel().sendMessage(guessString + pins).queue();
		if (blackPins == game.codeSize) {
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Congratulations, you won in " + game.guesses.size() + " turns.").queue();
			Mastermind.games.remove(gameID);
		} else if (game.guesses.size() == game.turns) {
			event.getChannel().sendTyping().queue();
			String codeString = "";
			for (String digit : game.code)
				codeString += digit;
			event.getChannel().sendMessage("Whoops, you lost. The code was: " + codeString).queue();
			Mastermind.games.remove(gameID);
		} else {
			game.stopTimer.cancel(true);
			game.stopTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					Mastermind.games.remove(game.id);
				}
			}, 10, TimeUnit.MINUTES);
		}
	}
}
