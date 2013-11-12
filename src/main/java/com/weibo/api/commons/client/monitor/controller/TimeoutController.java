package com.weibo.api.commons.client.monitor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import cn.sina.api.commons.util.SystemTimer;

import com.weibo.api.commons.client.monitor.MonitorEventBuilder;
import com.weibo.api.commons.client.util.ClientConstants;
import com.weibo.api.commons.client.util.ClientExecutorUtil;

/**
 * 
 * Control the client operate to avoid timeout.
 * 由于client thread基本都是复用的，所以考虑一个优化是map缓存所有的client thread，然后deamon 线程去监测thread是否执行了operate、是否timeout。
 * 这样可以降低了client执行时的性能损耗，因为大部分操作不用实际的put/reove操作，但deamon threads每次轮询需要分析所有client threads。
 * 通过压测发现，对于1000个元素的ConcurrentHashMap，put+remove 与 轮询相比，前者的性能比后者高近2个数量级。
 * 所以此处逻辑最终定位：client执行前后对map进行put+remove操作。
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-17
 */
@Component("timeoutController")
public class TimeoutController implements ClientController, InitializingBean{

	private static Logger log = LoggerFactory.getLogger(TimeoutController.class);
	
	private ConcurrentHashMap<Thread, MonitorEventBuilder> allClientThreads = new ConcurrentHashMap<Thread, MonitorEventBuilder>(); 
	
	/**
	 * init action
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception  {
		ClientExecutorUtil.getMonitorScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				try {
					checkTimeout();
				} catch (Exception e) {
					log.warn("Exception in commonClient.TimeoutController", e);
				}
			}
		}, ClientConstants.MONITOR_SCHEDULED_TASK_INITIAL_DELAY_MILLS, ClientConstants.MONITOR_CHECK_TIMEOUT_INTERVAL_MILLS, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 记录下client thread及其线程关联的monitor event，deamon 线程进行轮训监测
	 */
	@Override
	public void onProcessStart(Thread clientThread, MonitorEventBuilder monitorEvent) {
		allClientThreads.put(clientThread, monitorEvent);
	}
	
	@Override
	public void onProcessCompleted(Thread clientThread) {
		allClientThreads.remove(clientThread);
	}
	
	/**
	 * check if the client operate is timeout, interrput the client thread if it is timeout.
	 */
	private void checkTimeout(){
		if(allClientThreads.size() == 0){
			return;
		}
		if(log.isDebugEnabled()){
			log.debug("Check if the clients operate timeout, size=" + allClientThreads.size());
		}
		
		MonitorEventBuilder event = null;
		List<Thread> timeoutThreads = new ArrayList<Thread>();
		
		for(Thread t : allClientThreads.keySet()){
			event = allClientThreads.get(t);
			if(event == null || event.getStartTimeMills() < 1){
				continue;
			}
			
			if(event.isTimeout()){
				timeoutThreads.add(t);
			}
		}
		for(Thread t : timeoutThreads){
			event = allClientThreads.get(t);
			if(event == null){
				continue;
			}
			
			if(event.isTimeout()){
				try {
					allClientThreads.remove(t);
					log.warn(String.format("Client operates timeout, event=%s, usedTime=%s", event, (SystemTimer.currentTimeMillis() - event.getStartTimeMills())));
					t.interrupt();
				} catch (Exception e) {
					log.warn("Exception when close thread with event:" + event);
				}
			}
		}
	}
}
