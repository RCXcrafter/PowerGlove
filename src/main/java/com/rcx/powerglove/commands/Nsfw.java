package com.rcx.powerglove.commands;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rcx.powerglove.PowerGlove;
import com.rcx.powerglove.VoteHandler;
import com.rcx.powerglove.VoteHandler.userVotes;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Nsfw extends Command {

	Random rand = new Random();

	public static String[] message = {
			"No, just no.",
			"Get out of here, right now.",
			"*[Dissapointment intensifies]*",
			"Fricken horny teenagers.",
			"Ker-prank'd",
			"Stop being such a sex addict and play some video games.",
			"Hahaha, no.",
			"This is why we can't have nice things.",
			"You lost 23 power tokens and my respect for using that command.",
			"Jimmies.setRustled(true);",
			"Thank you for participating in this experiment, you have been globally blacklisted from life. Have a nice day.",
			"Pathetic.",
			"Loser, you're a loser, are you feeling sorry for yourself? Well you should be cause you are dirt. You make me sick you big baby. Baby want a bottle? A big dirt bottle?",
			"I don't want to live on this planet anymore.",
			"Nothing personal but, I hate you.",
			"I'm done.",
			"Never gonna give you up, never gonna let you down.",
			"Writing the code to process all this voting data was really annoying so I thought I'd give you a taste of it.", 
			"Congratulations with wasting your time!"
	};

	@Override
	public void execute(String[] arguments, MessageReceivedEvent event) {
		if (!event.getTextChannel().isNSFW()) {
			event.getChannel().sendMessage("This command can only be used in channels marked as NSFW.").queue();
			return;
		}
		String user = event.getAuthor().getId();
		userVotes voter = null;
		if (VoteHandler.voters.containsKey(user)) {
			voter = VoteHandler.voters.get(user);
		}
		if (voter == null) {
			event.getChannel().sendMessage("Please vote for Power Glove on botlist.space to get access to this command\n<https://botlist.space/bot/439435998078959616/upvote>").queue();
			return;
		}
		boolean spaceVote = voter.spaceVote;
		boolean BFDVote = voter.BFDVote;
		boolean boatsVote = getBoatsVote(user);
		boolean boatsClubVote = getBoatsClubVote(user);
		boolean DBL2Vote = voter.DBL2Vote;
		boolean reviewsVote = getReviewsVote(user);
		boolean worldVote = getWorldVote(user);
		boolean groupVote = voter.groupVote;
		boolean divineVote = getDivineVote(user);
		boolean DBLVote = getDBotsVote(user);

		if (spaceVote) {
			if (BFDVote) {
				if (boatsVote) {
					if (boatsClubVote) {
						if (DBL2Vote) {
							if (reviewsVote) {
								if (worldVote) {
									if (groupVote) {
										if (divineVote) {
											if (DBLVote) {
												event.getChannel().sendMessage(message[rand.nextInt(message.length)]).queue();
											} else {
												event.getChannel().sendMessage("Please also vote for Power Glove on discordbots.org to get access to this command\n<https://discordbots.org/bot/439435998078959616/vote>").queue();
											}
										} else {
											event.getChannel().sendMessage("Please also vote for Power Glove on divinediscordbots.com to get access to this command\n<https://divinediscordbots.com/bots/439435998078959616/vote>").queue();
										}
									} else {
										event.getChannel().sendMessage("Please also upvote Power Glove on discordbots.group to get access to this command\n<https://discordbots.group/bot/439435998078959616>").queue();
									}
								} else {
									event.getChannel().sendMessage("Please also like Power Glove on discordbot.world to get access to this command\n<https://discordbot.world/bot/439435998078959616>").queue();
								}
							} else {
								event.getChannel().sendMessage("Please also like Power Glove on discordbotreviews.xyz to get access to this command\n<https://discordbotreviews.xyz/bot/439435998078959616>").queue();
							}
						} else {
							event.getChannel().sendMessage("Please also upvote Power Glove on discordbotlist.com to get access to this command\n<https://discordbotlist.com/bots/439435998078959616/upvote>").queue();
						}
					} else {
						event.getChannel().sendMessage("Please also like Power Glove on discordboats.club to get access to this command\n<https://discordboats.club/bot/439435998078959616>").queue();
					}
				} else {
					event.getChannel().sendMessage("Please also vote for Power Glove on discord.boats to get access to this command\n<https://discord.boats/bot/439435998078959616/vote>").queue();
				}
			} else {
				event.getChannel().sendMessage("Please also vote for Power Glove on botsfordiscord.com to get access to this command\n<https://botsfordiscord.com/bot/439435998078959616/vote>").queue();
			}
		} else {
			event.getChannel().sendMessage("Please upvote Power Glove on botlist.space to get access to this command\n<https://botlist.space/bot/439435998078959616/upvote>").queue();
		}
	}

	public boolean getBoatsVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://discord.boats/api/bot/439435998078959616/voted?id=" + id).openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();
			return (Boolean) ((JSONObject) new JSONParser().parse(lines)).get("voted");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean getBoatsClubVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://discordboats.club/api/public/bot/me/liked/" + id).openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", "1aSgmSs0IFZMoHHVPglbg1SZFtSBxM");//PowerGlove.boatsClubToken);
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();

			System.out.println("getting boatsclub vote");
			System.out.println(lines);
			System.out.println((Boolean) ((JSONObject) new JSONParser().parse(lines)).get("data"));
			
			return (Boolean) ((JSONObject) new JSONParser().parse(lines)).get("data");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getReviewsVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://discordbotreviews.xyz/api/bot/439435998078959616/rating").openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", PowerGlove.botsReviewToken);
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();
			return ((JSONArray) ((JSONObject) new JSONParser().parse(lines)).get("likers")).contains(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getWorldVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://discordbot.world/api/bot/439435998078959616/likes").openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", PowerGlove.botWorldToken);
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();
			for (Object user : ((JSONArray) new JSONParser().parse(lines)).toArray()) {
				if (((String) ((JSONObject) user).get("id")).equals(id))
					return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getDivineVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://divinediscordbots.com/bots/439435998078959616/votes").openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", PowerGlove.ddBotsToken);
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();
			for (Object user : ((JSONArray) ((JSONObject) new JSONParser().parse(lines)).get("votes")).toArray()) {
				if (((String) ((JSONObject) user).get("id")).equals(id))
					return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getDBotsVote(String id) {
		try {
			HttpURLConnection site = (HttpURLConnection) new URL("https://discordbots.org/api/bots/439435998078959616/check?userId=" + id).openConnection();
			site.setRequestProperty("Content-Type", "application/json");
			site.setRequestProperty("Authorization", PowerGlove.dblToken);
			site.setRequestMethod("GET");
			site.setDoInput(true);
			site.connect();

			InputStream rad = site.getInputStream();
			DataInputStream rd = new DataInputStream (rad);
			BufferedReader d = new BufferedReader(new InputStreamReader(rd));
			String line;
			String lines = "";
			while ((line = d.readLine()) != null) {
				lines += line;
			}
			d.close();
			rd.close();
			site.disconnect();
			return  (Long) ((JSONObject) new JSONParser().parse(lines)).get("voted") == 1L;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
