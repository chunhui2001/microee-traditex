package com.microee.traditex.inbox.app.components;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microee.traditex.inbox.app.producer.TradiTexKafkaProducer;
import com.microee.traditex.inbox.oem.connector.TradiTexConnection;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.InBoxMessage.Message;

@Component
public class CombineMessage implements CombineMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CombineMessage.class);

    @Autowired
    private TradiTexConnectComponent connectionComponent;

    @Autowired
    private HBiTexConnectRetryer retryerComponent;

    @Autowired
    private TradiTexKafkaProducer kafkaProducer;

    @Override
    public void onConnected(String vender, String connid, Message message) {
        connectionComponent.putEvent(connid, "connected", Instant.now().toEpochMilli());
        kafkaProducer.connectedBroadcase(message); 
    }
    
    @Override
    public void onDisconnected(String vender, String connid, Message message, Long receiveTime) { 
        connectionComponent.putEvent(connid, "disconnected", receiveTime);
        this.onFailed(vender, connid, message, receiveTime);
    }

    @Override
    public void onAuthMessage(String vender, String connid, Message message, Long receiveTime) {
    	connectionComponent.putEvent(connid, "authed", receiveTime);
        logger.info("onAuthMessage, 鉴权: vender={}, connid={}, message={}", vender, connid, message);
        kafkaProducer.authEventBroadcase(message);
    }

    @Override
    public void onFailed(String vender, String connid, Message message, Long eventTime) {
    	TradiTexConnection<?> connection = connectionComponent.putEvent(connid, "failed", eventTime);
    	if (connection != null) {
            logger.info("onFailed, 连接失败, {} 秒后自动重连: vender={}, status={}, message={}", HBiTexConnectRetryer.RECONNECT_TIME_SEC, vender, connection.getFactory().status(), message);
            retryerComponent.add(connection); // 重新连接
    	}
    }

    @Override
    public void onTimeout(String vender, String connid, Message message, Long eventTime, Long start, Long end) {
    	connectionComponent.putEvent(connid, "timeout", eventTime);
        logger.info("onTimeout, 连接超时: vender={}, connid={}, message={}", vender, connid, message);
    }

    @Override
    public void onPingMessage(String vender, String connid, Message message, Long receiveTime) {
        connectionComponent.putEvent(connid, "ping", receiveTime);
    }

    @Override
    public void onOrderBookMessage(String vender, String connid, Message message, Long time0) {
        connectionComponent.putEvent(connid, "orderbook", time0);
        if (logger.isDebugEnabled()) {
            logger.debug("orderbook={}", message.toString());
        }
        kafkaProducer.orderBookBroadcase(message);
    }

    @Override
    public void onSubbedMessage(String vender, String connid, Message message, String topic, Long receiveTime) {
        connectionComponent.get(connid).getFactory().addTopic(topic); 
        connectionComponent.putEvent(connid, "subscribe", receiveTime);
        kafkaProducer.subscribeEventBroadcase(message);
        logger.info("onSubbedMessage: vender={}, message={}", vender, message);
    }

    @Override
    public void onUnSubbedMessage(String vender, String connid, Message message, String topic) { 
        connectionComponent.get(connid).getFactory().removeTopics(topic);
        connectionComponent.putEvent(connid, "unsubscribe", Instant.now().toEpochMilli());
        logger.info("onUnSubbedMessage: vender={}, message={}", vender, message);
    }

    @Override
    public void onAccountMessage(String vender, String connid, Message message, Long receiveTime) {
        kafkaProducer.balanceBroadcase(message);
        connectionComponent.putEvent(connid, "account-changed", receiveTime);
    }

    @Override
    public void onOrderStatMessage(String vender, String connid, Message message, Long receiveTime) {
        kafkaProducer.orderStateBroadcase(message);
    }

    @Override
    public void onErrorMessage(String vender, String connid, Message message, Long receiveTime) {
        logger.info("onErrorMessage: vender={}, message={}", vender, message);
        connectionComponent.putEvent(connid, "error", receiveTime);
    }

    @Override
    public void onPricingSteamMessage(String vender, String connid, Message message,
            Long receiveTime) {
        //@formatter:off
        // {"timeA":1599895997116,"timeB":1599895997117,"closeoutBid":"106.146","closeoutAsk":"106.191","instrument":"USD_JPY","type":"PRICE","vender":"Oanda","asks":[{"price":"106.183","liquidity":250000}],"bids":[{"price":"106.154","liquidity":250000}],"tradeable":false,"connid":"lzWGEKzzGxg","time":"1599859034.787235733","event":"pricing-stream","status":"non-tradeable"}
        //@formatter:on
        kafkaProducer.pricingBroadcase(message);
        connectionComponent.putEvent(connid, "pricing-stream", receiveTime);
    }

	@Override
	public void onKLineMessage(String name, String connid, Message message, Long receiveTime) {
        connectionComponent.putEvent(connid, "kline", receiveTime);
        kafkaProducer.klineBroadcase(message); 
	}

    // 服务重启时会触发该事件
    public void onShutDownEvent(String connid, Message message, long eventTime) {
        kafkaProducer.shutdownBroadcase(message);
    }
    
}
