package com.weibo.api.commons.client.monitor;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sina.api.commons.util.SystemTimer;

/**
 * 
 * Used for record a operation of client, the lifecycle is withReset-->withInit-->withComplete
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-15
 */

public class MonitorEventBuilder implements Cloneable{

	private static Logger log = LoggerFactory.getLogger(MonitorEventBuilder.class);
	
	private static boolean DEFAULT_RESULT_FLAG = false; 
	
	private String clientName;
	private String commandName;
	private String key;
	private int timeoutMills;
	
	/** Use SystemTimer to caculate the consume time, the max deviation is 10 mills */
	private long startTimeMills;
	
	/** Use SystemTimer to caculate the consume time, the max deviation is 10 mills */
	private long consumeTimeInMills;
	
	private boolean succeed = DEFAULT_RESULT_FLAG;
	
	public MonitorEventBuilder(){}
	
	public MonitorEventBuilder withInit(String clientName, String commandName, String key, int timeoutMills){
		this.clientName = clientName;
		this.commandName = commandName;
		this.key = key;
		this.timeoutMills = timeoutMills;
		
		this.startTimeMills = SystemTimer.currentTimeMillis();
		
		return this;
	}
	
	/**
	 * Invoke when the client operation is completed, no matter succeed or fail.
	 * @param succeed
	 */
	public MonitorEventBuilder withComplete(boolean succeed){
		consumeTimeInMills = SystemTimer.currentTimeMillis() - startTimeMills;
		this.succeed = succeed;
		return this;
	}
	
	/**
	 * Reset the event properties for reusing.
	 * FIXME：如果字段发生变化，需要同步变更此方法
	 */
	public MonitorEventBuilder withReset(){
		this.clientName = null;
		this.commandName = null;
		this.key = null;
		this.startTimeMills = 0L;
		this.consumeTimeInMills = 0L;
		this.succeed = DEFAULT_RESULT_FLAG;
		this.timeoutMills = 0;
		
		return this;
	}
	
	public boolean isTimeout(){
		if(startTimeMills <= 0){
			return false;
		}
		if(SystemTimer.currentTimeMillis() - startTimeMills > timeoutMills){
			return true;
		}
		return false;
	}
	
	/**
	 * clone a same event to listener.
	 */
	@Override
	protected MonitorEventBuilder clone(){
		try {
			return (MonitorEventBuilder)super.clone();
		} catch (CloneNotSupportedException e) {
			log.warn("Clone MonitorEventBuilder false:" + toString(), e);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(48).append("[")
			.append("client=").append(clientName)
			.append(", cmd=").append(commandName)
			.append(", key=").append(key)
			.append(", startTime=").append(startTimeMills)
			.append(", timeout=").append(timeoutMills)
			.append(", ctime=").append(consumeTimeInMills)
			.append(", succeed=").append(succeed)
			.toString();
	}
	
	@Override
	public int hashCode() {
		int hash = ObjectUtils.hashCode(clientName);
		hash = 31 * hash + ObjectUtils.hashCode(commandName);
		hash = 31 * hash + ObjectUtils.hashCode(key);
		hash = 31 * hash + ObjectUtils.hashCode(timeoutMills);
		hash = 31 * hash + ObjectUtils.hashCode(startTimeMills);
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		
		if(obj != null && obj instanceof MonitorEventBuilder){
			MonitorEventBuilder objEvent = (MonitorEventBuilder)obj;
			return ObjectUtils.equals(this.clientName, objEvent.clientName)
					&& ObjectUtils.equals(this.commandName, objEvent.commandName)
					&& ObjectUtils.equals(this.key, objEvent.key)
					&& ObjectUtils.equals(this.timeoutMills, objEvent.timeoutMills)
					&& ObjectUtils.equals(this.startTimeMills, objEvent.startTimeMills);
		}
		return false;
	}
	
	/**================================ getters & setters ================================*/

	public String getClientName() {
		return clientName;
	}
	
	public String getCommandName() {
		return commandName;
	}

	public String getKey() {
		return key;
	}

	public long getStartTimeMills() {
		return startTimeMills;
	}

	public static boolean isDEFAULT_RESULT_FLAG() {
		return DEFAULT_RESULT_FLAG;
	}

	public long getConsumeTimeInMills() {
		return consumeTimeInMills;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public int getTimeoutMills() {
		return timeoutMills;
	}

	public void setTimeoutMills(int timeoutMills) {
		this.timeoutMills = timeoutMills;
	}
	
}
