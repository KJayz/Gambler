package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the show_matches command.
 */
public class ShowMatchesCommand extends Command {
	
	private Gambler gambler;

	public ShowMatchesCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "show_matches");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		gambler.showMatches();
	}

	@Override
	public void showHelp() {
		System.out.println("show_matches : show a list of all open matches");
		System.out.println("    of all the connected bookies, since having connected");
	}

}
