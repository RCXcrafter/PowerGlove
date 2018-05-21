package com.rcx.powerglove.commands;

import java.util.Random;

import com.rcx.powerglove.PowerGlove;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command {

	public static String[] powerQuote = {
			"Now you're playing with power",
			"Now you're playing with power",
			"Now you're playing with power",
			"Now you're playing with power",
			"Now you're playing with power",
			"Powerplay",
			"Super power",
			"Ooh eeh ooh ah ah ting tang walla walla bing bang",
			"Everything else is childs play",
			"Square, rectangle, trapezoid, tetrizoid",
			"Rad",
			"Orange transparent chainsaw",
			"Yo yo, piraka",
			"Welcome to the space jam",
			"Donkey Kong is here",
			"It's amazing",
			"Multifunctionomical",
			"Laboricious",
			"Иitro",
			"It's hip to be square",
			"Your name is Bob",
			"Pranfeuri",
			"Programmed in java",
			"Feel the power of love",
			"Now you're thinking with portals",
			"Fuzzy pickles",
			"We found an octagon",
			"Numbers are hard",
			"You got to flip it turn-ways",
			"Knock knock it's knuckles",
			"It's so bad",
			"Thank you for helping us help you help us all",
			"Redesign your logo",
			"<a:rcxsword:445609872210264066>",
			"Awesomazing",
			"I can feel the power",
			"I have the power"
	};
	
	String avatarUrl = "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png";
	
	EmbedBuilder embed = new EmbedBuilder().setColor(0x419399).setAuthor("Power Glove Help", null, avatarUrl).setThumbnail(avatarUrl).appendDescription(
			"Prefix = \"" + PowerGlove.prefix + "\""
			+"\nHere's a list of all the commands:\n"
			+ "\n\u2022 **help:** Displays this list, helpful isn't it?"
			+ "\n\u2022 **settings:** Change the settings for this server."
			+ "\n\u2022 **science:** Posts a science related picture."
			+ "\n\u2022 **type:** Makes the bot start typing."
			+ "\n\u2022 **info:** Gives some info about this bot."
			+ "\n\u2022 **anthem:** Posts the theme song of this bot."
			+ "\n\u2022 **mlg [user]:** Make someone (or yourself) mlg."
			+ "\n\u2022 **smiles [smiles]:** Give it a SMILES formula and it will draw the molecule for you. https://en.wikipedia.org/wiki/Simplified_molecular-input_line-entry_system"
			+ "\nExample: pow smiles CN1C=NC2=C1C(=O)N(C(=O)N2C)C"
			+ "\n\u2022 **disgusting [text]:** Insert your own text into the absolutely disgusting meme."
			+ "\n\u2022 **dong [text]:** Convert text into expand dong text."
			+ "\n\u2022 **talk:** Make the bot start or stop talking in the current channel.\n"
			+ "\nThis bot will also respond to certain key phrases/words:"
			+ "\n\u2022 If you say \"delete this message\", your message will be deleted."
			+ "\n\u2022 Say \"des\", and your sentence will be finished."
			+ "\n\u2022 Start your message with \"poll:\" to start a poll.");

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		event.getChannel().sendMessage(embed.setTitle(powerQuote[new Random().nextInt(powerQuote.length)]).build()).queue();
	}
}
