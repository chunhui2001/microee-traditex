package com.microee.traditex.hello.app.process;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HBiTexKLineProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexKLineProcess.class);
	
	public void process(JSONObject kLineMessage) {
		String _ch = kLineMessage.getString("ch"); // market.btcusdt.kline.1min
		String _symbol = _ch.split("\\.")[1].toUpperCase();
		String _period = _ch.split("\\.")[3];
		Long _ts = kLineMessage.getLong("ts");
		JSONObject _tick = kLineMessage.getJSONObject("tick");
		LOGGER.info("vender=HBiTex, ts={}, symbol={}, period={}, tick={}", _ts, _symbol, _period, _tick);
	}
}
