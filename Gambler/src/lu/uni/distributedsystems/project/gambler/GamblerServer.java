package lu.uni.distributedsystems.project.gambler;

import java.io.IOException;

import com.google.code.gsonrmi.Parameter;
import com.google.code.gsonrmi.RpcRequest;
import com.google.code.gsonrmi.RpcResponse;
import com.google.code.gsonrmi.annotations.RMI;
import com.google.code.gsonrmi.serializer.ExceptionSerializer;
import com.google.code.gsonrmi.serializer.ParameterSerializer;
import com.google.code.gsonrmi.server.RpcTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lu.uni.distributedsystems.gsonrmi.server.Interceptor;
import lu.uni.distributedsystems.gsonrmi.server.RpcSocketListener;
import lu.uni.distributedsystems.project.common.RemoteControllableServer;
import lu.uni.distributedsystems.project.common.command.CommandProcessor;

/**
 * JSON-RPC server associated with a gambler. Receives JSON-RPC methods via
 * a socket connection. All methods that should be exposed to remote JSON-RPC
 * calls must be defined in this class. Use the <code>@RMI</code> annotation
 * for all such JSON-RPC methods.
 * 
 * @author steffen
 *
 */
public class GamblerServer extends RemoteControllableServer {

	/**
	 * the enclosing gambler
	 */
	@SuppressWarnings("unused")
	private Gambler gambler;
	
	/**
	 * IP address of the gambler's JSON-RPC server
	 */
	private String gamblerIP;
	
	/**
	 * port number on which the gambler's JSON-RPC server listens
	 */
	private int gamblerPort;
	
	/**
	 * Create a new JSON-RPC server instance that is associated with a gambler.
	 * 
	 * @param gambler          enclosing gambler this JSON-RPC server is associated with
	 * @param gamblerIP        IP address the JSON-RPC server shall listen on
	 * @param gamblerPort      port number the JSON-RPC server shall listen on
	 * @param commandProcessor command processor used for processing remote commands
	 */
	public GamblerServer(Gambler gambler, String gamblerIP, int gamblerPort, CommandProcessor commandProcessor) {
		super(commandProcessor);
		this.gambler = gambler;
		this.gamblerIP = gamblerIP;
		this.gamblerPort = gamblerPort;
	}
	
	/**
	 * Gets the IP address this JSON-RPC server listens on.
	 * 
	 * @return IP address of this JSON-RPC server listens on
	 */
	public String getGamblerIP() {
		return gamblerIP;
	}
	
	/**
	 * Gets the port number this JSON-RPC server listens on.
	 * 
	 * @return The port number this JSON-RPC server listens on
	 */
	public int getGamblerPort() {
		return gamblerPort;
	}

	/**
	 * Simple hello method for testing and reference purposes.
	 * <p>
	 * Please note that the <code>@RMI</code> annotation exposes a method for remote invocation
	 * 
	 * @param bookieName name of the bookie who says hello
	 * @return Hello reply message sent back to the remote bookie
	 */
	@RMI
	public String sayHelloToGambler(String bookieName) {
		// implementation of the sayHelloToGambler method, which
		// can be invoked remotely with one argument of
		// type String, returning another String
		System.out.println("sayHelloToGambler(" + bookieName + ")");
		return "Gambler says: Hello, bookie " + bookieName;
	}
	
	/**
	 * Method which captures a match created by a bookie.
	 * 
	 * @param
	 * 
	 */
	@RMI
	public String startBetPhase(String bookieID, int matchID, String teamA, int oddsA, String teamB, int oddsB, int limit) {
		
		AvailableMatch match = new AvailableMatch(bookieID, matchID, teamA, oddsA, teamB, oddsB, limit);
		gambler.availableMatches.add(match);
		return "Match with ID: "+matchID+" received from bookie: "+bookieID;
	}

	/**
	 * Method which captures a change in the odds of a match.
	 * 
	 * @param bookieID The bookie who's changing the odds
	 * @param matchID The match who's odds are being changed
	 * @param team The team involved
	 * @param odds The new odds
	 * @return Response message to the bookie
	 */
	@RMI
	public String setOdds(String bookieID, int matchID, String team, int odds) {
		
		for(AvailableMatch match : gambler.availableMatches) {
			if(match.getBookieID().equals(bookieID) && match.getMatchID() == matchID) {
				if(match.getTeamA().equals(team)) {
					match.setOddsA(odds);
					return "BookieID: " + " | MatchID: " + matchID + " | TeamA: " + team + " | New odds: " + odds;
				}
				
				if(match.getTeamB().equals(team)) {
					match.setOddsB(odds);
					return "BookieID: " + " | MatchID: " + matchID + " | TeamB: " + team + " | New odds: " + odds;
				}
			}

		}
		
		//If gambler does not have the stated match or if spelling mistake by bookie
		return "Match not found.";
	}
	
	@RMI
	public String endBetPhase(int matchID, String winningTeam, float payout) {
		for(Bet bet : gambler.bets) {
			if(bet.getMatchID() == matchID) {
				if(bet.getTeam().equals(winningTeam)) {
					//Have to round since payout is a float and the wallet contains int
					int money = Math.round(payout);
					gambler.fillWallet(money);
					System.out.println("Match: " + matchID + " finished. You earned "+ money+ " !");
				}
				//matchID is unique
				break;
			}
			
			
		}
		
		//Cleanup
		for(int i = 0;i<gambler.bets.size();i++) {
			if(gambler.bets.get(i).getMatchID() == matchID) {
				gambler.bets.remove(i);
			}
		}
		return "endBetPhase method received by: " + gambler.getGamblerID();
	}
	// TODO insert the methods that can be invoked remotely via JSON-RPC on this gambler
	
	
	/**
	 * Starts the JSON-RPC server. All methods tagged with the <code>@RMI</code> annotation
 	 * will be invokable remotely. All requests as well as all responses will be
	 * passed on to the <code>Interceptor</code> instantiated in the body of this method.
	 */
	public void start() {
		// start a JSON-RPC server; all methods tagged with the @RMI annotation will
		// be invokable remotely; all requests as well as all responses will be
		// passed on to the interceptor; please note that this class extends BaseServer
		// and thus will expose an additional method setModeOfHost to control how
		// requests will be processed

		// create an instance of Gson, the primary class for using the Gson libraries
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Exception.class, new ExceptionSerializer())
				.registerTypeAdapter(Parameter.class, new ParameterSerializer())
				.create();

		// TODO create an interceptor that will intercept both requests as well as responses
		// for illustration purposes, a simple interceptor is created that simply prints
		// all intercepted requests and responses
		Interceptor interceptor = new Interceptor() {
			
			@Override
			public RpcResponse interceptRequest(RpcRequest request) {
				System.out.println("intercepted request: " + gson.toJson(request));
				return null;
			}
			
			@Override
			public void interceptResponse(RpcRequest request, RpcResponse response) {
				System.out.println("intercepted response: " + gson.toJson(response) + " for request: " + gson.toJson(request));
			}

		};
		
		// launch a socket listener accepting and handling JSON-RPC requests
		try {
			new RpcSocketListener(gamblerPort, new RpcTarget(this, gson), gson, interceptor).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
