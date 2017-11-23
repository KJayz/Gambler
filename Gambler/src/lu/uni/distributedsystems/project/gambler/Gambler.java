package lu.uni.distributedsystems.project.gambler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import lu.uni.distributedsystems.project.common.PlaceBetResult;
import lu.uni.distributedsystems.project.common.command.*;
import lu.uni.distributedsystems.project.gambler.commands.BetCommand;
import lu.uni.distributedsystems.project.gambler.commands.ConnectCommand;
import lu.uni.distributedsystems.project.gambler.commands.FillWalletCommand;
import lu.uni.distributedsystems.project.gambler.commands.SayHelloCommand;
import lu.uni.distributedsystems.project.gambler.commands.ShowBetsCommand;
import lu.uni.distributedsystems.project.gambler.commands.ShowMatchesCommand;

/**
 * Main class of a Gambler
 * 
 * @author steffen
 *
 */
public class Gambler {

	/**
	 * unique human-readable name for the gambler, such as Alice, Bob, ...
	 */
	private String gamblerID;

	/**
	 * the JSON-RPC server associated with this gambler
	 */
	private GamblerServer gamblerServer;

	/**
	 * the command processor in charge of executing commands, such as bet, show_bets, ...
	 */
	private CommandProcessor commandProcessor;
	
	/**
	 * directory of all known bookie connections
	 */
	private Map<String, BookieConnection> bookieConnections;
	
	/**
	 * the amount of money available for the gambler (Default value can be altered at will)
	 */
	private int wallet =0;
	
	
	/**
	 * an array containing the information of all the bets the gambler has made
	 */
	private ArrayList<Bet> bets = new ArrayList<Bet>();
	
	/**
	 * an array containing the information of all matches which the 
	 * gambler knows of and hasn't bet on yet
	 */
	private ArrayList<AvailableMatch> availableMatches = new ArrayList<AvailableMatch>();

	/**
	 * Construct a new gambler instance, including to create and start
	 * an associated JSON-RPC server.
	 * 
	 * @param gamblerID   unique human-readable name for the gambler
	 * @param gamblerIP   IP address where the associated JSON-RPC server shall wait for connections
	 * @param gamblerPort port number where the associated JSON-RPC server shall listen on
	 */
	public Gambler(String gamblerID, String gamblerIP, int gamblerPort) {
		this.gamblerID = gamblerID;
		this.bookieConnections = new HashMap<String, BookieConnection>();
		
		// create a command processor and register all commands
		commandProcessor = new CommandProcessor(new Scanner(System.in));
		new ConnectCommand(commandProcessor, this);
		new BetCommand(commandProcessor, this);
		new ShowBetsCommand(commandProcessor, this);
		new ShowMatchesCommand(commandProcessor, this);
		new FillWalletCommand(commandProcessor, this);
		new SayHelloCommand(commandProcessor, this);
		// create the gambler's JSON-RPC server
		gamblerServer = new GamblerServer(this, gamblerIP, gamblerPort, commandProcessor);
		// start the bookie server, i.e. the remote interface which gamblers may invoke
		gamblerServer.start();
	}

