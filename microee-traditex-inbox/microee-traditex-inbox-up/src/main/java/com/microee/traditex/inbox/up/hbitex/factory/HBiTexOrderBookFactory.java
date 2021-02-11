package com.microee.traditex.inbox.up.hbitex.factory;

import java.net.InetSocketAddress;

import com.microee.plugin.http.assets.HttpWebsocketListener;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.hbitex.HBiTexFactory;
import com.microee.traditex.inbox.up.hbitex.HBiTexFactoryConf;
import com.microee.traditex.inbox.up.hbitex.HBiTexWebsocketHandler;
import com.microee.traditex.inbox.up.hbitex.handlers.HBiTexWebsocketOrderBookHandler;

public class HBiTexOrderBookFactory extends HBiTexFactory {
 
	private final HBiTexWebsocketHandler wsHandler;
	private final HttpWebsocketListener wsListener;
	
	public static HBiTexOrderBookFactory create(
			HBiTexFactoryConf conf, CombineMessageListener combineMessageListener, InetSocketAddress proxy
	) {
		return new HBiTexOrderBookFactory(conf, combineMessageListener, proxy);
	}
	
	public HBiTexOrderBookFactory(
			HBiTexFactoryConf conf, CombineMessageListener combineMessageListener, InetSocketAddress proxy
	) {
		super(conf, combineMessageListener, proxy);
		this.wsHandler = new HBiTexWebsocketOrderBookHandler(this.conf.connid, this.combineMessageListener);
		this.wsListener = new HttpWebsocketListener(this.wsHandler);
	}

	@Override
	public String connectType() {
		return "HBiTex订单薄";
	}

	@Override
	public HttpWebsocketListener wsListener() {
		return this.wsListener; 
	}

	@Override
	public HBiTexWebsocketHandler wsHandler() {
		return this.wsHandler;
	}
 
}
