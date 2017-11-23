package lu.uni.distributedsystems.gsonrmi.server;

/**
 * Enumeration of the modes in which JSON-RPC requests shall be processed.
 */
public enum ServiceMode {
	/**
	 * Process requests in a reliable way, i.e. without disconnections, interruptions, crashes, or alike.
	 */
	RELIABLE,
	
	/**
	 * Close the connection before handling the request in any way. Please note
	 * that no interceptor will be called in this mode.
	 */
	DISCONNECT_BEFORE_PROCESSING,
	
	/**
	 * Close the connection after having processed the request, but before
	 * sending back the response. Please note that in this mode, the
	 * interceptor will be called for both the request as well as the response. 
	 */
	DISCONNECT_BEFORE_REPLY,
	
	/**
	 * The service mode will be determined randomly for every subsequent
	 * request. This includes all three other modes, i.e. RELIABLE,
	 * DISCONNECT_BEFORE_PROCESSING, and DISCONNECT_BEFORE_REPLY.
	 * Please note that by definition of the two latter modes, the
	 * connection will be closed in those cases. If the randomly
	 * selected service mode is RELIABLE, however, the next request
	 * will be handled in a reliable fashion, keeping the connection
	 * alive. For each subsequent request, a service mode will again
	 * be chosen randomly.
	 */
	RANDOM 
	};
