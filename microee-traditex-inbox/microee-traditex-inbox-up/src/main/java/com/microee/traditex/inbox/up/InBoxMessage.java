package com.microee.traditex.inbox.up;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microee.plugin.http.assets.HttpAssets;
import com.microee.traditex.inbox.oem.constrants.VENDER;

//time0 源头时间
//timeA 收到时间
//timeB 处理时间
//timeC 入库时间
//timeD 广播时间
public class InBoxMessage {

    public static final String SHUTDOWN = "shutdown"; // 连接主动关闭，已断开
    public static final String ERROR = "error"; // 
    public static final String QUOTE = "quote"; // 报价等同于 orderbook
    public static final String CONNECTED = "connected"; // 连接成功打开
    public static final String DISCONNECTED = "disconnected"; // 连接被动关闭, 已断开
    public static final String TIMEOUT = "timeout"; // 连接超时
    public static final String PRICE_STREAM = "pricing-stream";
    public static final String PING = "ping";
    public static final String FAILED = "failed"; // 连接失败, 已断开
    public static final String SUBBED = "subbed";
    public static final String UNSUB = "unsub";
    public static final String UNSUBBED = "unsubbed";
    public static final String ACCOUNT_CHANGE = "account-change";
    public static final String ORDER_STATS = "order-stats";
    public static final String UNKNOW = "unknow";
    public static final String AUTH = "auth";
    public static final String KLINE = "kline";

    public static Message getMessage(
            String connid, VENDER vender, String event, JSONObject message, JSONObject times) {
        return new Message(connid, vender, event, message, times);
    }
    
    public static class Message implements Serializable {

		private static final long serialVersionUID = -7370265026962529880L;
		
		public final String connid;
		public final VENDER vender;
		public final String event;
		public final Map<String, Object> message;
		public final Map<String, Object> times;
		
		public Message(String connid, VENDER vender, String event, JSONObject message, JSONObject times) {
			super();
			this.connid = connid;
			this.vender = vender;
			this.event = event;
			this.message = HttpAssets.parseJson(message.toString(), new TypeReference<Map<String, Object>>() {});
			this.times = HttpAssets.parseJson(times.toString(), new TypeReference<Map<String, Object>>() {});
		}
    	
    }
        
}
