package com.microee.traditex.inbox.up.hbitex.factory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.microee.plugin.http.assets.HttpClientResult;
import com.microee.plugin.http.assets.HttpWebsocketListener;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.hbitex.factory.wshandler.HBiTexWebsocketHandler;
import com.microee.traditex.inbox.up.hbitex.factory.wshandler.HBiTexWebsocketKLineHandler;

public class HBiTexKLineFactory extends HBiTexFactory {

	private final HBiTexWebsocketHandler wsHandler;
	private final HttpWebsocketListener wsListener;
	
	public HBiTexKLineFactory(
			HBiTexFactoryConf conf, CombineMessageListener combineMessageListener, InetSocketAddress proxy) {
		super(conf, combineMessageListener, proxy);
		this.wsHandler = new HBiTexWebsocketKLineHandler(this.conf.connid, this.combineMessageListener);
		this.wsListener = new HttpWebsocketListener(this.wsHandler);
	}

	@Override
	public String connectType() {
		return "HBiTexK线";
	}

	@Override
	public HttpWebsocketListener wsListener() {
		return this.wsListener;
	}

	@Override
	public HBiTexWebsocketHandler wsHandler() {
		return this.wsHandler;
	}

    /**
     * 查询k线
     * @param resthost
     * @param orderId
     * @return
     */
    public String kline(String resthost, String symbol, String period, Integer size) {
        String endpoint = String.format("%s/market/history/kline", resthost);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("symbol", symbol);
        queryMap.put("period", period);
        queryMap.put("size", size);
        HttpClientResult httpClientResult = this.doGet(endpoint, queryMap); 
        return httpClientResult.getResult();
    }
	
}
