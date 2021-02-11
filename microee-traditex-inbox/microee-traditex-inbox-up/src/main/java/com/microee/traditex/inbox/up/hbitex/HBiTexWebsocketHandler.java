package com.microee.traditex.inbox.up.hbitex;

import java.time.Instant;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microee.plugin.http.assets.HttpWebsocketHandler;
import com.microee.traditex.inbox.oem.constrants.ConnectStatus;
import com.microee.traditex.inbox.oem.constrants.VENDER;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.InBoxMessage;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public abstract class HBiTexWebsocketHandler implements HttpWebsocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(HBiTexWebsocketHandler.class);
    public static final VENDER _VENDER = VENDER.HBiTex;

    protected WebSocket webSocket;
    protected final String connid;
    protected ConnectStatus connectStatus;
    protected final CombineMessageListener combineMessageListener;

    public HBiTexWebsocketHandler(final String connid, final CombineMessageListener combineMessageListener) {
        this.connid = connid;
        this.connectStatus = ConnectStatus.UNKNOW;
        this.combineMessageListener = combineMessageListener;
    }

    @Override
    public void onClosedHandler(WebSocket webSocket, int code, String reason) {
        this.connectStatus = ConnectStatus.CLOSED;
        logger.info("websocket连接已关闭: url={}, code={}, reason={}", webSocket.request().url().toString(), code, reason);
    }

    @Override
    public void onFailureHandler(WebSocket webSocket, Throwable t, String responseText) {
        if (this.connectStatus != null && this.connectStatus.equals(ConnectStatus.DESTROY)) {
            // 主动销毁
            return;
        }
        this.connectStatus = ConnectStatus.DAMAGED;
        logger.error("onFailureHandler, status={}, errorMessage={}", this.getConnectStatus(), responseText);
        Long receiveTime = Instant.now().toEpochMilli();
        JSONObject _times = new JSONObject();
        _times.put("timeA", receiveTime); // 收到时间
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", webSocket.request().url());
        jsonObject.put("headers", webSocket.request().headers());
        jsonObject.put("errorMessage", responseText);
        this.combineMessageListener.onFailed(_VENDER.name(), connid, 
                InBoxMessage.getMessage(connid, VENDER.HBiTex, InBoxMessage.FAILED, jsonObject, _times), receiveTime);
    }

    @Override
    public void onTimeoutHandler(String url, long start, long end) {
        this.connectStatus = ConnectStatus.TIMEOUT;
        Long receiveTime = Instant.now().toEpochMilli();
        JSONObject _times = new JSONObject();
        _times.put("timeA", receiveTime); // 收到时间
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("start", start);
        jsonObject.put("end", end);
        jsonObject.put("duration", end - start);
        jsonObject.put("url", url);
        this.combineMessageListener.onTimeout(_VENDER.name(), connid, 
                InBoxMessage.getMessage(connid, VENDER.HBiTex, InBoxMessage.TIMEOUT, jsonObject, _times), receiveTime, start, end);
    }
    
    @Override
    public void onOpenHandler(WebSocket webSocket, Response response) {
        this.connectStatus = ConnectStatus.ONLINE;
        this.webSocket = webSocket;
        if (this.combineMessageListener != null) {
            Long receiveTime = Instant.now().toEpochMilli();
            JSONObject _times = new JSONObject();
            _times.put("timeA", receiveTime); // 收到时间
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", webSocket.request().url());
            jsonObject.put("headers", webSocket.request().headers());
            this.combineMessageListener.onConnected(_VENDER.name(), connid, 
                    InBoxMessage.getMessage(connid, VENDER.HBiTex, InBoxMessage.CONNECTED, jsonObject, _times));
        }
    }

    @Override
    public abstract void onMessageStringHandler(WebSocket webSocket, String text) ;

    @Override
    public abstract void onMessageByteStringHandler(WebSocket webSocket, ByteString bytes);

    @Override
    public void closeWebsocket() {
        if (this.webSocket != null) {
            try {
                this.connectStatus = ConnectStatus.DESTROY;
                // https://tools.ietf.org/html/rfc6455#section-7.4
                boolean b = this.webSocket.close(1000, "主动关闭连接");
                logger.info("连接已销毁: connid={}, closed={}", this.connid, b);
            } catch (Exception e) {
                logger.info("主动关闭连接异常: connid={}, errorMessage={}", this.connid, e.getMessage(), e);
            }
        }
    }

    public void writeMessageWithoutLog(String message) {
        this.webSocket.send(message);
    }

    public void writeMessage(String message) {
        this.webSocket.send(message);
        logger.info("发送了一条消息: connid={}, url={}, message={}", this.connid, webSocket.request().url(), message);
    }

    public ConnectStatus getConnectStatus() {
        return this.connectStatus;
    }

    public void setConnectStatus(ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
    }

}