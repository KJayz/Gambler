package lu.uni.distributedsystems.project.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.google.code.gsonrmi.Parameter;
import com.google.code.gsonrmi.RpcRequest;
import com.google.code.gsonrmi.RpcResponse;
import com.google.code.gsonrmi.serializer.ExceptionSerializer;
import com.google.code.gsonrmi.serializer.ParameterSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import lu.uni.distributedsystems.gsonrmi.server.PartnerID;

/**
 * Base class for any JSON-RPC connection. This class implements the
 * basic functionality in terms of both connection management with a
 * JSON-RPC server, as well as for JSON-RPC request processing.
 * 
 * @author steffen
 *
 */
public class JsonRpcConnection {

	private String serverIP;  // IP address of the JSON-RPC server
	private int serverPort;   // port number of the JSON-RPC server
	private String partnerID; // our own partnerID, used on JSON-RPC server-side to uniquely identify this sender, specially in case of necessary re-connections
	
	private Socket socket;         // socket connection with the JSON-RPC server
	private Gson gson;             // gson object to use e.g. for serialization/deserialization
	private Writer writer;         // writer to write to socket's stream
	private JsonReader jsonReader; // JSON reader to read from socket's stream

	private static Logger logger = Logger.getLogger(JsonRpcConnection.class.getName());
	
	/**
	 * Initialize a new JSON-RPC connection with the specified JSON-RPC server. 
	 * 
	 * @param serverIP   IP address of the remote JSON-RPC server
	 * @param serverPort port on which the remote JSON-RPC server listens on
	 * @param partnerID  own id, i.e. the id of the enclosing bookie or gambler which establishes the connection
	 */
	public JsonRpcConnection(String serverIP, int serverPort, String partnerID) {
		// remember IP and port of the JSON-RPC server as well as our partnerID, also in case we need to re-connect
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.partnerID = partnerID;

		// create an instance of Gson, the primary class for using the Gson libraries
		gson = new GsonBuilder()
				.registerTypeAdapter(Exception.class, new ExceptionSerializer())
				.registerTypeAdapter(Parameter.class, new ParameterSerializer())
				.create();
	}
	
	public Gson getGson() {
		return gson;
	}
	
	/**
	 * Establish a connection with the JSON-RPC server via a socket, and
	 * create reader and writer objects to send and received data.
	 */
	public void establishSocketConnection() {
		// setup a socket connection to the JSON-RPC server
		try {
			socket = new Socket(serverIP, serverPort);
			logger.info("Connection with JSON-RPC server opened at local endpoint " + socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort());
			// create a writer to send JSON-RPC requests to the JSON-RPC server
			writer = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			// create a JsonReader object to receive JSON-RPC response
			jsonReader = new JsonReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			// send across our partnerID, which identifies this sender side uniquely, specially
			// in case of a re-connect; this way, the receiving JSON-RPC server can retain
			// the mapping of connections onto a partnerID
			PartnerID partnerIDObject = new PartnerID(partnerID);
			
			writer.write(gson.toJson(partnerIDObject));
			writer.flush();
			// response is ignored at this point
			gson.fromJson(jsonReader, JsonObject.class);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the connection with the JSON-RPC server.
	 */
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			logger.warning("problem closing socket connection with JSON-RPC server at " + serverIP + ":" + serverPort);
			e.printStackTrace();
		}
	}

	/**
	 * Main method for JSON-RPC request processing.
	 * 
	 * @param method name of the JSON-RPC method to call
	 * @param params array of parameters to pass as arguments
	 * @return The result of the JSON-RPC method invocation.
	 */
	// TODO adapt this method to properly handle sending JSON-RPC requests
	protected RpcResponse handleJsonRpcRequest(String method, Parameter[] params) {
		RpcRequest request = new RpcRequest();
		RpcResponse response = null;
		
		request.method = method;
		request.params = params;
		
		// TODO make sure to assign an appropriate message-id
		
		switch (method) {
		case "connect":
			request.id = new Parameter(1);
			break;
		case "bet":
			request.id = new Parameter(2);
			break;
			// Add when creating new types of requests
		}

		try {
			logger.info("sending request: " + gson.toJson(request));

			// attempting to send the request via the writer to
			// the JSON-RPC server might throw an IOException
			writer.write(gson.toJson(request));
			writer.flush();

			// in case of a connection loss, the following call will throw an exception
			response = gson.fromJson(jsonReader, RpcResponse.class);

			logger.info("received response: " + gson.toJson(response) + " for request: " + gson.toJson(request));
		}
		catch (Exception ex) {
			// TODO handle lost connections
			// connection to JSON-RPC server is lost
			
			System.err.print("Your last request was not received. Try again in a few moments. If the problem persists, check your connection with the server.");
		}
		return response;
	}


}
