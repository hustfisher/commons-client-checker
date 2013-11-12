package com.weibo.api.commons.client;

/**
 * 
 * Generic abstract callback class for code that operates with commands of query or update. 
 * Allows to execute any number of operations on a single invoke. 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public abstract class ClientCallback<T> {

	private String cmdName;
	private String key;
	
	public ClientCallback(String cmdName, String key){
		this.cmdName = cmdName;
		this.key = key;
	}
	
	/**
	 * get the command name
	 * @return
	 */
	public String getCmdName() {
		return cmdName;
	}
	
	/**
	 * get the key
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets called by #ClientTemplate.Donnot need to care about the status of the server connected by the clientTemplate, or handling exceptions.
	 * @return
	 */
	public abstract T doInClient();
}
