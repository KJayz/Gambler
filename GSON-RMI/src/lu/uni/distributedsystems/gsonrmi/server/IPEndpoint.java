package lu.uni.distributedsystems.gsonrmi.server;

import java.net.InetAddress;

/**
 * An IPEndpoint represents a combination of IP address and port number.
 */
public class IPEndpoint {
	
	private InetAddress inetAddress;
	private int port;
	
	/**
	 * Constructs an IPEndpoint with the specified IP address and port number.
	 * 
	 * @param inetAddress	IP address of the end point
	 * @param port			port number of the end point
	 */
	IPEndpoint(InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}
	
	/**
	 * Retrieves the IP address of the IPEndpoint.
	 * 
	 * @return	IP address of the end point
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}
	
	/**
	 * Retrieves the port number of the IPEndpoint.
	 * 
	 * @return	port number of the end point
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Converts this IPEndpoint to a String.
	 */
	@Override
	public String toString() {
		return inetAddress.getHostAddress() + ":" + port;
	}
	
	/**
	 * Returns a hash code for this IPEndpoint.
	 */
	@Override
	public int hashCode() {
		return inetAddress.hashCode();
	}
	
	/**
	 * Compares this object against the specified object.
	 * 
	 * @param obj	the object to compare against
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (! (obj instanceof IPEndpoint))
			return false;
		
		IPEndpoint other = (IPEndpoint) obj;
		
		return (inetAddress.getHostAddress().equals(other.inetAddress.getHostAddress()) && port == other.port);
	}

}
