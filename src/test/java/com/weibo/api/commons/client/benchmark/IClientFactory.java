package com.weibo.api.commons.client.benchmark;

import com.weibo.api.commons.client.AbstractClientFactory;
import com.weibo.api.commons.client.ClientFactory.ServerStatus;

/**
 * 
 * 类说明
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-19
 */

public class IClientFactory extends AbstractClientFactory<IClient>{

	private IClient iclient = new ILazyClient();
	private ServerStatus serverStatus;
	
	@Override
	protected IClient doGetClient() {
		return iclient;
	}
	@Override
	protected IClient doGetMockClient() {
		return null;
	}
	@Override
	public com.weibo.api.commons.client.ClientFactory.ServerStatus getServerStatus() {
		return serverStatus;
	}
	@Override
	public void updateServerStatus(
			com.weibo.api.commons.client.ClientFactory.ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}
}
