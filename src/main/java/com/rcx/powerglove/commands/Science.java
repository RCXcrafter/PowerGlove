package com.rcx.powerglove.commands;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class Science extends Command {

	String[] sciencepics = {
			"https://i.imgur.com/s3IMBBE.png",
			"https://i.imgur.com/jjJbxT8.jpg",
			"https://i.imgur.com/g5ibSVk.gif",
			"https://i.imgur.com/RwDmYsQ.gif",
			"https://i.imgur.com/KGDjJwv.gif",
			"https://i.imgur.com/PSo8jzy.png",
			"https://i.imgur.com/e9YbCR0.jpg",
			"https://i.imgur.com/Sfze3j7.gif",
			"https://i.imgur.com/WUthQ6W.png",
			"https://i.imgur.com/8xbxH23.gif",
			"https://i.imgur.com/fMC7bZC.gif",
			"https://i.imgur.com/3Qmlfc3.gif",
			"https://i.imgur.com/BScDG5k.png",
			"https://i.imgur.com/8Gyek72.jpg",
			"https://i.imgur.com/N0h9Bfq.png",
			"https://i.imgur.com/nSCBvR4.png",
			"https://i.imgur.com/JZz0iad.gif",
			"https://i.imgur.com/MsB2TbD.png",
			"https://i.imgur.com/qjoMKCI.gif",
			"https://i.imgur.com/FSOFlJO.png",
			"https://i.imgur.com/iwz3k5k.gif",
			"https://i.imgur.com/TpKpEH2.png",
			"https://i.imgur.com/dDkZ3mm.gif",
			"https://i.imgur.com/1ylGagx.gif",
			"https://i.imgur.com/zkeogtl.gif",
			"https://i.imgur.com/myAHCED.jpg",
			"https://i.imgur.com/k96pXMk.png",
			"https://i.imgur.com/PQKY3R6.gif",
			"https://i.imgur.com/jdGNA94.jpg",
			"https://i.imgur.com/bKa0uhB.png",
			"https://i.imgur.com/UPb7P65.png",
			"https://i.imgur.com/1ErpbUi.gif",
			"https://i.imgur.com/a7qVlDk.gif",
			"https://i.imgur.com/vvDjWec.gif",
			"https://i.imgur.com/O7mG5Nj.jpg",
			"https://i.imgur.com/hH7E9IV.png",
			"https://i.imgur.com/6hRe0Do.gif",
			"https://i.imgur.com/AtyCQfl.jpg",
			"https://i.imgur.com/9b3zezF.gif",
			"https://i.imgur.com/XLHkWAz.gif",
			"https://i.imgur.com/oSwnKyH.gif",
			"https://i.imgur.com/Ew4kdPa.gif",
			"https://i.imgur.com/O6KigaR.gif",
			"https://i.imgur.com/Nipt0Nr.jpg",
			"https://i.imgur.com/16onlMx.jpg",
			"https://i.imgur.com/WzPcD1S.png",
			"https://i.imgur.com/wCtlPJi.jpg",
			"https://i.imgur.com/hS3r16L.jpg",
			"https://i.imgur.com/q0X4JTW.png"
	};

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessage(new EmbedBuilder().setImage(sciencepics[new Random().nextInt(sciencepics.length)]).setColor(0x419399).build()).queue();
		} catch (InsufficientPermissionException e) {
			event.getChannel().sendMessage(sciencepics[new Random().nextInt(sciencepics.length)]).queue();
		}
	}
}
