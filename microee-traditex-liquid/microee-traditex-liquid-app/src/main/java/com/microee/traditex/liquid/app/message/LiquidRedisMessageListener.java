package com.microee.traditex.liquid.app.message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.microee.stacks.es6.supports.ElasticSearchSaveSupport;

@Component
public class LiquidRedisMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidRedisMessageListener.class);
    
    @Autowired
    private ElasticSearchSaveSupport elasticSearchSave;
    
    static ThreadLocal<SimpleDateFormat> format1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd");
        }
    };

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String _topic = new String(message.getChannel()); 
        String _message = new String(message.getBody(), StandardCharsets.UTF_8); 
        try {
            String type = "api-monitor-sla";
            String index = type + "-" + format1.get().format(new Date(Instant.now().toEpochMilli()));
            JSONObject _messageObject = new JSONObject(_message);
            _messageObject.remove("headers");
            String newId = elasticSearchSave.save(type, index, _messageObject);
            LOGGER.info("保存到es: topic={}, message={}, type={}, index={}, newId={}", _topic, _message, type, index, newId);
        } catch (JSONException | IOException e) {
            LOGGER.error("errorMessage={}", e.getMessage(), e);
        }
    }
    
}
