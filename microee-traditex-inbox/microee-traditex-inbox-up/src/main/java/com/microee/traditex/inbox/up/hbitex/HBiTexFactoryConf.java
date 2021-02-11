package com.microee.traditex.inbox.up.hbitex;

import com.microee.plugin.http.assets.HttpClientLogger;

public class HBiTexFactoryConf {

	public final String title;
	public final String connid;
	public final String uid;
	public final String wshost;
	public final String exchangeCode;
	public final String threadName;
	public final HttpClientLogger logger;

	public HBiTexFactoryConf(
			String title, String connid, String uid, String wshost, String exchangeCode, String threadName, HttpClientLogger logger) {
		super();
		this.title = title;
		this.connid = connid;
		this.uid = uid;
		this.wshost = wshost;
		this.exchangeCode = exchangeCode;
		this.threadName = threadName;
		this.logger = logger;
	}
	
}
