package com.microee.traditex.inbox.interfaces;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.microee.plugin.response.R;

public interface ITradiTexHBiTexKLineRMi {

	// #### kline
	/**
	 * 查询k线
	 * @param connid
	 * @param resthost
	 * @param symbol
	 * @param period   1min, 5min, 15min, 30min, 60min, 4hour, 1day, 1mon, 1week, 1year
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<String> query(
			@RequestHeader("connid") String connid, 
			@RequestHeader("resthost") String resthost,
			@RequestParam("symbol") String symbol, 
			@RequestParam("period") String period,
			@RequestParam(value = "size", required = false, defaultValue = "150") Integer size);
}
