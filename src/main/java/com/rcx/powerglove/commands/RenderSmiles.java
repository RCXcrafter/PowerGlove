package com.rcx.powerglove.commands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RenderSmiles extends Command {

	SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
	DepictionGenerator dptgen = new DepictionGenerator().withZoom(1.7).withAtomColors().withCarbonSymbols().withMolTitle().withTitleColor(Color.BLACK);

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (arguments.length < 2) {
			event.getChannel().sendMessage("\u26A0 Add a SMILES formula for this to work.").queue();
			return;
		}

		File picture = null;
		try {
			try {
				picture = File.createTempFile("molecule", ".png");
				IAtomContainer mol = sp.parseSmiles(arguments[1]);
				try {
					String inchi = InChIGeneratorFactory.getInstance().getInChIGenerator(mol).getInchi();
					if (inchi != null) {
						if (inchi.indexOf("/") + 1 > 1)
							inchi = inchi.substring(inchi.indexOf("/") + 1);
						if (inchi.indexOf("/") != -1)
							inchi = inchi.substring(0, inchi.indexOf("/"));
						mol.setTitle(subscriptNumbers(inchi));
					}
					dptgen.depict(mol).writeTo("png", picture);
					event.getChannel().sendFile(picture).queue();
				} catch (CDKException | IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InvalidSmilesException e) {
			event.getChannel().sendMessage("\u26A0 Invalid SMILES syntax :(\n```" + e.getMessage() + "```").queue();
		}
		picture.delete();
	}

	public static String subscriptNumbers(String str) {
		str = str.replaceAll("0", "\u2080");
		str = str.replaceAll("1", "\u2081");
		str = str.replaceAll("2", "\u2082");
		str = str.replaceAll("3", "\u2083");
		str = str.replaceAll("4", "\u2084");
		str = str.replaceAll("5", "\u2085");
		str = str.replaceAll("6", "\u2086");
		str = str.replaceAll("7", "\u2087");
		str = str.replaceAll("8", "\u2088");
		str = str.replaceAll("9", "\u2089");
		return str;
	}
}
