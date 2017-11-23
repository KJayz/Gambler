package lu.uni.distributedsystems.project.common.command;

/**
 * Implementation of the help command.
 * 
 * @author steffen
 *
 */
public class HelpCommand extends Command {

	public HelpCommand(CommandProcessor commandProcessor) {
		super(commandProcessor, "help");
	}

	@Override
	public void process(String[] args) {
		if (args.length == 1) {
			// show help for a specific command
			getCommandProcessor().showHelp(args[0]);
		}
		else {
			// show help for all commands
			getCommandProcessor().showHelp();
		}
	}

	@Override
	public void showHelp() {
		System.out.println("help : show help for all available commands");
		System.out.println("help [command-name] : show help for a certain command");
	}

}
