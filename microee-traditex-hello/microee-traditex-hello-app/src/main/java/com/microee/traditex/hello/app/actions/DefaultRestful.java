package com.microee.traditex.hello.app.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microee.plugin.response.R;
import com.microee.traditex.hello.app.components.DingTalkComponent;
import com.microee.traditex.hello.app.props.AppConfigurationProps;

@RestController
@RequestMapping("/")
public class DefaultRestful {

	@Autowired
	private AppConfigurationProps appConf;
	
	@Autowired
	private DingTalkComponent dingTalkComponent;
	
	// 发送钉钉消息
	@RequestMapping(value = "/ding", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<String> ding(@RequestParam("title") String title, @RequestBody String message) {
		return R.ok(dingTalkComponent.dingMarkdown(appConf.getTradDingTalkToken(), title, message));
	}

	// 发送钉钉消息
	@RequestMapping(value = "/ding-link", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<String> dingLink(@RequestParam("title") String title, @RequestBody String message) {
		return R.ok(dingTalkComponent.dingLink(appConf.getTradDingTalkToken(), title, message, "http://cdn.microee.com/RichMedias/apidoc/api-sign.png", "https://www.baidu.com"));
	}
	
}
