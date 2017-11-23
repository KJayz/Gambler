package lu.uni.distributedsystems.project.common.command;

/**
 * Base class for a command that can be invoked via the console.
 * 
 * @author steffen
 *
 */
public abstract class Command {
	
	private CommandProcessor commandProcessor;
	private String commandName;
	
	/**
	 * Constructs a new command and registers it with a CommandProcessor.
	 * 
	 * @param commandProcessor	the CommandProcessor to register the Command with
	 * @param commandName		name used to invoke the Command
	 */
	public Command(CommandProcessor commandProcessor, String commandName) {
		this.commandProcessor = commandProcessor;
		this.commandName = commandName;
		commandProcessor.register(commandName, this);
	}
	
	/**
	 * Returns the associated CommandProcessor.
	 * 
	 * @return The associated CommandProcessor.
	 */
	protected CommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
	
	/**
	 * Returns the name of the command that is used to invoke it.
	 * 
	 * @return Name of this command.
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * Handler method to process this Command.
	 * 
	 * @param args	Arguments given on the command line.
	 */
	public abstract void process(String[] args);
	
	/**
	 * Print a short message, typically a single line, giving
	 * some brief help for this Command. 
	 */
	public abstract void showHelp();

}
