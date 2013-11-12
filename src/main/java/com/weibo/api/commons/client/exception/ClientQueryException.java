package com.weibo.api.commons.client.exception;

/**
 * 
 * 类说明
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-22
 */

public class ClientQueryException extends RuntimeException {

	private static final long serialVersionUID = -6769086558941290862L;

	public ClientQueryException(){
		super();
	}
	
	public ClientQueryException(String message){
		super(message);
	}
	
	public ClientQueryException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ClientQueryException(Throwable cause){
		super(cause);
	}
}
