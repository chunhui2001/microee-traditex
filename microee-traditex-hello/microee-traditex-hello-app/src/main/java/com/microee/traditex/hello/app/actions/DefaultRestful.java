package com.microee.traditex.hello.app.actions;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microee.plugin.commons.H;
import com.microee.plugin.commons.Helper.KV;
import com.microee.plugin.http.assets.HttpClient;
import com.microee.plugin.http.assets.HttpClientResult;
import com.microee.plugin.response.R;
import com.microee.traditex.hello.app.props.AppConfigurationProps;

import okhttp3.Headers;

@RestController
@RequestMapping("/")
public class DefaultRestful {

	private HttpClient httpClient;
	
	@Autowired
	private AppConfigurationProps appConf;
	
	@PostConstruct
	public void init() {
		httpClient = HttpClient.create();
	}
	
	// 发送钉钉消息
	@RequestMapping(value = "/ding", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<String> ding(@RequestParam("title") String title, @RequestBody String message) {
		String _url = "https://oapi.dingtalk.com/robot/send";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgtype", "markdown");
		jsonObject.put("markdown", new JSONObject().put("title", title).put("text", message));
		HttpClientResult httpResult = httpClient.postJson(_url, Headers.of("Content-Type", "application/json"), H.mapOf(KV.of("access_token", appConf.getTradDingTalkToken())), jsonObject.toString());
		return R.ok(httpResult.getResult());
	}

	// 发送钉钉消息
	@RequestMapping(value = "/ding-link", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<String> dingLink(@RequestParam("title") String title, @RequestBody String message) {
		String _url = "https://oapi.dingtalk.com/robot/send";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgtype", "link");
		jsonObject.put("link", new JSONObject().put("title", title).put("text", message).put("picUrl", "http://cdn.microee.com/RichMedias/apidoc/api-sign.png").put("messageUrl", "https://www.baidu.com"));
		HttpClientResult httpResult = httpClient.postJson(_url, Headers.of("Content-Type", "application/json"), H.mapOf(KV.of("access_token", appConf.getTradDingTalkToken())), jsonObject.toString());
		return R.ok(httpResult.getResult());
	}
	
}
