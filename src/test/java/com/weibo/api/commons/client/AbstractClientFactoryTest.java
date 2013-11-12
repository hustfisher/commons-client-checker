package com.weibo.api.commons.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.weibo.api.commons.client.ClientFactory.ServerStatus;
import com.weibo.api.commons.client.exception.ServerUnavailableException;



/**
 * 
 * AbstractClientFactory test.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-18
 */

public class AbstractClientFactoryTest extends BaseTest{

	private AbstractClientFactory<Object> clientFactory = null;
	private Object client = new Object();
	private Object mockClient = new Object();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void init(){
		clientFactory = new AbstractClientFactory<Object>() {
			
			private ServerStatus serverStatus;
			
			@Override
			public void updateServerStatus(ServerStatus serverStatus) {
				this.serverStatus = serverStatus;
			}
			
			@Override
			public ServerStatus getServerStatus() {
				return serverStatus;
			}
			
			@Override
			protected Object doGetMockClient() {
				return mockClient;
			}
			
			@Override
			protected Object doGetClient() {
				return client;
			}
		};
	}
	
	@Test
	public void getClient(){
		Assert.assertEquals(client, clientFactory.getClient());
		
		clientFactory.updateServerStatus(ServerStatus.mocking);
		Assert.assertEquals(mockClient, clientFactory.getClient());
		
		clientFactory.updateServerStatus(ServerStatus.running);
		Assert.assertEquals(client, clientFactory.getClient());
		
		expectedException.expect(ServerUnavailableException.class);
		clientFactory.updateServerStatus(ServerStatus.unavailable);
		clientFactory.getClient();
		
	}
}
