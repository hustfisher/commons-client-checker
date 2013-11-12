package com.weibo.api.commons.client.util;

/**
 * 
 * Constants for client framework.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-17
 */

public class ClientConstants {

	/** client 配置的最小超时时间。 */
	public static int TIMEOUT_THRESHOLD_MILLS = 100;
	
	public static int THREADS_MONITOR_SCHEDULE_EXECUTOR = 2;
	public static int THREADS_MONIROT_EXECUTOR = 5;
	
	public static int SECOND = 1000;
	
	/** monitor stat log interval in second */
	public static int MONITOR_STAT_PRINT_INTERVAL_MILLS = 60 * SECOND;
	
	/** 轮询监测client operate 超时的时间间隔 */
	public static int MONITOR_CHECK_TIMEOUT_INTERVAL_MILLS = TIMEOUT_THRESHOLD_MILLS;
	
	//FIXME: 测试完毕后需要改为60s
	public static int MONITOR_SCHEDULED_TASK_INITIAL_DELAY_MILLS = 1 * SECOND;
	
	public static String SEPERATOR_FOR_MULTI_KEYS = "|";
}
