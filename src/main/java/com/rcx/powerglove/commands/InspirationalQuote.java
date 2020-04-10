package com.rcx.powerglove.commands;

import java.util.Random;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InspirationalQuote extends Command {

	Random rand = new Random();

	String[] quotes = {
			"Don't shitpost where you eatpost.",
			"How much wood would a woodchuck chuck if a woodchuck could chuck wood?",
			"Haha what.",
			"One day, somewhere in the future, my work will be quoted!",
			"The problem with qoutes on the internet is that you can't always rely on thier accuracy.",
			"Everything has an end, but a sausage has two.",
			"Something wonderful is about to happy.",
			"What did he mean by this?",
			"Welcome to the internet, I will be your guide.",
			"Vroom vroom.",
			"This statement is false.",
			"Thank you for helping us help you help us all.",
			"And now for something completely different.",
			"Just, do it!",
			"How do you tame a horse in minecraft?",
			"The sky is not blue.",
			"Your time is up, my time is now.",
			"Air like that is unreal, it doesn't even happen (most of the time).",
			"It's dangerous to go alone, take this."
	};

	String[] people = {
			"Albert Einstein",
			"Abraham Lincoln",
			"Bob the Builder",
			"Ash Ketchum",
			"Barack Obama",
			"Stephen Hawking",
			"Bob Ross",
			"Kanye West",
			"Darth Vader",
			"Steve Jobs",
			"No one Ever",
			"Gabe Newell",
			"Adolf Hitler",
			"Julius Ceasar",
			"Arnold Schwarzenegger",
			"John Cena",
			"Isaac Newton"
	};

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage("*\"" + quotes[rand.nextInt(quotes.length)] + "\"*\n-" + people[rand.nextInt(people.length)] + ", " + (1100 + rand.nextInt(1000))).queue();
	}
}
