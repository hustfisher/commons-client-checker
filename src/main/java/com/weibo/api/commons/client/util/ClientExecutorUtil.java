package com.weibo.api.commons.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.weibo.api.commons.client.monitor.listener.StatListener;

/**
 * 
 * Supply executors for client frame.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-17
 */

public class ClientExecutorUtil {

	private static ScheduledThreadPoolExecutor scheduleExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(ClientConstants.THREADS_MONITOR_SCHEDULE_EXECUTOR);
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(ClientConstants.THREADS_MONIROT_EXECUTOR);
	
	static{
		StatListener.register("common-client-executor", executor);
		StatListener.register("common-client-schedule-executor", scheduleExecutor);
	}
	
	/**
	 * get scheduled executor for monitor
	 * @return
	 */
	public static ScheduledExecutorService getMonitorScheduledExecutor(){
		return scheduleExecutor;
	}
	
	/**
	 * get executor for monitor
	 * @return
	 */
	public static ExecutorService getMonitorExecutor(){
		return executor;
	}
}
