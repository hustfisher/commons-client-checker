package com.weibo.api.commons.client;

import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * base test
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-18
 */

public class BaseTest {

	public JUnit4Mockery mockery = new JUnit4Mockery(){{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	
	@Test
	public void testCheckMockery(){
		Assert.assertNotNull(mockery);
	}
}
