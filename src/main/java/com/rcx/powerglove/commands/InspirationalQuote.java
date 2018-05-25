package com.rcx.powerglove.commands;

import java.util.Random;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InspirationalQuote extends Command {

	Random rand = new Random();

	String[] quotes = {
			"Don't shitpost where you eatpost.",
			"How much wood would a woodchuck chuck if a woodchuck could chuck wood?",
			"A bear is not a penguin.",
			"One plus one equals window.",
			""
	};

	String[] people = {
			"Albert Einstein",
			"Louis Fonsi",
			"Ringo Starr",
			"Abraham Lincoln",
			"Jack Black",
			"Kyle Gass",
			"Bob the Builder",
			"Ash Ketchum",
			"Barack Obama",
			"Stephen Hawking",
			"Bob Ross",
			"Neil Cicierega",
			"Kanye West",
			"Twilight Sparkle",
			"Rob Swire",
			"Darth Vader",
			"Markus Persson",
			"Steve Jobs",
			"Nobody Ever",
			"Gabe Newell",
			"Mata Nui",
			"Adolf Hitler",
			"Julius Ceasar",
			"Arnold Schwarzenegger"
	};

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage("\"" + quotes[rand.nextInt(quotes.length)]
				+ "\"\n-" + people[rand.nextInt(people.length)] + " " + (1000 + rand.nextInt(2000))).queue();
	}
}
