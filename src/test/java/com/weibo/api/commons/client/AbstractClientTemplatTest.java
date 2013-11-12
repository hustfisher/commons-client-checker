package com.weibo.api.commons.client;

import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.weibo.api.commons.client.AbstractClientTemplate;
import com.weibo.api.commons.client.ClientFactory;
import com.weibo.api.commons.client.config.ClientConfig;
import com.weibo.api.commons.client.monitor.ClientMonitor;

/**
 * 
 * test clientTemplate
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-18
 */

public class AbstractClientTemplatTest extends BaseTest {

	private AbstractClientTemplate<Object> clientTemplate = null;
	
	private ClientMonitor clientMonitor = mockery.mock(ClientMonitor.class);
	
	@Before
	@SuppressWarnings("unchecked")
	public void init(){
		
		clientTemplate = new AbstractClientTemplate<Object>() {
			private ClientFactory<Object> cf = mockery.mock(ClientFactory.class);
			@Override
			public ClientConfig getClientConfig() {
				ClientConfig config = new ClientConfig();
				config.setRetries(2);
				config.setTimeoutMills(200);
				return config;
			}
			@Override
			public ClientFactory<Object> getClientFactory() {
				return this.cf;
			}
			
			@Override
			public boolean retryWhenException(Exception exception) {
				return false;
			}
			
			@Override
			public void setName(String name) {
				this.name = "test_client_template";
			}
			
			@Override
			public void setClientFactory(ClientFactory<Object> clientFactory) {
				this.cf = clientFactory;
			}
			
		};
		clientTemplate.setClientMonitor(clientMonitor);
	}
	
	@Test
	public void testProtocol(){
		final String commandName = "get";
		final String key = "key_1";
		final long value = 1;
		
		mockery.checking(new Expectations(){{
			one(clientMonitor).notifyBeforeProcess(clientTemplate.getName(), commandName, key, clientTemplate.getClientConfig().getTimeoutMills());
			one(clientMonitor).notifyAfterProcess(true);	
		}});
		
		long rs = clientTemplate.execute(new ClientCallback<Long>(commandName, key) {
			@Override
			public Long doInClient() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return value;
			}
		});
		
		Assert.assertEquals(value, rs);
	}
}
