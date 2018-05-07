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
			event.getChannel().sendMessage("Add a SMILES formula for this to work.").queue();
			return;
		}

		try {
			try {
				File picture = File.createTempFile("molecule", ".png");
				IAtomContainer mol = sp.parseSmiles(arguments[1]);
				try {
					String inchi = InChIGeneratorFactory.getInstance().getInChIGenerator(mol).getInchi();
					inchi = inchi.substring(inchi.indexOf("/") + 1);
					if (inchi.indexOf("/") != -1)
						inchi = inchi.substring(0, inchi.indexOf("/"));
					mol.setTitle(subscriptNumbers(inchi));
					dptgen.depict(mol).writeTo("png", picture);
					event.getChannel().sendFile(picture).queue();
					picture.delete();
				} catch (CDKException | IOException e) {
					e.printStackTrace();
					picture.delete();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InvalidSmilesException e) {
			event.getChannel().sendMessage("Invalid SMILES syntax :(\n```" + e.getMessage() + "```").queue();
		}
	}

	public static String subscriptNumbers(String str) {
		str = str.replaceAll("0", "₀");
		str = str.replaceAll("1", "₁");
		str = str.replaceAll("2", "₂");
		str = str.replaceAll("3", "₃");
		str = str.replaceAll("4", "₄");
		str = str.replaceAll("5", "₅");
		str = str.replaceAll("6", "₆");
		str = str.replaceAll("7", "₇");
		str = str.replaceAll("8", "₈");
		str = str.replaceAll("9", "₉");
		return str;
	}
}
