package com.weibo.api.commons.client.benchmark;

import com.weibo.api.commons.client.AbstractClientTemplate;
import com.weibo.api.commons.client.ClientCallback;
import com.weibo.api.commons.client.ClientFactory;
import com.weibo.api.commons.client.config.ClientConfig;
import com.weibo.api.commons.client.monitor.ClientMonitorImpl;
import com.weibo.api.commons.client.monitor.controller.TimeoutController;

/**
 * 
 * Concrete ClientTemplate for benchmark test.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-19
 */

public class IClientTemplate extends AbstractClientTemplate<IClient> implements IClient{

	private ClientConfig clientConfig;
	private ClientFactory<IClient> clientFactory;
	private String name;
	
	@Override
	public ClientConfig getClientConfig() {
		return clientConfig;
	}
	
	@Override
	public ClientFactory<IClient> getClientFactory() {
		return clientFactory;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean retryWhenException(Exception exception) {
		return exception instanceof ClientTimeoutException;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public void setClientFactory(ClientFactory<IClient> clientFactory) {
		this.clientFactory = clientFactory;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public long incr(final String key) {
		return execute(new ClientCallback<Long>("incr", key){
			@Override
			public Long doInClient() {
				return getClient().incr(key);
			}
		});
	}
	
	@Override
	public long mockIncAndSleep(final String key, final long sleepTimeMills) {
		Long rs = execute(new ClientCallback<Long>("incr", key) {
			@Override
			public Long doInClient(){
				return getClient().mockIncAndSleep(key, sleepTimeMills);
			}
		});
		return rs != null ? rs : -1;
	}
	
	public static IClientTemplate mockCLientTemplate(){
		IClientTemplate iClientTemplate;
		try {
			iClientTemplate = new IClientTemplate();
			iClientTemplate.setClientFactory(new IClientFactory());
			iClientTemplate.setName("itest_client_templat");
			
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setRetries(2);
			clientConfig.setTimeoutMills(200);
			iClientTemplate.setClientConfig(clientConfig);
			
			TimeoutController clientController = new TimeoutController();
			ClientMonitorImpl clientMonitor = new ClientMonitorImpl();
			clientMonitor.setClientController(clientController);
			iClientTemplate.setClientMonitor(clientMonitor);
			
			clientMonitor.afterPropertiesSet();
			clientController.afterPropertiesSet();
			
			return iClientTemplate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