	/**
	 * Main loop, processing incoming commands.
	 */
	public void run() {
		try {
			// start the command processor such that the user can enter commands
			commandProcessor.start();
			// wait for the command processor to exit
			commandProcessor.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the gambler-id, which is a unique human-readable name of this gambler.
	 * 
	 * @return The unique human-readable name of this gambler.
	 */
	public String getGamblerID() {
		return gamblerID;
	}

	/**
	 * Create a new connection with some bookie.
	 * 
	 * @param bookieIP   IP address of the bookie's JSON-RPC server
	 * @param bookiePort port number on which the bookie's JSON-RPC server listens
	 */
	public void createNewBookieConnection(String bookieIP, int bookiePort) {
		// create a new connection with a bookie at the given address
		BookieConnection bookieConnection = new BookieConnection(this, bookieIP, bookiePort);

		// setup a socket connection to the bookie's JSON-RPC server
		bookieConnection.establishSocketConnection();
		// send a "connect" command to register this gambler with the bookie
		String bookieID = bookieConnection.sendConnectCommand(gamblerServer.getGamblerIP(), gamblerServer.getGamblerPort());
		// register bookie connection
		bookieConnections.put(bookieID, bookieConnection);
	}

	/**
	 * Shut down the gambler, properly releasing all resources.
	 */
	private void shutdown() {
		// shut down all bookie connections
		for (BookieConnection bookieConnection : bookieConnections.values())
			bookieConnection.closeConnection();
	}

	/**
	 * Sample method to say hello to some bookie.
	 * 
	 * @param bookieID bookie-id of the bookie to say hello to
	 */
	public void sayHelloToBookie(String bookieID) {
		// need to perform sanity check here; bookie might be unknown ...
		bookieConnections.get(bookieID).sayHello();
	}

	/**
	 * Adds (or deducts) the given amount of money to the gambler's wallet, then
	 * prints the current amount.
	 * 
	 * @param amount amount of money to be added (if positive) or deducted (if negative)
	 */
	public void fillWallet(int amount) {
		// TODO add or deduct money
		this.wallet += amount;
		
		System.out.println("The gambler's wallet is now at: " +wallet);
		
	}

	/**
	 * Prints a list of all open matches of all bookies (since having connected).
	 */
	public void showMatches() {
		// TODO show all open matches of all connected bookies since having connected
		
		//Prints all info for each match
		while(availableMatches.iterator().hasNext()) {
			AvailableMatch temp = availableMatches.iterator().next();
			System.out.println("Bookie: "+temp.getBookieID());
			System.out.println("MatchID: "+temp.getMatchID());
			System.out.println("Team A: "+temp.getTeamA());
			System.out.println("Odds A: "+temp.getOddsA());
			System.out.println("Team B: "+temp.getTeamB());
			System.out.println("Odds B: "+temp.getOddsB());
			System.out.println("Limit: "+temp.getLimit());
			System.out.println("---------");
		}
		
		System.out.println("All matches have been listed!");
	}

	/**
	 * Place a bet on one of the teams of a running match.
	 * Each gambler can place at most one bet per match and per bookie.
	 * The bookie can either accept or reject a bet. One reason for rejecting
	 * a bet might be that the total wager for this match would be exceeded.
	 * Another reason might be that the odds specified in the gambler’s 
	 * request are outdated, because of having been changed in the meantime
	 * on the bookie’s side. In case the bet is accepted, the gambler will 
	 * deduct the stake from his wallet.
	 * 
	 * @param bookieID bookie-id of the bookie to place the bet with
	 * @param matchID  match-id of the match to place the bet for
	 * @param team     name of the team to place the bet for, i.e. the team expected to win the match
	 * @param stake    amount of money placed
	 * @param odds     odds of the team the bet is placed on
	 */
	public void bet(String bookieID, int matchID, String team, int stake, float odds) {
		// TODO bet on a match
		// hint: use the PlaceBetResult enum to cover the different cases that can occur when placing a bet
		
		PlaceBetResult response = bookieConnections.get(bookieID).bet(bookieID, matchID, team, stake, odds);
		
		if(response == PlaceBetResult.ACCEPTED) {
			Bet bet = new Bet(bookieID, matchID, team, stake, odds);
			bets.add(bet);
			
			System.out.println("Bet made with bookie " + bookieID);
			System.out.println("on match with ID: "+ matchID);
		} else {
			System.out.println("Bet rejected. Bookie returned the following info: ");
			System.out.println("--" + response + "--");
		}
	}

	/**
	 * Shows a list of all bets placed by the gambler.
	 */
	public void showBets() {
		
		while(bets.iterator().hasNext()) {
			Bet temp = bets.iterator().next();
			System.out.println("Bookie: "+temp.getBookieID());
			System.out.println("Match ID: "+temp.getMatchID());
			System.out.println("Team: "+temp.getTeam());
			System.out.println("Odds: "+temp.getOdds());
			System.out.println("Stake: "+temp.getStake());
			System.out.println("---------");
		}
		
		System.out.println("All bets have been listed!");
	}
	
	/**
	 * Configuration of the logging messages generated during runtime. 
	 */
	private static void configureLogging() {
		Logger logger = Logger.getLogger("lu.uni");

		// uncomment the following line to suppress log messages
//		logger.setLevel(java.util.logging.Level.OFF);

		// create a handler to show messages on the console
		ConsoleHandler consoleHandler = new ConsoleHandler();
		
		// restrict log messages to the minimum, i.e. just the message itself
		consoleHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getMessage() + "\n";
			}
		});
		logger.setUseParentHandlers(false);
		logger.addHandler(consoleHandler);
	}
	
	/**
	 * Configure and launch a gambler instance.
	 * 
	 * @param args command-line arguments (unused)
	 */
	public static void main(String[] args) {
		Scanner consoleScanner = new Scanner(System.in);
		Gambler gambler = null;
		
		try {
			String gamblerID;
			String gamblerIP;
			int gamblerPort;
			
			// configure (console) logging
			configureLogging();
			
			if (args.length == 3) {
				gamblerID = args[0];
				gamblerIP = args[1];
				gamblerPort = Integer.parseInt(args[2]);
			}
			else {
				// let the user enter the gambler's configuration
				System.out.print("Please enter unique Gambler-ID: ");
				gamblerID = consoleScanner.nextLine();
				System.out.print("Please enter IP address for the gambler server: ");
				gamblerIP = consoleScanner.nextLine();
				System.out.print("Please enter port number for the gambler server: ");
				gamblerPort = Integer.parseInt(consoleScanner.nextLine());
			}

			// create the gambler
			gambler = new Gambler(gamblerID, gamblerIP, gamblerPort);

			gambler.run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consoleScanner.close();
			// make sure the socket connections are closed, whatever happens ...
			if (gambler != null)
				gambler.shutdown();
		}
	}

}
