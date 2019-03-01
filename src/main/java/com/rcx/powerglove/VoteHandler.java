package com.rcx.powerglove;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class VoteHandler {
	public static Map<String, userVotes> voters = new HashMap<String, userVotes>();
	
	public static void recieveVote(String content, String sender, String authorization, String ip) {
		String user = null;
		userVotes voter;

		if (sender.equals("botlist.space Webhooks (https://botlist.space)") && authorization.equals(PowerGlove.botListToken)) {
			user = content.substring(content.indexOf("{\"id\":\"") + 7, content.substring(content.indexOf("{\"id\":\"") + 7).indexOf("\""));
		} else if (ip.equals("144.172.70.177") && authorization.equals(PowerGlove.bots4DToken)) {
			user = content.substring(9, content.substring(9).indexOf("\""));
		} else if (sender.equals("DBL2") && authorization.equals(PowerGlove.dbl2Token)) {
			user = content.substring(3, content.substring(3).indexOf("&"));
		} else if (ip.equals("139.99.56.126") && authorization.equals(PowerGlove.dBotsGroupToken)) {
			user = content.substring(7, content.substring(7).indexOf("\""));
		}

		System.out.println("a vote has been recieved");
		System.out.println("user:" + user);
		System.out.println("content: " + content);
		System.out.println("auth: " + authorization);
		System.out.println("sender: " + sender);
		System.out.println("ip: " + ip);
		
		if (user == null)
			return;

		if (voters.containsKey(user)) {
			voter = voters.get(user);
		} else {
			voter = new userVotes(user);
			voters.put(user, voter);
		}
		if (sender.equals("botlist.space Webhooks (https://botlist.space)")) {
			voter.voteSpace();
		//} else if (sender.equals("DBL") && authorization.equals(PowerGlove.dblToken)) {
			//voter.voteDBL();
		} else if (ip.equals("144.172.70.177")) {
			voter.voteBFD();
		//} else if (ip.equals("104.248.37.42") && authorization.equals(PowerGlove.dBoatsToken)) {
			//voter.voteBoats();
		//} else if (ip.equals("ns104109.ip-147-135-8.us") && authorization.equals(PowerGlove.boatsClubToken)) {
			//voter.voteBoatsClub();
		//} else if (sender.equals("") && authorization.equals(PowerGlove.Token)) { //I'll have to do something special for this one
			//voter.voteIndex();
		} else if (sender.equals("DBL2")) {
			voter.voteDBL2();
		//} else if (sender.equals("") && authorization.equals(PowerGlove.Token)) {
			//voter.voteReviews();
		//} else if (sender.equals("") && authorization.equals(PowerGlove.Token)) {
			//voter.voteWorld();
		} else if (ip.equals("139.99.56.126")) {
			voter.voteGroup();
		//} else if (sender.equals("") && authorization.equals(PowerGlove.Token)) {
			//voter.voteDivine();
		}
		voter.cleanUp();
	}
	
	public static class userVotes {
		public String id;
		public boolean spaceVote = false;
		ScheduledFuture<?> spaceTimer;
		//boolean DBLVote = false;
		//ScheduledFuture<?> DBLTimer;
		public boolean BFDVote = false;
		ScheduledFuture<?> BFDTimer;
		//boolean boatsVote = false;
		//ScheduledFuture<?> boatsTimer;
		//boolean boatsClubVote = false;
		//ScheduledFuture<?> boatsClubTimer;
		//public boolean indexVote = false;
		//ScheduledFuture<?> indexTimer;
		public boolean DBL2Vote = false;
		ScheduledFuture<?> DBL2Timer;
		//boolean reviewsVote = false;
		//ScheduledFuture<?> reviewsTimer;
		//boolean worldVote = false;
		//ScheduledFuture<?> worldTimer;
		public boolean groupVote = false;
		ScheduledFuture<?> groupTimer;
		//boolean divineVote = false;
		//ScheduledFuture<?> divineTimer;

		public userVotes(String id) {
			this.id = id;
		}
		
		public void cleanUp() {
			if (!(spaceVote || /*DBLVote ||*/ BFDVote || /*boatsVote || boatsClubVote || indexVote ||*/ DBL2Vote || /*reviewsVote || worldVote ||*/ groupVote /*|| divineVote*/))
				voters.remove(id);
		}
		
		public void voteSpace() {
			spaceVote = true;
			spaceTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					spaceVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		/*public void voteDBL() {
			DBLVote = true;
			DBLTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					DBLVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}*/
		
		public void voteBFD() {
			BFDVote = true;
			BFDTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					BFDVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		/*public void voteBoats() {
			boatsVote = true;
			boatsTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					boatsVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		public void voteBoatsClub() {
			boatsClubVote = true;
			boatsClubTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					boatsClubVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		public void voteIndex() {
			indexVote = true;
			indexTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					indexVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}*/
		
		public void voteDBL2() {
			DBL2Vote = true;
			DBL2Timer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					DBL2Vote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		/*public void voteReviews() {
			reviewsVote = true;
			reviewsTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					reviewsVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		public void voteWorld() {
			worldVote = true;
			worldTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					worldVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}*/
		
		public void voteGroup() {
			groupVote = true;
			groupTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					groupVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}
		
		/*public void voteDivine() {
			divineVote = true;
			divineTimer = Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				public void run() {
					divineVote = false;
				}
			}, 1, TimeUnit.DAYS);
			cleanUp();
		}*/
	}
}
