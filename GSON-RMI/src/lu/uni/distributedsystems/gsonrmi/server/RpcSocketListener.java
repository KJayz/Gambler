package lu.uni.distributedsystems.gsonrmi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import com.google.code.gsonrmi.server.RpcTarget;
import com.google.gson.Gson;

/**
 * A listener for JSON-RPC server connection requests.
 * Objects of this class each run in their own separate thread.
 */
public class RpcSocketListener extends Thread {

	private ServerSocket ss;
	private RpcTarget rpcTarget;
	private Gson gson;
	private Interceptor interceptor;
	
	private static Logger logger = Logger.getLogger(RpcSocketListener.class.getName());

	/**
	 * Constructs a listener for JSON-RPC server connection requests.
	 * 
	 * @param port			local port to listen for connection requests
	 * @param rpcTarget		the RpcTarget of the associated JSON-RPC server implementation
	 * @param gson			the Gson object to use throughout request processing
	 * @param interceptor	a request/reply interceptor, or null if none is required
	 * @throws IOException	if there is an issue creating the server-side socket listener
	 */
	public RpcSocketListener(int port, RpcTarget rpcTarget, Gson gson, Interceptor interceptor) throws IOException {
		this.rpcTarget = rpcTarget;
		this.gson = gson;
		this.interceptor = interceptor;
		ss = new ServerSocket(port);
	}
	
	/**
	 * Shuts down this connection listener by closing the server-side socket.
	 * 
	 * @throws IOException	if there is a problem closing the server socket
	 */
	public void shutdown() throws IOException {
		ss.close();
	}
	
	/**
	 * Main loop of this connection listener. Listens for incoming connection
	 * attempts. Accepts connections and creates an RpcConnectionHandler
	 * to process requests from that connection.
	 */
	@Override
	public void run() {
		logger.info("listening for connections at port: " + ss.getLocalPort());
		try {
			while (true) {
				new RpcConnectionHandler(ss.accept(), rpcTarget, gson, interceptor).start();
			}
		}
		catch (IOException e) {
			if (ss.isClosed()) System.err.println("RPC server terminated normally");
			else {
				System.err.println("RPC server terminated by error");
				e.printStackTrace();
			}
		}
	}


}
