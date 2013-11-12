package com.weibo.api.commons.client.monitor;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.weibo.api.commons.client.BaseTest;
import com.weibo.api.commons.client.monitor.controller.ClientController;
import com.weibo.api.commons.client.monitor.listener.ClientListener;

/**
 * 
 * ClientMonitorImpl unit test.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-18
 */

public class ClientMonitorTest extends BaseTest{

	private ClientMonitorImpl clientMonitor = new ClientMonitorImpl();
	
	private ClientListener listerner1 = mockery.mock(ClientListener.class, "listener1");
	private ClientListener listerner2 = mockery.mock(ClientListener.class, "listener2");
	
	private ClientController controller = mockery.mock(ClientController.class);
	
	@Before
	public void init(){
		clientMonitor.register(listerner1);
		clientMonitor.register(listerner2);
		
		clientMonitor.setClientController(controller);
	}
	
	@Test
	public void testNotifyBeforeProcess(){
		String clientName = "test_template";
		String commandName = "get";
		String key = "key_1";
		int timeoutMills = 200;
		
		mockery.checking(new Expectations(){{
			one(controller).onProcessStart(Thread.currentThread(), ClientMonitorImpl.eventLocal.get());
		}});
		clientMonitor.notifyBeforeProcess(clientName, commandName, key, timeoutMills);
	}
	
	@Test
	public void testNotifyAfterProcess() throws Exception{
		testNotifyBeforeProcess();
		
		final MonitorEventBuilder eventClone = ClientMonitorImpl.eventLocal.get().clone();
		mockery.checking(new Expectations(){{
			one(listerner1).onProcessComplete(eventClone);
			one(listerner2).onProcessComplete(eventClone);
			oneOf(controller).onProcessCompleted(Thread.currentThread());
		}});
		clientMonitor.notifyAfterProcess(true);
	}
	
}
