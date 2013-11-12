package com.weibo.api.commons.client.monitor;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.weibo.api.commons.client.monitor.controller.ClientController;
import com.weibo.api.commons.client.monitor.listener.ClientListener;
import com.weibo.api.commons.client.util.ClientExecutorUtil;

/**
 * 
 * To receive notification and broadcast to listeners.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-15
 */
@Component("clientMonitor")
public class ClientMonitorImpl implements ClientMonitor, InitializingBean{

	private static Logger log = LoggerFactory.getLogger(ClientMonitor.class);
	
	private CopyOnWriteArrayList<ClientListener> clientListeners = new CopyOnWriteArrayList<ClientListener>();
	
	@Resource(name="timeoutController")
	private ClientController clientController;
	
	@Resource(name="accessListener")
	private ClientListener accessListener;
	
	@Resource(name="statListener")
	private ClientListener statListener;
	
	protected static ThreadLocal<MonitorEventBuilder> eventLocal = new ThreadLocal<MonitorEventBuilder>(){
		@Override
		protected MonitorEventBuilder initialValue() {
			return new MonitorEventBuilder();
		}
	};
	
	/**
	 * load default client listeners.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		register(accessListener);
		register(statListener);
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void register(ClientListener clientListener) {
		if(clientListener == null){
			return;
		}
		if(!clientListeners.contains(clientListener)){
			clientListeners.add(clientListener);
		}
		log.info("Load client listerner: " + clientListener.getClass().getSimpleName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyBeforeProcess(String clientName, String commandName, String key, int timeoutMills) {
		eventLocal.get()
			.withReset()
			.withInit(clientName, commandName, key, timeoutMills);
		
		//notify controller in-time
		clientController.onProcessStart(Thread.currentThread(), eventLocal.get());
	}
	
	/**
	 * {@inheritDoc}
	 * 异步执行，避免client请求的性能损耗
	 */
	@Override
	public void notifyAfterProcess(boolean succeed) {
		//clone and reset
		final MonitorEventBuilder eventClone = eventLocal.get().withComplete(succeed).clone();
		//notify controller
		eventLocal.get().withReset();
		
		//notify listeners
		ClientExecutorUtil.getMonitorExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					for(ClientListener listener : clientListeners){
						listener.onProcessComplete(eventClone);
					}
				} catch (Exception e) {
					log.warn("Exception in notifyAfterProcess", e);
				}
			}
		});
		clientController.onProcessCompleted(Thread.currentThread());
	}

	public void setClientController(ClientController clientController) {
		this.clientController = clientController;
	}

	
}
