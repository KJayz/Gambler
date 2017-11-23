package lu.uni.distributedsystems.project.gambler;

import com.google.code.gsonrmi.Parameter;
import com.google.code.gsonrmi.RpcResponse;

import lu.uni.distributedsystems.project.common.JsonRpcConnection;
import lu.uni.distributedsystems.project.common.PlaceBetResult;

/**
 * A <code>BookieConnection</code> represents the communication channel
 * from this gambler to some bookie. A separate <code>BookieConnection</code>
 * is being created for each bookie this gambler establishes a connection with.
 * <p>
 * All methods required to communicate with a bookie should be implemented
 * within this class. In other words, for each method bookies expose via
 * JSON-RPC, there will be an associated method within this class to perform
 * the JSON-RPC call. 
 * 
 * @author steffen
 *
 */
public class BookieConnection extends JsonRpcConnection {
	
	private String bookieID; // bookie-ID of the bookie on the other side of this connection
	private Gambler gambler; // the enclosing gambler
	
	/**
	 * Create a connection with the specified bookie.
	 * 
	 * @param gambler    the enclosing gambler
	 * @param bookieIP   IP address of the bookie to setup a connection with
	 * @param bookiePort port number on which the bookie listens
	 */
	public BookieConnection(Gambler gambler, String bookieIP, int bookiePort) {
		// initialize JsonRpcConnection base class
		super(bookieIP, bookiePort, gambler.getGamblerID());
		this.gambler = gambler;
		// bookieID can be set only after having established the connection
	}
	
	/**
	 * Simple hello method for illustration purposes. Constructs a JSON-RPC request,
	 * respectively its parameters, invokes the JSON-RPC method on the bookie being
	 * connected with, and retrieves the response sent back by the remote bookie.
	 */
	public void sayHello() {
		Parameter[] params = new Parameter[] { new Parameter(gambler.getGamblerID()) };
		RpcResponse response = handleJsonRpcRequest("sayHelloToBookie", params);
		
		// show hello message returned by bookie
		String sayHelloResponse = response.result.getValue(String.class, getGson());
		System.out.println("Bookie " + bookieID + " sent response: " + sayHelloResponse);
	}
	
	/**
	 * Invokes the JSON-RPC connect method on a bookie that shall be
	 * connected to the gambler through this BookieConnection.
	 * 
	 * @param gamblerIP   IP address of the gambler to establish a connection with
	 * @param gamblerPort port number of the gambler to establish a connection with
	 * @return bookie-id of the bookie being connected with
	 */
	public String sendConnectCommand(String gamblerIP, int gamblerPort) {
		Parameter[] params = new Parameter[] {
				new Parameter(gambler.getGamblerID()),
				new Parameter(gamblerIP),
				new Parameter(gamblerPort)
		};
		// handleJsonRpcRequest sends a JSON-RPC request to the bookie that is connected
		// via this BookieConnection
		RpcResponse response = handleJsonRpcRequest("connect", params);
		
		// response contains bookie-id
		bookieID = response.result.getValue(String.class, getGson());
		
		System.out.println("connected with bookie " + bookieID);
		return bookieID;
	}
	
	/**
	 * Place a bet with the specified bookie
	 * 
	 * @param bookieID id of the bookie the bet is being placed with
	 * @param matchID id of the match the bet is being placed on
	 * @param team name of the team inside the match the gambler is betting on
	 * @param stake the wager of the bet
	 * @param odds the odds of the team winning the match
	 */
	
	public PlaceBetResult bet(String bookieID, int matchID, String team, int stake, float odds) {
		Parameter[] params = new Parameter[] {
				new Parameter(bookieID),
				new Parameter(matchID),
				new Parameter(team),
				new Parameter(stake),
				new Parameter(odds)
		};
		
		// handleJsonRpcRequest sends a JSON-RPC request to the bookie that is connected
		// via this BookieConnection
		RpcResponse response = handleJsonRpcRequest("bet",params);
		
		// response is either accepted or rejected for a reason
		// See PlaceBetResult enum for possiblities
		PlaceBetResult bookieResponse = response.result.getValue(PlaceBetResult.class, getGson());
		
		return bookieResponse;
	}
	// TODO insert all methods required to communicate with a bookie
	
}
