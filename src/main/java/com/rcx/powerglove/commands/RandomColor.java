package com.rcx.powerglove.commands;

import java.awt.Color;
import java.util.Random;

import javax.vecmath.Point3d;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class RandomColor extends Command {

	NamedColor[] colorNames = {
			new NamedColor(255, 0, 0, "Red"),
			new NamedColor(0, 255, 0, "Green"),
			new NamedColor(0, 0, 255, "Blue")
	};

	NamedColor defaultColorName = new NamedColor(256, 256, 256, "error");

	Random rand = new Random();

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		String hex = Integer.toHexString(color.getRGB()).substring(2);

		MessageEmbed returns = new EmbedBuilder().setColor(color.getRGB()).addField("Random color",
				"Name: " + getClosestColorName(color)
				+ "\nHex: #" + hex
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

	String getClosestColorName(Color color) {
		Point3d colorPoint = new Point3d(color.getRed(), color.getGreen(), color.getBlue());
		NamedColor closestColorName = defaultColorName;
		Double shortestDistance = 1000D;

		for (NamedColor colorName : colorNames) {
			Double distance = colorPoint.distance(colorName.location);
			if (distance < shortestDistance) {
				shortestDistance = distance;
				closestColorName = colorName;
			}
		}
		return closestColorName.colorName;
	}

	public class NamedColor {

		Point3d location;
		String colorName;

		NamedColor(int r, int g, int b, String name){
			location = new Point3d(r, g, b);
			colorName = name;
		}
	}
}
