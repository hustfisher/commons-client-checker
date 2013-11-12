package com.weibo.api.commons.client.benchmark;

/**
 * 
 * 类说明
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-23
 */

public class ClientTimeoutException extends RuntimeException{

	private static final long serialVersionUID = 4030457449165356090L;

	public ClientTimeoutException(String message){
		super(message);
	}
	
	public ClientTimeoutException(String message, Throwable cause){
		super(message, cause);
	}
}
