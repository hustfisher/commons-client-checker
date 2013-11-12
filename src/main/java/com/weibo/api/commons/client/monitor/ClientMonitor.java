package com.weibo.api.commons.client.monitor;

import com.weibo.api.commons.client.monitor.listener.ClientListener;

/**
 * 
 * Monitor the operation of clients.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public interface ClientMonitor {

	
	/**
	 * Add listener to clientListeners, provided that it is not the same as 
	 * the listeners already in collections.
	 * @param clientListener
	 */
	void register(ClientListener clientListener);
	
	/**
	 * If the client will process commands, notify the monitor.
	 * @param clientName
	 * @param commandName
	 * @param key
	 * @param timeoutMills
	 */
	void notifyBeforeProcess(String clientName, String commandName, String key, int timeoutMills);
	
	/**
	 * If the client has processed commands, notify the monitor.
	 * 
	 * @param succeed the client execute result.
	 */
	void notifyAfterProcess(boolean succeed);
	
}
