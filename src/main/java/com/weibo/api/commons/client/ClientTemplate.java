package com.weibo.api.commons.client;

import com.weibo.api.commons.client.ClientFactory.ServerStatus;

/**
 * 
 * Template for all kinds of client like mc-client、jedis、jdbctemplate、rpc-client and so on. 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public interface ClientTemplate<T> {
	
	void updateServerStatus(ServerStatus serverStatus);
	
	T getClient();
	
	String getName();
}
