package com.microee.traditex.hello.app.process;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microee.plugin.http.assets.HttpAssets;

@Component
public class HBiTexKLineProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexKLineProcess.class);

//	@Autowired
//	private AppConfigurationProps appConf;
//	
//	@Autowired
//	private DingTalkComponent dingTalkComponent;
	
	public void process(JSONObject kLineMessage) {
		// 最新k线, 
		String _ch = kLineMessage.getString("ch"); // market.btcusdt.kline.1min
		String _symbol = _ch.split("\\.")[1].toUpperCase();
		String _period = _ch.split("\\.")[3];
		Long _ts = kLineMessage.getLong("ts");
		JSONObject _tick = kLineMessage.getJSONObject("tick");
		Double[] data = new Double[] { _ts.doubleValue(), _tick.getDouble("open"), _tick.getDouble("high"), _tick.getDouble("low"), _tick.getDouble("close") };
		// 查看缓存中是否有历史k线, 将最新k线追加到历史k线, 并生成k线图
		// 如果没有历史k线, 则调用rest接口查询并缓存历史k线
		// 起一个定时任务定时查询历史k线
		// TODO
		LOGGER.info("vender=HBiTex, ts={}, symbol={}, period={}, data={}", _ts, _symbol, _period, HttpAssets.toJsonString(data));
//		String klineImage = "http://cdn.microee.com/RichMedias/apidoc/api-sign.png";
//		String _link = "https://www.baidu.com";
//		String _title = "trad";
//		String _message = "下单";
		// dingTalkComponent.dingLink(appConf.getTradDingTalkToken(), _title, _message, klineImage, _link);
	}
	
}
