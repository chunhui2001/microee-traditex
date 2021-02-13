package com.microee.traditex.inbox.app.actions;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microee.plugin.http.assets.HttpClient;
import com.microee.plugin.http.assets.HttpClientResult;
import com.microee.plugin.response.R;

// 世界上最大，最古老的比特币交易所之一
// Kraken 一直被评为在线购买和出售加密货币的最佳场所之一
// https://www.kraken.com/features/api
// https://docs.kraken.com/websockets/#overview
// https://futures.kraken.com/
@RestController
@RequestMapping("/kraken")
public class TradiTexKrakenRestful {

    private HttpClient httpClient;
    
    @PostConstruct
    public void init() {
        this.httpClient = HttpClient.create();
    }

    @RequestMapping(value = "/get-tradable-pairs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<JSONObject> getTradablePairs(@RequestParam(value = "name", required=false) String name) {
    	HttpClientResult result = httpClient.doGet("https://api.kraken.com/0/public/AssetPairs");
    	if (result == null) {
    		return R.failed(R.TIME_OUT, "fetch data time out");
    	}
    	JSONObject jsonObject = new JSONObject(result.getResult()).getJSONObject("result");
    	if (!StringUtils.isBlank(name)) {
        	if (jsonObject.has(name.toUpperCase().trim())) {
            	return R.ok(jsonObject.getJSONObject(name.toUpperCase().trim()));
        	}
        	return R.failed(R.FAILED, "invalide name");
    	}
    	return R.ok(jsonObject);
    }
    
}
