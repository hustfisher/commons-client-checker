package com.weibo.api.commons.client.monitor.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.weibo.api.commons.client.monitor.MonitorEventBuilder;
import com.weibo.api.commons.client.util.ClientConstants;
import com.weibo.api.commons.client.util.ClientExecutorUtil;

/**
 * 
 * Stat client operation.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */
@Component("statListener")
public class StatListener implements ClientListener, InitializingBean{

	private static Logger log = LoggerFactory.getLogger(StatListener.class);
	
	private static ConcurrentHashMap<String, CountTimePair> allTimeStat = new ConcurrentHashMap<String, StatListener.CountTimePair>();
	private static ConcurrentHashMap<String, CountTimePair> last5MinutesTimeStat = new ConcurrentHashMap<String, StatListener.CountTimePair>();
	private static ConcurrentHashMap<String, CountTimePair> lastMinuteTimeStat = new ConcurrentHashMap<String, StatListener.CountTimePair>();
	
	private static Map<String, ThreadPoolExecutor> executors = new ConcurrentHashMap<String, ThreadPoolExecutor>();
	
	public static void register(String executorName, ThreadPoolExecutor executor){
		executors.put(executorName, executor);
	}
	
	/**
	 * init action
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception{
		ClientExecutorUtil.getMonitorScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					printStat();
				} catch (Exception e) {
					log.warn("Exception in StatListener.printStat", e);
				}
			}
		}, ClientConstants.MONITOR_SCHEDULED_TASK_INITIAL_DELAY_MILLS, ClientConstants.MONITOR_STAT_PRINT_INTERVAL_MILLS, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void onProcessComplete(MonitorEventBuilder eventClone) {
		if(eventClone == null){
			throw new NullPointerException("MonitorEventBuilder is null");
		}
		
		inc(allTimeStat, eventClone);
		inc(last5MinutesTimeStat, eventClone);
		inc(lastMinuteTimeStat, eventClone);
	}
	
	private void inc(ConcurrentHashMap<String, CountTimePair> countTimes, MonitorEventBuilder event){
		CountTimePair ctp = countTimes.get(event.getClientName());
		if(ctp == null){
			countTimes.putIfAbsent(event.getClientName(), new CountTimePair());
			ctp = countTimes.get(event.getClientName());
		}
		ctp.inc(1, event.getConsumeTimeInMills());
	}
	
	private static void printStat(){
		if(!log.isInfoEnabled()){
			return;
		}
		StringBuilder sb = new StringBuilder(128)
			.append("===========================")
			.append(memoryReport()).append(SystemUtils.LINE_SEPARATOR)
			.append("[ commonKeys - ").append(SystemUtils.LINE_SEPARATOR);
		
		CountTimePair allCounter, last5MinutesCounter, lastMinuteCounter;
		List<String> allKeys = new ArrayList<String>(allTimeStat.keySet());
		Collections.sort(allKeys);
		for(String key : allKeys){
			
			lastMinuteCounter = lastMinuteTimeStat.get(key);
			last5MinutesCounter = last5MinutesTimeStat.get(key);
			allCounter = allTimeStat.get(key);
			
			sb.append(key)
				.append(": {")
				.append(lastMinuteCounter.count).append(", ")
				.append(last5MinutesCounter.count).append(", ")
				.append(allCounter.count)
				.append("}, {")
				.append(getAvgCount(lastMinuteCounter)).append(", ")
				.append(getAvgCount(last5MinutesCounter)).append(", ")
				.append(getAvgCount(allCounter))
				.append("}").append(SystemUtils.LINE_SEPARATOR);
		}
		sb.append("]").append(SystemUtils.LINE_SEPARATOR);
		
		sb.append("[ executors - ").append(SystemUtils.LINE_SEPARATOR);
		for(String executorName : executors.keySet()){
			sb.append(statExecutor(executorName, executors.get(executorName)));
		}
		sb.append("]").append(SystemUtils.LINE_SEPARATOR);
		
		log.info(sb.toString());
	}
	
	private static long getAvgCount(CountTimePair pair){
		if(pair.timeMills.get() == 0){
			return 0;
		}
		return (long)(pair.count.get() / (pair.timeMills.get() / 1000.0));
	}
	
	private static String memoryReport(){
		
		double maxMemory = (double)Runtime.getRuntime().maxMemory() / (1024 * 1024);
		double totalMemory = (double)Runtime.getRuntime().totalMemory() / (1024 * 1024);
		double freeMemory = (double)Runtime.getRuntime().freeMemory() / (1024 * 1024);
		double usedMemeory = totalMemory - freeMemory;
		double usedMemoryPercent = usedMemeory / maxMemory * 100.0;
		
		
		if(usedMemoryPercent > 80){
			log.error(String.format("Detected OutOfMemory potentia, usedMemory=%s, be careful !!!!!!", usedMemoryPercent));
		}
		
		DecimalFormat memoryFormat = new DecimalFormat("#0.00");
		DecimalFormat percentFormat = new DecimalFormat("#");
		
		return new StringBuilder(32)
			.append(memoryFormat.format(usedMemeory)).append(" MB of")
			.append(memoryFormat.format(maxMemory)).append(" MB (")
			.append(percentFormat.format(usedMemoryPercent)).append("%)")
			.toString();
	}
	
	private static class CountTimePair{
		private AtomicLong count;
		private AtomicLong timeMills;
		
		public CountTimePair(){
			count = new AtomicLong();
			timeMills = new AtomicLong();
		}
		
		public void inc(int incCount, long incTimeMills){
			count.getAndAdd(incCount);
			timeMills.getAndAdd(incTimeMills);
		}
	}
	
	private static String statExecutor(String name, ThreadPoolExecutor executor){
		StringBuilder strBuf = new StringBuilder(32);
		strBuf.append(name).append("{").append(executor.getQueue().size()).append(",")
			.append(executor.getCompletedTaskCount()).append(",")
			.append(executor.getTaskCount()).append(",")
			.append(executor.getActiveCount()).append(",")
			.append(executor.getCorePoolSize()).append("}\n");
		return strBuf.toString();
	}
}
