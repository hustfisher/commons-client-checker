package com.weibo.api.commons.client.monitor.listener;

import com.weibo.api.commons.client.monitor.MonitorEventBuilder;


/**
 * 
 * Listener the client operations.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public interface ClientListener {
	
	/**
	 * Notify the listener when the client has completed the cmd.
	 * @param clientMonitor
	 */
	void onProcessComplete(MonitorEventBuilder eventClone);
}
