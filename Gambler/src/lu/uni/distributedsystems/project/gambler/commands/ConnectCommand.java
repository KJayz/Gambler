package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the command connect [bookie-ip] [bookie-port]
 */
public class ConnectCommand extends Command {
	
	private Gambler gambler;

	public ConnectCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "connect");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		// extract arguments
		String bookieIP = args[0];
		int bookiePort = Integer.parseInt(args[1]);
		
		gambler.createNewBookieConnection(bookieIP, bookiePort);
	}

	@Override
	public void showHelp() {
		System.out.println("connect [bookie-ip] [bookie-port] : connect to bookie at the given address");
	}

}
