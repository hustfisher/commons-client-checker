package com.weibo.api.commons.client;

import com.weibo.api.commons.client.exception.ServerUnavailableException;

/**
 * 
 * Used to manager the lifecycle of client 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public abstract class AbstractClientFactory<T> implements ClientFactory<T>{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T getClient() throws ServerUnavailableException {
		ServerStatus status = getServerStatus();
		if(null == status || ServerStatus.running == status){
			return doGetClient();
		}else if(ServerStatus.mocking == status){
			return doGetMockClient();
		}else if(ServerStatus.unavailable == status){
			throw new ServerUnavailableException();
		}else{
			throw new RuntimeException("Unknown server status: " + status);
		}
	}
	
	/**
	 * return the client for communicate with resources.
	 * @return
	 */
	protected abstract T doGetClient();
	
	/**
	 * return the mock client for mock datas.
	 * @return
	 */
	protected abstract T doGetMockClient();
}
