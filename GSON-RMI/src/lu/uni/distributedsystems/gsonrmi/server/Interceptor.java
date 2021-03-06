package lu.uni.distributedsystems.gsonrmi.server;

import com.google.code.gsonrmi.RpcRequest;
import com.google.code.gsonrmi.RpcResponse;

/**
 * Server-side request interceptor.
 * An Interceptor allows to intercept incoming JSON-RPC requests
 * as well as JSON-RPC responses.
 */
public interface Interceptor {
	
	/**
	 * Intercept an incoming JSON-RPC request before being processed.
	 * 
	 * @param request	the incoming JSON-RPC request object
	 * @return			a response for this request, or null if the response
	 * 					should be generated by handling the request 
	 */
	public RpcResponse interceptRequest(RpcRequest request);
	
	/**
	 * Intercept a response before sending it back to the caller.
	 * 
	 * @param request	the request that has been handled
	 * @param response	the response that has been generated by handling the request
	 */
	public void interceptResponse(RpcRequest request, RpcResponse response);
	
}
