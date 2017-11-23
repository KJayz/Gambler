package lu.uni.distributedsystems.project.gambler;

public class Bet {
	
	/**
	 * Stores a bet succesfully placed with a bookie.
	 */
	private String bookieID;
	private int matchID;
	private String team;
	private int stake;
	private float odds;
		
		public Bet(String bookieID, int matchID, String team, int stake, float odds) {
			this.setBookieID(bookieID);
			this.setMatchID(matchID);
			this.setTeam(team);
			this.setStake(stake);
			this.setOdds(odds);
		}

		public String getBookieID() {
			return bookieID;
		}

		public void setBookieID(String bookieID) {
			this.bookieID = bookieID;
		}

		public int getMatchID() {
			return matchID;
		}

		public void setMatchID(int matchID) {
			this.matchID = matchID;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public int getStake() {
			return stake;
		}

		public void setStake(int stake) {
			this.stake = stake;
		}

		public float getOdds() {
			return odds;
		}

		public void setOdds(float odds) {
			this.odds = odds;
		}
}
