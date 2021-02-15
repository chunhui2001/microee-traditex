package com.microee.traditex.inbox.app.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTopicsProps {

    @Value("${topics.inbox.orderbook}")
    private String orderBookTopic;
    
    @Value("${topics.inbox.connected}")
    private String connectedTopic;
    
    @Value("${topics.inbox.pricing}")
    private String pricingTopic;
    
    @Value("${topics.inbox.banalce}")
    private String banalceTopic;
    
    @Value("${topics.inbox.orderstat}")
    private String orderStatTopic;
    
    @Value("${topics.inbox.authevent}")
    private String authEventTopic;
    
    @Value("${topics.inbox.subscribe.event}")
    private String subscribeEventTopic;
    
    @Value("${topics.inbox.connect.shutdown.event}")
    private String connectShutDownEventTopic;
    
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

	public String getConnectedTopic() {
		return connectedTopic;
	}

	public void setConnectedTopic(String connectedTopic) {
		this.connectedTopic = connectedTopic;
	}

	public String getPricingTopic() {
		return pricingTopic;
	}

	public void setPricingTopic(String pricingTopic) {
		this.pricingTopic = pricingTopic;
	}

	public String getBanalceTopic() {
		return banalceTopic;
	}

	public void setBanalceTopic(String banalceTopic) {
		this.banalceTopic = banalceTopic;
	}

	public String getOrderStatTopic() {
		return orderStatTopic;
	}

	public void setOrderStatTopic(String orderStatTopic) {
		this.orderStatTopic = orderStatTopic;
	}

	public String getAuthEventTopic() {
		return authEventTopic;
	}

	public void setAuthEventTopic(String authEventTopic) {
		this.authEventTopic = authEventTopic;
	}

	public String getSubscribeEventTopic() {
		return subscribeEventTopic;
	}

	public void setSubscribeEventTopic(String subscribeEventTopic) {
		this.subscribeEventTopic = subscribeEventTopic;
	}

	public String getConnectShutDownEventTopic() {
		return connectShutDownEventTopic;
	}

	public void setConnectShutDownEventTopic(String connectShutDownEventTopic) {
		this.connectShutDownEventTopic = connectShutDownEventTopic;
	}

	public String getKlineEventTopic() {
		return klineEventTopic;
	}

	public void setKlineEventTopic(String klineEventTopic) {
		this.klineEventTopic = klineEventTopic;
	}
    
}
