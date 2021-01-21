package com.microee.traditex.liquid.app.message;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class LiquidRedisMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidRedisMessageListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String _topic = new String(message.getChannel()); 
        String _message = new String(message.getBody(), StandardCharsets.UTF_8); 
        LOGGER.info("消费了一条redis消息: topic={}, message={}", _topic, _message);
    }
    
}
