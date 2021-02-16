package com.microee.traditex.hello.app.process;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microee.plugin.http.assets.HttpAssets;

@Component
public class HBiTexKLineProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexKLineProcess.class);
	
	public void process(JSONObject kLineMessage) {
		String _ch = kLineMessage.getString("ch"); // market.btcusdt.kline.1min
		String _symbol = _ch.split("\\.")[1].toUpperCase();
		String _period = _ch.split("\\.")[3];
		Long _ts = kLineMessage.getLong("ts");
		JSONObject _tick = kLineMessage.getJSONObject("tick");
		Double[] data = new Double[] { _ts.doubleValue(), _tick.getDouble("open"), _tick.getDouble("high"), _tick.getDouble("low"), _tick.getDouble("close") };
		LOGGER.info("vender=HBiTex, ts={}, symbol={}, period={}, data={}", _ts, _symbol, _period, HttpAssets.toJsonString(data));
	}
	
	public static void main(String[] args) {
		Long l = 1613491058841l;
		System.out.println(BigDecimal.valueOf(l.doubleValue()).toPlainString());
	}
	
}
