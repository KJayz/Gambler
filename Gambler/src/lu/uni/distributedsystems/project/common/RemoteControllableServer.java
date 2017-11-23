package lu.uni.distributedsystems.project.common;

import com.google.code.gsonrmi.annotations.RMI;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;

/**
 * Base class for a JSON-RPC server that can be controlled remotely.
 * A method is exported allowing to invoke an arbitrary command.
 * 
 * @author steffen
 *
 */
public class RemoteControllableServer {
	
	// the command processor used for invoking (remote) commands
	private CommandProcessor commandProcessor;

	public RemoteControllableServer(CommandProcessor commandProcessor) {
		this.commandProcessor = commandProcessor;
	}

	/**
	 * Locally invoke a command that is received from a remote site. This effectively
	 * enables to remote control the application.
	 * 
	 * @param command command to be invoked
	 */
	@RMI
	public void invokeCommand(String command) {
		// invoke the given command with the given parameters
		commandProcessor.processCommand(command);
	}

}
