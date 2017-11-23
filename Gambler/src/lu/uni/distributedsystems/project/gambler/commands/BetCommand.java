package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the command bet [bookie-id] [match-id] [team] [stake] [odds]
 */
public class BetCommand extends Command {
	
	private Gambler gambler;

	public BetCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "bet");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		// extract arguments
		String bookieID = args[0];
		int matchID = Integer.parseInt(args[1]);
		String team = args[2];
		int stake = Integer.parseInt(args[3]);
		float odds = Float.parseFloat(args[4]);
		
		gambler.bet(bookieID, matchID, team, stake, odds);
	}

	@Override
	public void showHelp() {
		System.out.println("bet [bookie-id] [match-id] [team] [stake] [odds]");
		System.out.println("    place a bet with some bookie");
	}

}
