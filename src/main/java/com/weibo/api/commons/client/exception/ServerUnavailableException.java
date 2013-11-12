package com.weibo.api.commons.client.exception;

/**
 * 
 * server unavailable exception
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-12
 */

public class ServerUnavailableException extends RuntimeException{

	private static final long serialVersionUID = 8682823304373992521L;

	public ServerUnavailableException() {
		super();
	}
	
	public ServerUnavailableException(String message){
		super(message);
	}
	
	public ServerUnavailableException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ServerUnavailableException(Throwable cause){
		super(cause);
	}
}
