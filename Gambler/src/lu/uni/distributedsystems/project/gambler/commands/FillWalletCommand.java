package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the command fill_wallet [amount]
 */
public class FillWalletCommand extends Command {
	
	private Gambler gambler;

	public FillWalletCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "fill_wallet");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		// extract arguments
		int amount = Integer.parseInt(args[0]);
		
		gambler.fillWallet(amount);
	}

	@Override
	public void showHelp() {
		System.out.println("fill_wallet [amount] : adds the given amount of money to the wallet");
	}

}
