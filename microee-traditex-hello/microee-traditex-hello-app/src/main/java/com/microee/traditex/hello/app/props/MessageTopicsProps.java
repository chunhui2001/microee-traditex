package com.microee.traditex.hello.app.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTopicsProps {

    @Value("${topics.inbox.orderbook}")
    private String orderBookTopic;
    
    @Value("${topics.inbox.kline.event}")
    private String klineEventTopic;
    
    public MessageTopicsProps() {
    	
    }

	public String getOrderBookTopic() {
		return orderBookTopic;
	}

	public void setOrderBookTopic(String orderBookTopic) {
		this.orderBookTopic = orderBookTopic;
	}

	public String getKlineEventTopic() {
		return klineEventTopic;
	}

	public void setKlineEventTopic(String klineEventTopic) {
		this.klineEventTopic = klineEventTopic;
	}
    
}
