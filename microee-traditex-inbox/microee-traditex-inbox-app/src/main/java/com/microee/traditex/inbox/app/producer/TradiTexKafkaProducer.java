package com.microee.traditex.inbox.app.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microee.plugin.http.assets.HttpAssets;
import com.microee.stacks.kafka.support.KafkaStringProducer;
import com.microee.traditex.inbox.app.props.MessageTopicsProps;
import com.microee.traditex.inbox.up.InBoxMessage.Message;

@Component
public class TradiTexKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(TradiTexKafkaProducer.class);
    
    @Autowired(required=false)
    private KafkaStringProducer kafkaStringProducer;

    @Autowired(required=false)
    private MessageTopicsProps messageTopicsProps;


    /**
     * 广播连接建立成功事件
     * @param message
     */
    public void subscribeEventBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-subscribeEventBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getSubscribeEventTopic(), HttpAssets.toJsonString(message));
    }
    
    /**
     * 广播连接建立成功事件
     * @param message
     */
    public void connectedBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-connectedBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getConnectedTopic(), HttpAssets.toJsonString(message));
    }

    /**
     * 广播外汇价格变动事件
     * @param message
     */
    public void pricingBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-pricingBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getPricingTopic(), HttpAssets.toJsonString(message));
    }
    
    /**
     * 广播 orderbook
     * 
     * @param message
     */
    public void orderBookBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-orderBookBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getOrderBookTopic(), HttpAssets.toJsonString(message));
    }
    
    /**
     * 广播 account balance change
     * 
     * @param message
     */
    public void balanceBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-balanceBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getBanalceTopic(), HttpAssets.toJsonString(message));
    }
    
    /**
     * 广播 order state change
     * 
     * @param message
     */
    public void orderStateBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-orderStateBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getOrderStatTopic(), HttpAssets.toJsonString(message));
    }
    
    // 广播鉴权事件
    public void authEventBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-authEventBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getAuthEventTopic(), HttpAssets.toJsonString(message));
    }

    // 广播连接关闭事件
    public void shutdownBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-shutdownBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getConnectShutDownEventTopic(), HttpAssets.toJsonString(message));
    }

	public void klineBroadcase(Message message) {
    	if (kafkaStringProducer == null) {
    		logger.warn("收到消息未订阅-klineBroadcase: message={}", HttpAssets.toJsonString(message));
    		return;
    	}
        kafkaStringProducer.sendMessage(messageTopicsProps.getKlineEventTopic(), HttpAssets.toJsonString(message));
	}
    
}
