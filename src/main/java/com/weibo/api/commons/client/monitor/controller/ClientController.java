package com.weibo.api.commons.client.monitor.controller;

import com.weibo.api.commons.client.monitor.MonitorEventBuilder;

/**
 * 
 * To control the client operator.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-17
 */

public interface ClientController {

	/**
	 * notify the controller when client start to process command.
	 * @param clientThread
	 * @param event
	 */
	void onProcessStart(Thread clientThread, MonitorEventBuilder monitorEvent);
	
	/**
	 * notify the controller when client has completed the process
	 * @param clientThread
	 */
	void onProcessCompleted(Thread clientThread);
}
