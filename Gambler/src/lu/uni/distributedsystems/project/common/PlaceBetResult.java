package lu.uni.distributedsystems.project.common;

/**
 * Enumeration of possibilities when a gambler attempts to place a bet.
 * 
 * @author steffen
 *
 */
public enum PlaceBetResult {
	
	ACCEPTED,
	REJECTED_UNKNOWN_MATCH,
	REJECTED_UNKNOWN_TEAM,
	REJECTED_ALREADY_PLACED_BET,
	REJECTED_LIMIT_EXCEEDED,
	REJECTED_ODDS_MISMATCH

}
