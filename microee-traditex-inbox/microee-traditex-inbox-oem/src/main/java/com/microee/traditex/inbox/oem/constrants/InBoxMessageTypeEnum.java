package com.microee.traditex.inbox.oem.constrants;

public enum InBoxMessageTypeEnum {

	// @formatter:off
	SHUTDOWN ("shutdown"),
	ERROR ("error"),
	QUOTE ("quote"),
	CONNECTED ("connected"),
	DISCONNECTED ("disconnected"),
	TIMEOUT ("timeout"),
	PRICE_STREAM ("pricing-stream"),
	PING ("ping"),
	FAILED ("failed"),
	SUBBED ("subbed"),
	UNSUB ("unsub"),
	UNSUBBED ("unsubbed"),
	ACCOUNT_CHANGE ("account-change"),
	ORDER_STATS ("order-stats"),
	UNKNOW ("unknow"),
	AUTH ("auth"),
	KLINE ("kline");
	// @formatter:on
	
	public final String code;
	
	InBoxMessageTypeEnum(String code) {
		this.code = code;
	}

}
