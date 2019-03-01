package com.rcx.powerglove.commands;

import java.awt.Color;
import java.util.Random;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class RandomColor extends Command {
	
	Random rand = new Random();

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		String hex = Integer.toHexString(color.getRGB()).substring(2);

		MessageEmbed returns = new EmbedBuilder().setColor(color.getRGB()).addField("Random color",
		"Hex: #" + hex
		+ "\nRed: " + color.getRed()
		+ "\nGreen: " + color.getGreen()
		+ "\nBlue: " + color.getBlue(), true).setThumbnail("https://www.colorhexa.com/" + hex +".png").build();

		try {
			event.getChannel().sendMessage(returns).queue();
		} catch (InsufficientPermissionException e) {
			event.getChannel().sendMessage("This looks terrible because the bot doesn't have embed permissions."
					+ "\nRandom color"
					+ "\nHex: #" + hex
					+ "\nRed: " + color.getRed()
					+ "\nGreen: " + color.getGreen()
					+ "\nBlue: " + color.getBlue()).queue();
		}
	}
}
