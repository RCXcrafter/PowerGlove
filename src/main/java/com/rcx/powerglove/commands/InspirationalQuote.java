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
			"Haha what.",
			"Des, pa, cito.",
			"One day, someone wil qoute me.",
			"The problem with qoutes on the internet is that you can't always rely on thier accuracy.",
			"Everything has an end, but a sausage has two.",
			"Something wonderful is about to happy.",
			"What did he mean by this?",
			"Welcome to the internet, I will be your guide.",
			"Vroom vroom.",
			"This statement is false.",
			"Thank you for helping us help you help us all.",
			"And now for something completely different.",
			"I like dank memes and I can not lie.",
			"Just, do it!",
			"How do you tame a horse in minecraft?",
			"What would happen if you throw a jar of mayonaise into a black hole?",
			"You're kid now, you're a squid now, you're kid now, you're a squid now, you're kid now, you're a squid now.",
			"This, is, spartaaaaaaaaaa!",
			"Sus.",
			"Honk!",
			"It's amazing!",
			"And that's why you never walk sideways on a gravel path.",
			"I'm you from the future! Whatever you do, don't spill that coffee!",
			"What does the fox say?",
			"Authentic, organic, natural.",
			"You tried.",
			"Yabadabadoo!",
			"Skreeonk!"
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
			"Gareth McGrillen",
			"Darth Vader",
			"Markus Persson",
			"Steve Jobs",
			"No one Ever",
			"Gabe Newell",
			"Mata Nui",
			"Adolf Hitler",
			"Julius Ceasar",
			"Arnold Schwarzenegger",
			"John Cena",
			"Mario Jumpman",
			"Isaac Newton"
	};

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage("*\"" + quotes[rand.nextInt(quotes.length)]
				+ "\"*\n-" + people[rand.nextInt(people.length)] + ", " + (1100 + rand.nextInt(1000))).queue();
	}
}
