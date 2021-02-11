package com.microee.traditex.inbox.up.hbitex.factory.wshandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microee.plugin.zip.Zip;
import com.microee.traditex.inbox.up.CombineMessageListener;

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
        logger.info("onMessageByteStringHandler, text={}", jsonMessage);
		
	}

}
