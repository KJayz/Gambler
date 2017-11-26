package lu.uni.distributedsystems.project.gambler;

public class AvailableMatch {

	private String bookieID;
	private int matchID;
	private String teamA;
	private float oddsA;
	private String teamB;
	private float oddsB;
	private int limit;
	
	public AvailableMatch(String bookieID, int matchID, String teamA,
			int oddsA, String teamB, int oddsB, int limit) {
		
		this.setBookieID(bookieID);
		this.setMatchID(matchID);
		this.setTeamA(teamA);
		this.setOddsA(oddsA);
		this.setTeamB(teamB);
		this.setOddsB(oddsB);
		this.setLimit(limit);		
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

	public String getTeamA() {
		return teamA;
	}

	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}

	public float getOddsB() {
		return oddsB;
	}

	public void setOddsB(float oddsB) {
		this.oddsB = oddsB;
	}

	public float getOddsA() {
		return oddsA;
	}

	public void setOddsA(float oddsA) {
		this.oddsA = oddsA;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}	
	
}
