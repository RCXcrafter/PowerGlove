package com.rcx.powerglove.commands;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AbsolutelyDisgusting extends Command {

	BufferedImage background;
	BufferedImage font;

	public AbsolutelyDisgusting() {
		try {
			font = ImageIO.read(getClass().getClassLoader().getResource("assets/absolutely disgusting/font.png"));
			background = ImageIO.read(getClass().getClassLoader().getResource("assets/absolutely disgusting/absolutely empty template.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		File picture = null;
		try {
			picture = File.createTempFile("disgusting", ".png");
			if (arguments.length < 2) {
				ImageIO.write(background, "PNG", picture);
				event.getChannel().sendFile(picture).queue();
				picture.delete();
				return;
			}

			String textString = arguments[1];
			for (int i = 2; i < arguments.length; i++)
				textString += " " + arguments[i];

			BufferedImage combined = new BufferedImage(600, 450, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			List<BufferedImage> text = convert(textString);
			int x = 134;

			g.drawImage(background, 0, 0, null);
			for (BufferedImage img : text) {
				g.drawImage(img, x, 369, null);
				x += img.getWidth();
			}
			ImageIO.write(combined, "PNG", picture);
			event.getChannel().sendFile(picture).queue();
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		picture.delete();
	}

	public List<BufferedImage> convert(String text) {

		List<BufferedImage> images = new ArrayList<>(25);

		for (char c : text.toCharArray()) {
			c = Character.toUpperCase(c);
			int smudge = 3;
			int offset = -1;
			int width = 24;
			if (c >= 48 && c <= 57) {
				offset = c - 48;
			} else if (c >= 65 && c <= 90) {
				offset = c - 65 + 10;
				if (c == 73)
					smudge *= 5;
			} else if (c == 32) {
				offset = 48;
				smudge *= 2;
			}

			if (offset >= 0) {
				BufferedImage sub = font.getSubimage((offset * width) + smudge, 0, width - smudge, width);
				images.add(sub);
			}
		}
		return images;
	}
}
