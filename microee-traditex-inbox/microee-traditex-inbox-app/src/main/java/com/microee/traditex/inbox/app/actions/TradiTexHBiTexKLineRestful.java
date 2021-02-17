package com.microee.traditex.inbox.app.actions;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microee.plugin.response.R;
import com.microee.traditex.inbox.app.components.TradiTexConnectComponent;
import com.microee.traditex.inbox.app.validator.RestValidator;
import com.microee.traditex.inbox.interfaces.ITradiTexHBiTexKLineRMi;
import com.microee.traditex.inbox.oem.connector.TradiTexConnection;
import com.microee.traditex.inbox.up.hbitex.factory.HBiTexKLineFactory;

// HBiTex K线相关
@RestController
@RequestMapping("/hbitex-kline")
public class TradiTexHBiTexKLineRestful implements ITradiTexHBiTexKLineRMi {

	@Autowired
    private TradiTexConnectComponent connectionComponent;
    
    @Autowired
    private RestValidator restValidator; 
    
    // #### kline
    /**
     * 查询k线
     * @param connid
     * @param resthost
     * @param symbol
     * @param period 1min, 5min, 15min, 30min, 60min, 4hour, 1day, 1mon, 1week, 1year
     * @param size 查询的k线数量
     * @return
     */
    @Override
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> query(
            @RequestHeader("connid") String connid,
            @RequestHeader("resthost") String resthost,
            @RequestParam("symbol") String symbol,
            @RequestParam("period") String period,
            @RequestParam(value = "size", required=false, defaultValue="150") Integer size) {
        Assertions.assertThat(connid).withFailMessage("%s 必传", "connid").isNotBlank();
        restValidator.connIdValid(connid);
        TradiTexConnection<?> conn = connectionComponent.get(connid);
        restValidator.connIdFun(connid, conn).connIdIllegalHbiTex(connid);
        HBiTexKLineFactory hbiTexTradFactory = connectionComponent.get(connid, HBiTexKLineFactory.class);
        String result = hbiTexTradFactory.kline(resthost, symbol, period, size);
        return R.ok(result);
    }
    
}
