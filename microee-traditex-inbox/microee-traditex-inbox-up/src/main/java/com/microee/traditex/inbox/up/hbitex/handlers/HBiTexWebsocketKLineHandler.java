package com.microee.traditex.inbox.up.hbitex.handlers;

import java.time.Instant;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microee.plugin.zip.Zip;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.InBoxMessage;
import com.microee.traditex.inbox.up.hbitex.HBiTexWebsocketHandler;

import okhttp3.WebSocket;
import okio.ByteString;

public class HBiTexWebsocketKLineHandler extends HBiTexWebsocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(HBiTexWebsocketKLineHandler.class);
    
	public HBiTexWebsocketKLineHandler(String connid, CombineMessageListener combineMessageListener) {
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
        if (jsonObject.has("ping")) {
            this.writeMessageWithoutLog(String.format("{\"pong\": %s}", jsonObject.getLong("ping")));
            this.combineMessageListener.onPingMessage(_VENDER.name(), connid,  InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.PING, jsonObject, _times), receiveTime);
            return;
        }
        if (jsonObject.has("ch") && jsonObject.getString("ch").contains("kline")) {
			// {
			//   "ch": "market.ethbtc.kline.1min",
			//   "ts": 1613039969994,
			//   "tick": {
			//      "id": 1613039940, // unix 时间, 同时作为K线ID
			//      "open": 0.039146, // 开盘价
			//      "close": 0.039143, // 收盘价（当K线为最晚的一根时，是最新成交价）
			//      "low": 0.039131, // 最低价
			//      "high": 0.039153, // 最高价
			//      "amount": 43.2841, // 成交量
			//      "vol": 1.6943929044, // 成交额, 即 sum(每一笔成交价 * 该笔的成交量)
			//      "count": 59 // 成交笔数
			//   }
			// }
        	this.combineMessageListener.onKLineMessage(_VENDER.name(), connid,  InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.KLINE, jsonObject, _times), receiveTime);
        	return;
        } 
        if (jsonObject.has("subbed")) {
            String _topic = jsonObject.getString("subbed");
            _times.put("timeB", Instant.now().toEpochMilli()); // 处理时间
            this.combineMessageListener.onSubbedMessage(_VENDER.name(), connid,
                    InBoxMessage.getMessage(connid, _VENDER, InBoxMessage.SUBBED, jsonObject, _times), _topic, receiveTime);
            return;
        }
        logger.info("onMessageByteStringHandler, text={}", jsonMessage);
		
	}

}
