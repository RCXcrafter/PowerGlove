package com.rcx.powerglove.commands;

import java.util.Random;

import com.rcx.powerglove.PowerGlove;
import com.rcx.powerglove.commands.Settings.Setting;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class Help implements Command {

	Random rand = new Random();

	public static String[] powerQuote = {
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
			"\u0376itro",
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
			"I can feel the power of fire",
			"I have the power",
			"Pa pa pa pa pa power",
			"The power is mine",
			"Infinite power",
			"I just need more power",
			"What's his power level?",
			"Young man, you must use what little power you have to save the princess and conquer the world",
			"He's become too powerful for his own good",
			"Powerful stuff",
			"Firepower",
			"Horsepower"
	};

	String description = "Prefix = \"" + PowerGlove.prefix + "\"";

	String lameversion = "\nHere's a list of all the commands:"
			+ "\n\u2022 **help:** Displays this list, helpful isn't it?"
			+ "\n\u2022 **settings:** Change the settings for this server."
			+ "\n\u2022 **science:** Posts a science related picture."
			+ "\n\u2022 **type:** Makes the bot start typing."
			+ "\n\u2022 **info:** Gives some info about this bot."
			+ "\n\u2022 **mlg [user]:** Make someone (or yourself) mlg."
			+ "\n\u2022 **smiles [smiles]:** Give it a SMILES formula and it will draw the molecule for you (reaction smiles are also supported). https://en.wikipedia.org/wiki/Simplified_molecular-input_line-entry_system"
			+ "\nExample: pow smiles CN1C=NC2=C1C(=O)N(C(=O)N2C)C"
			+ "\n\u2022 **disgusting [text]:** Insert your own text into the absolutely disgusting meme."
			+ "\n\u2022 **dong [text]:** Convert text into expand dong text."
			+ "\n\u2022 **talk:** Make the bot start or stop talking in the current channel."
			+ "\n\u2022 **afk: [optional reason]** Set yourself away from keyboard (globally)."
			+ "\n\u2022 **mastermind:** Play a nice game of mastermind against the bot."
			+ "\n\u2022 **color:** Generates a random color.\n";

	EmbedBuilder embed = new EmbedBuilder().setColor(0x419399).setAuthor("Power Glove Help", null, "https://cdn.discordapp.com/avatars/439435998078959616/94941ff09437eef86861c579e8b5a6fb.png").addField("Here's a list of all the commands:",
			"\u2022 **help:** Displays this list, helpful isn't it?"
			+ "\n\u2022 **settings:** Change the settings for this server."
			+ "\n\u2022 **science:** Posts a science related picture."
			+ "\n\u2022 **type:** Makes the bot start typing."
			+ "\n\u2022 **info:** Gives some info about this bot."
			+ "\n\u2022 **mlg [user]:** Make someone (or yourself) mlg."
			+ "\n\u2022 **smiles [smiles]:** Give it a SMILES formula and it will draw the molecule for you (reaction smiles are also supported). [Wikipedia: SMILES](https://en.wikipedia.org/wiki/Simplified_molecular-input_line-entry_system)"
			+ "\nExample: pow smiles CN1C=NC2=C1C(=O)N(C(=O)N2C)C", false).addField("",
			"\u2022 **disgusting [text]:** Insert your own text into the absolutely disgusting meme."
			+ "\n\u2022 **dong [text]:** Convert text into expand dong text."
			+ "\n\u2022 **talk:** Make the bot start or stop talking in the current channel."
			+ "\n\u2022 **afk [optional reason]:** Set yourself away from keyboard (globally)."
			+ "\n\u2022 **mastermind:** Play a nice game of mastermind against the bot."
			+ "\n\u2022 **color:** Generates a random color."
			+ "\n\u2022 **cancel [user]:** Cancel or uncancel someone.\n", false);

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event, Setting settings) {
		EmbedBuilder newEmbed = new EmbedBuilder(embed);

		if (settings.prefix.equals("pow "))
			newEmbed.appendDescription(description);
		else
			newEmbed.appendDescription("Prefix for this server = \"" + settings.prefix + "\"\n" + description);
		try {
			event.getChannel().sendMessageEmbeds(newEmbed.setTitle(powerQuote[Math.max(rand.nextInt(powerQuote.length + 10) - 10, 0)]).build()).queue();
		} catch (InsufficientPermissionException e) {
			event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions.\n" + "Power Glove Help\n" + new String(newEmbed.getDescriptionBuilder()) + lameversion).queue();
		}
	}
}
