package com.rcx.powerglove.commands;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class MakeMLG extends Command {

	BufferedImage rainbow;
	BufferedImage guns;
	BufferedImage hitmarker;
	BufferedImage shades;
	BufferedImage dew;
	BufferedImage doritos;
	BufferedImage explosion;
	BufferedImage faze;
	BufferedImage frog;
	BufferedImage illuminati;
	BufferedImage lenny;
	BufferedImage mlg;
	BufferedImage sample;
	BufferedImage sanic;
	BufferedImage weed;
	Random rand = new Random();

	public MakeMLG () {
		try {
			rainbow = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/rainbow.png"));
			guns = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/guns.png"));
			hitmarker = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/hitmarker.png"));
			shades = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/shades.png"));
			dew = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/dew.png"));
			doritos = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/doritos.png"));
			explosion = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/explosion.png"));
			faze = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/faze.png"));
			frog = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/frog.png"));
			illuminati = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/illuminati.png"));
			lenny = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/lenny.png"));
			mlg = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/mlg.png"));
			sample = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/sample.png"));
			sanic = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/sanic.png"));
			weed = ImageIO.read(getClass().getClassLoader().getResource("assets/mlg overlays/weed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		User thisGuy = event.getAuthor();

		if (arguments.length > 1) {
			try {
				thisGuy = event.getJDA().getUserById(arguments[1]);
			} catch (NumberFormatException e) {}

			if (!event.getMessage().getMentionedUsers().isEmpty())
				thisGuy = event.getMessage().getMentionedUsers().get(0);
		}

		File avatar = null;
		String url = thisGuy.getEffectiveAvatarUrl() + "?size=1024";

		try {
			avatar = File.createTempFile("avatar", url.substring(url.lastIndexOf("."), url.lastIndexOf("?")));

			HttpURLConnection site = (HttpURLConnection) new URL(url).openConnection();
			site.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			site.setDoInput(true);
			site.connect();
			InputStream in = new BufferedInputStream(site.getInputStream());
			FileUtils.copyInputStreamToFile(in, avatar);

			BufferedImage vatar = ImageIO.read(avatar);
			BufferedImage combined = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			g.drawImage(rainbow, 0, 0, null);
			g.drawImage(vatar.getScaledInstance(600, 600, 0), 0, 0, null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(hitmarker, rand.nextInt(600-hitmarker.getWidth()), rand.nextInt(600-hitmarker.getHeight()), null);
			g.drawImage(guns, 0, 0, null);
			addRotOverlay(g, dew, rand.nextInt(600-dew.getWidth()), rand.nextInt(600-dew.getHeight()), rand.nextInt(360));
			addRotOverlay(g, doritos, rand.nextInt(600-doritos.getWidth()), rand.nextInt(600-doritos.getHeight()), rand.nextInt(360));
			addRotOverlay(g, explosion, rand.nextInt(600-explosion.getWidth()), rand.nextInt(600-explosion.getHeight()), rand.nextInt(360));
			addRotOverlay(g, faze, rand.nextInt(600-faze.getWidth()), rand.nextInt(600-faze.getHeight()), rand.nextInt(360));
			addRotOverlay(g, frog, rand.nextInt(600-frog.getWidth()), rand.nextInt(600-frog.getHeight()), rand.nextInt(360));
			addRotOverlay(g, illuminati, rand.nextInt(600-illuminati.getWidth()), rand.nextInt(600-illuminati.getHeight()), rand.nextInt(360));
			addRotOverlay(g, lenny, rand.nextInt(600-lenny.getWidth()), rand.nextInt(600-lenny.getHeight()), rand.nextInt(360));
			addRotOverlay(g, mlg, rand.nextInt(600-mlg.getWidth()), rand.nextInt(600-mlg.getHeight()), rand.nextInt(360));
			addRotOverlay(g, sample, rand.nextInt(600-sample.getWidth()), rand.nextInt(600-sample.getHeight()), rand.nextInt(360));
			addRotOverlay(g, sanic, rand.nextInt(600-sanic.getWidth()), rand.nextInt(600-sanic.getHeight()), rand.nextInt(360));
			addRotOverlay(g, weed, rand.nextInt(600-weed.getWidth()), rand.nextInt(600-weed.getHeight()), rand.nextInt(360));
			addRotOverlay(g, shades, 109, 64, rand.nextInt(70)-35);

			ImageIO.write(combined, "PNG", avatar);
			event.getChannel().sendFile(avatar).queue();
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InsufficientPermissionException e) {
			event.getChannel().sendMessage("\u26A0 This command normally results in an image, but I lack the permission to *Attach Files*").queue();
		}
		avatar.delete();
	}

	void addRotOverlay (Graphics g, BufferedImage img, int x, int y, int rot) {
		double rotationRequired = Math.toRadians (rot);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		g.drawImage(op.filter(img, null), x, y, null);
	}
}
