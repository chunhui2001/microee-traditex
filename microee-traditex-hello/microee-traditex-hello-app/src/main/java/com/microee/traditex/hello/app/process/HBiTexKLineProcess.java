package com.microee.traditex.hello.app.process;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microee.plugin.http.assets.HttpAssets;
import com.microee.traditex.hello.app.repository.KLineRepository;

@Component
public class HBiTexKLineProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexKLineProcess.class); 

//	@Autowired
//	private AppConfigurationProps appConf;
//	
//	@Autowired
//	private DingTalkComponent dingTalkComponent;
	
	@Autowired
	private KLineRepository klineRepository;
	
	public void process(JSONObject kLineMessage) {
		// 最新k线,
		String _ch = kLineMessage.getString("ch"); // market.btcusdt.kline.1min
		String _symbol = _ch.split("\\.")[1].toUpperCase();
		String _period = _ch.split("\\.")[3];
		Long _ts = kLineMessage.getLong("ts");
		Map<Long, Double[]> klineMap = klineRepository.fetchHBitexKLine(_ts, _symbol, _period, kLineMessage);
		LOGGER.info("vender=HBiTex, ts={}, symbol={}, period={}, data={}", _ts, _symbol, _period, HttpAssets.toJsonString(klineMap));
//		String klineImage = "http://cdn.microee.com/RichMedias/apidoc/api-sign.png";
//		String _link = "https://www.baidu.com";
//		String _title = "trad";
//		String _message = "下单";
		// dingTalkComponent.dingLink(appConf.getTradDingTalkToken(), _title, _message, klineImage, _link);
	}
	
}
