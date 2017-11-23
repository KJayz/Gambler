package lu.uni.distributedsystems.project.common.command;

import lu.uni.distributedsystems.gsonrmi.server.RpcConnectionHandler;
import lu.uni.distributedsystems.gsonrmi.server.ServiceMode;

/**
 * Implementation of the command set_mode [partner-id] [service-mode]
 * To learn more about the set_mode functionality, please refer to the
 * project description.
 * 
 * @author steffen
 *
 */
public class SetModeCommand extends Command {

	public SetModeCommand(CommandProcessor commandProcessor) {
		super(commandProcessor, "set_mode");
	}

	@Override
	public void process(String[] args) {
		// extract arguments
		String partnerID = args[0];
		String serviceModeString = args[1];
		ServiceMode serviceMode = null;
		
		try {
			// try to convert from string
			serviceMode = ServiceMode.valueOf(args[1]);
		}
		catch (IllegalArgumentException ex) {
			// try to convert from integer value
			try {
				int serviceModeOrdinal = Integer.parseInt(serviceModeString);
				
				serviceMode = ServiceMode.values()[serviceModeOrdinal];
			}
			catch (NumberFormatException ex2) {
				System.err.println("Wrong mode. Please try:");
				showHelp();
				return;
			}
		}
		RpcConnectionHandler.setServiceMode(partnerID, serviceMode);
	}

	@Override
	public void showHelp() {
		System.out.println("set_mode [partner-id] [service-mode] : sets mode of partner (bookie or gambler)");
		System.out.println("    Determines how subsequent requests received from the given partner will be handled.");
		System.out.println("    Possible service-modes are: RELIABLE (0), DISCONNECT_BEFORE_PROCESSING (1), DISCONNECT_BEFORE_REPLY (2), and RANDOM (3).");
		System.out.println("    The service-mode can be specified either by name or by ordinal.");
	}

}
