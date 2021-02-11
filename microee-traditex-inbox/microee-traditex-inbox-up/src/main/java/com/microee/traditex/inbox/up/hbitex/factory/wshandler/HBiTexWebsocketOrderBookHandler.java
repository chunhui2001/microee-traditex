package com.microee.traditex.inbox.up.hbitex.factory.wshandler;

import java.time.Instant;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microee.plugin.zip.Zip;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.InBoxMessage;

import okhttp3.WebSocket;
import okio.ByteString;

public class HBiTexWebsocketOrderBookHandler extends HBiTexWebsocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(HBiTexWebsocketOrderBookHandler.class);
    
	public HBiTexWebsocketOrderBookHandler(String connid, CombineMessageListener combineMessageListener) {
		super(connid, combineMessageListener);
	}
    
    @Override
    public void onMessageStringHandler(WebSocket webSocket, String text) {
        logger.info("onMessageStringHandler, text={}", text);
    }

    @Override
    public void onMessageByteStringHandler(WebSocket webSocket, ByteString bytes) {
        String jsonMessage = Zip.ungzip(bytes.toByteArray());
        Long receiveTime = Instant.now().toEpochMilli();
        JSONObject _times = new JSONObject();
        _times.put("timeA", receiveTime); // 收到时间
        JSONObject jsonObject = new JSONObject(jsonMessage);
        jsonObject.put("vender", _VENDER);
        if (jsonObject.has("ping")) {
            this.writeMessageWithoutLog(String.format("{\"pong\": %s}", jsonObject.getLong("ping")));
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onPingMessage(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.PING, jsonObject, _times), receiveTime);
            return;
        }
        if (jsonObject.has("ch") && jsonObject.has("tick") // 深度
                && jsonObject.getString("ch").startsWith("market.")) {
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onOrderBookMessage(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.QUOTE, jsonObject, _times), jsonObject.getLong("ts"));
            return;
        }
        if (jsonObject.has("subbed")) {
            String _topic = jsonObject.getString("subbed");
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onSubbedMessage(_VENDER.name(), connid,
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.SUBBED, jsonObject, _times), _topic, receiveTime);
            return;
        }
        if (jsonObject.has("unsubbed")) {
            String _topic = jsonObject.getString("unsubbed");
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onUnSubbedMessage(_VENDER.name(), connid,
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.UNSUBBED, jsonObject, _times), _topic);
            return;
        }
        if (jsonObject.has("err-code")) {
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            jsonObject.put("url", webSocket.request().url().toString());
            jsonObject.put("headers", webSocket.request().headers());
            this.combineMessageListener.onErrorMessage(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.ERROR, jsonObject, _times), receiveTime);
            return;
        }
        jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
        jsonObject.put("url", webSocket.request().url().toString());
        jsonObject.put("headers", webSocket.request().headers());
        jsonObject.put("desc", "收到一条消息未处理");
        this.combineMessageListener.onErrorMessage(_VENDER.name(), connid, 
                InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.UNKNOW, jsonObject, _times), receiveTime);
    }

}
