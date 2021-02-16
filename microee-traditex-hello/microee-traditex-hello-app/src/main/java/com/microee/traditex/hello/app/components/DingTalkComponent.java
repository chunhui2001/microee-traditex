package com.microee.traditex.hello.app.components;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.microee.plugin.commons.H;
import com.microee.plugin.commons.Helper.KV;
import com.microee.plugin.http.assets.HttpClient;
import com.microee.plugin.http.assets.HttpClientResult;
import com.microee.plugin.response.R;

import okhttp3.Headers;

@Component
public class DingTalkComponent {

	public static final String DINGTALK_HOST_PREFIX = "https://oapi.dingtalk.com/robot/send";

	private HttpClient httpClient;
	
	@PostConstruct
	public void init() {
		httpClient = HttpClient.create();
	}

	// 发送钉钉消息
	public String dingLink(String accessToken, String title, String message, String imageUrl, String link) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgtype", "link");
		jsonObject.put("link", new JSONObject().put("title", title).put("text", message).put("picUrl", imageUrl).put("messageUrl", link));
		return httpClient.postJson(DINGTALK_HOST_PREFIX, Headers.of("Content-Type", "application/json"), H.mapOf(KV.of("access_token", accessToken)), jsonObject.toString()).getResult();
	}

	// 发送钉钉消息
	public String dingMarkdown(String accessToken, String title, String message) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgtype", "markdown");
		jsonObject.put("markdown", new JSONObject().put("title", title).put("text", message));
		return httpClient.postJson(DINGTALK_HOST_PREFIX, Headers.of("Content-Type", "application/json"), H.mapOf(KV.of("access_token", accessToken)), jsonObject.toString()).getResult();
	}
	
}
