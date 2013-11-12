package com.weibo.api.commons.client;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sina.api.commons.cache.MemcacheClient;

import com.weibo.api.commons.client.ClientFactory.ServerStatus;
import com.weibo.api.commons.client.config.ClientConfig;
import com.weibo.api.commons.client.exception.ClientQueryException;
import com.weibo.api.commons.client.exception.ServerUnavailableException;
import com.weibo.api.commons.client.monitor.ClientMonitor;

/**
 * 
 * Template for all kinds of client like mc-client、jedis、jdbctemplate、rpc-client and so on. 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public abstract class AbstractClientTemplate<T> implements ClientTemplate<T> {

	private static Logger log = LoggerFactory.getLogger(AbstractClientTemplate.class);
	
	@Resource(name="clientMonitor")
	private ClientMonitor clientMonitor;
	
	protected String name;
	
	protected static AtomicInteger clientTemplateId = new AtomicInteger(0); 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateServerStatus(ServerStatus serverStatus){
		this.getClientFactory().updateServerStatus(serverStatus);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getClient() {
		return getClientFactory().getClient();
	}
	
	@Override
	public String getName() {
		if(StringUtils.isBlank(name)){
			synchronized (clientTemplateId) {
				if(StringUtils.isBlank(name)){
					clientTemplateId.getAndIncrement();
					name = "clientTemplate_" + this.getClass().getSimpleName() + "_" + clientTemplateId.get();
				}
			}
		}
		return name;
	}
	
	public void setClientMonitor(ClientMonitor clientMonitor) {
		this.clientMonitor = clientMonitor;
	}
	
	public ClientMonitor getClientMonitor() {
		return clientMonitor;
	}

	public abstract void setName(String name);
	
	public abstract ClientConfig getClientConfig();

	public abstract ClientFactory<T> getClientFactory();
	
	public abstract void setClientFactory(ClientFactory<T> clientFactory);
	
	public abstract boolean retryWhenException(Exception exception);

	/**
	 * execute callback methods.
	 * 如果执行操作时，抛出一个可重试异常，则进行重试操作,否则直接继续上抛异常。
	 * @param callback
	 * @return
	 */
	protected <R> R execute(ClientCallback<R> callback){
		R result = null;
		boolean succeed = false;
		int tryCount = 0;
		
		while(tryCount++ <= getClientConfig().getRetries()){
			try {
				preProcess(callback);
				result = callback.doInClient();
				succeed = true;
				break;
			} catch (RuntimeException e) {
				//在遇到某些特殊异常时，可能需要进行重试
				if(retryWhenException(e)){
					log.warn(String.format("Exception in CommonClient.clientTemplate.execute, client:%s, key=%s, tryCount=%s, will_retry=%s", getName(), callback.getKey(), tryCount, (tryCount <= getClientConfig().getRetries())), e);
				}else{
					throw e;
				}
			} finally{
				postProcess(succeed);
			}
		}
		
		return result;
	}
	
	protected <R> void preProcess(ClientCallback<R> callback){
		clientMonitor.notifyBeforeProcess(getName(), callback.getCmdName(), callback.getKey(), getClientConfig().getTimeoutMills());
	}
	
	protected void postProcess(boolean succeed){
		clientMonitor.notifyAfterProcess(succeed);
	}
	
}
