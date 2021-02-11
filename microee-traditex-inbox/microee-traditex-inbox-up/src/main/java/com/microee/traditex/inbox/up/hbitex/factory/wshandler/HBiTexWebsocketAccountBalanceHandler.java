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

public class HBiTexWebsocketAccountBalanceHandler extends HBiTexWebsocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(HBiTexWebsocketAccountBalanceHandler.class);
    
	public HBiTexWebsocketAccountBalanceHandler(String connid, CombineMessageListener combineMessageListener) {
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
        if (jsonObject.has("op") && jsonObject.getString("op").equals("ping")) {
            this.writeMessageWithoutLog(String.format("{\"op\":\"pong\", \"ts\":%s}", jsonObject.getLong("ts")));
            jsonObject.put("event", "ping");
            this.combineMessageListener.onPingMessage(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.PING, jsonObject, _times), receiveTime);
            return;
        }
        if (jsonObject.has("op") && jsonObject.getString("op").equals("close")) {
            logger.warn("连接被动关闭, connid={}, url={}, message={}", connid, webSocket.request().url().toString(), jsonMessage);
            this.combineMessageListener.onDisconnected(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.DISCONNECTED, jsonObject, _times), receiveTime);
            return;
        }
        if (jsonObject.has("op") && jsonObject.getString("op").equals("auth")) {
            jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onAuthMessage(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.AUTH, jsonObject, _times), receiveTime);
            return;
        }
        if (jsonObject.has("op")) {
            if (jsonObject.getString("op").equals("sub")) {
                String _topic = jsonObject.getString("topic");
                if (_topic.equals("accounts")) {
                    jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
                    this.combineMessageListener.onSubbedMessage(_VENDER.name(), connid, 
                            InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.SUBBED, jsonObject, _times), _topic, receiveTime);
                    return;
                }
                if (_topic.matches("orders.[a-z]{1,}.update")) {
                    jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
                    this.combineMessageListener.onSubbedMessage(_VENDER.name(), connid, 
                            InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.SUBBED, jsonObject, _times), _topic, receiveTime);
                    return;
                }
            }
            if (jsonObject.getString("op").equals("unsub")) {
                String _topic = jsonObject.getString("topic");
                jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
                this.combineMessageListener.onUnSubbedMessage(_VENDER.name(), connid,
                        InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.UNSUB, jsonObject, _times), _topic);
                return;
            }
        }
        if (jsonObject.has("op") && jsonObject.getString("op").equals("notify")) {
            if (jsonObject.has("topic")) {
                if (jsonObject.getString("topic").equals("accounts")) {
                    jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
                    this.combineMessageListener.onAccountMessage(_VENDER.name(), connid, 
                            InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.ACCOUNT_CHANGE, jsonObject, _times), receiveTime);
                    return;
                }
                if (jsonObject.getString("topic").matches("orders.[a-z]{1,}.update")) {
                    jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
                    this.combineMessageListener.onOrderStatMessage(_VENDER.name(), connid, 
                            InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.ORDER_STATS, jsonObject, _times), receiveTime);
                    return;
                }
            }
        }
        if (jsonObject.has("err-code") && jsonObject.getInt("err-code") != 0) {
            jsonObject.put("timeB", Instant.now().toEpochMilli()); // 处理时间
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
