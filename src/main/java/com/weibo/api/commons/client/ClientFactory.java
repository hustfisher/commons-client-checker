package com.weibo.api.commons.client;

import com.weibo.api.commons.client.exception.ServerUnavailableException;


/**
 * 
 * Used to manager the lifecycle of client 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public interface ClientFactory<T> {

	/**
	 * return client via the status, or throws #ServerUnavailableException.
	 * 
	 * @return
	 * @throws ServerUnavailableException
	 */
	T getClient() throws ServerUnavailableException;
	
	/**
	 * update server status，used to control the switch of resource
	 * @param serverStatus
	 */
	void updateServerStatus(ServerStatus serverStatus);
	
	/**
	 * get server status.
	 * @return
	 */
	ServerStatus getServerStatus();
	
	/**
	 * server status
	 * @author fishermen
	 *
	 */
	public static enum ServerStatus{
		running,
		mocking,
		unavailable;
	}
}
