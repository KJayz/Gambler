package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the show_bets command.
 */
public class ShowBetsCommand extends Command {
	
	private Gambler gambler;

	public ShowBetsCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "show_bets");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		gambler.showBets();
	}

	@Override
	public void showHelp() {
		System.out.println("show_bets : show a list of all open bets");
	}

}
