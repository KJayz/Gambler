package lu.uni.distributedsystems.project.gambler.commands;

import lu.uni.distributedsystems.project.common.command.Command;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;
import lu.uni.distributedsystems.project.gambler.Gambler;

/**
 * Implementation of the command say_hello [bookie-id]
 */
public class SayHelloCommand extends Command {
	
	private Gambler gambler;

	public SayHelloCommand(CommandProcessor commandProcessor, Gambler gambler) {
		super(commandProcessor, "say_hello");
		this.gambler = gambler;
	}

	@Override
	public void process(String[] args) {
		// extract arguments
		String bookieID = args[0];
		
		gambler.sayHelloToBookie(bookieID);
	}

	@Override
	public void showHelp() {
		System.out.println("say_hello [bookie-id] : send sayHello request to specified bookie");
	}


}
