package lu.uni.distributedsystems.project.common.command;

/**
 * Implementation of the exit command.
 * 
 * @author steffen
 *
 */
public class ExitCommand extends Command {

	public ExitCommand(CommandProcessor commandProcessor) {
		super(commandProcessor, "exit");
	}

	@Override
	public void process(String[] args) {
		System.out.println("Exiting ...");
		// tell associated CommandProcessor to exit
		getCommandProcessor().exit();
	}

	@Override
	public void showHelp() {
		System.out.println("exit : terminate the application");
	}

}
