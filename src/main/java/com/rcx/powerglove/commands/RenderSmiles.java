package com.rcx.powerglove.commands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.utils.FileUpload;

public class RenderSmiles implements Command {

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
				String smiles = arguments[1];
				for (int i = 2; i < arguments.length; i++)
					smiles += " " + arguments[i];

				boolean reaction = smiles.contains(">");
				IAtomContainer mol = null;
				IReaction rec = null;
				if (reaction) {
					rec = sp.parseReactionSmiles(smiles);
				} else {
					mol = sp.parseSmiles(smiles);
				}
				try {
					String inchi = null;
					if (!reaction)
						inchi = InChIGeneratorFactory.getInstance().getInChIGenerator(mol).getInchi();
					if (inchi != null) {
						if (inchi.indexOf("/") + 1 > 1)
							inchi = inchi.substring(inchi.indexOf("/") + 1);
						if (inchi.indexOf("/") != -1)
							inchi = inchi.substring(0, inchi.indexOf("/"));
						mol.setTitle(subscriptNumbers(inchi));
					}

					if (reaction)
						dptgen.depict(rec).writeTo("png", picture);
					else
						dptgen.depict(mol).writeTo("png", picture);
					event.getChannel().sendFiles(FileUpload.fromData(picture)).queue();
				} catch (CDKException | IOException e) {
					e.printStackTrace();
				} catch (InsufficientPermissionException e) {
					event.getChannel().sendMessage("\u26A0 This command normally results in an image, but I lack the permission to *Attach Files*").queue();
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
