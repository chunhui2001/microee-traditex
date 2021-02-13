package com.microee.traditex.inbox.interfaces;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.microee.plugin.response.R;

public interface ITradiTexConnectorRMi {

    // #### 生成一个连接ID
    @RequestMapping(value = "/genid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> genid() ;

    // ### 查看连接详情
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Map<String, JSONObject>> get(@RequestParam("connid") String connid) ;
    
    // ### 查看所有连接
    @RequestMapping(value = "/conns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Map<String, JSONObject>> conns() ;

    // ### 关闭所有连接
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Set<String>> shutdown(@RequestBody String[] connids) ;
    
    // ### 销毁一个连接
    @RequestMapping(value = "/destroy", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Set<String>> destroy(
    		@RequestParam("connid") String connid,
            @RequestParam("ONE_TIME_PASSWORD") String otp
    ) ;

    // ### ping一个连接, 延长该连接的存活时间
    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Long> ping(@RequestParam("connid") String connid) ;

    // ### B2C2 orderbook
    // 建立 b2c2 orderbook ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/b2c2/orderbook/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> b2c2OrderBookNew(
    		@RequestParam("connid") String connid,
            @RequestParam("wsHost") String wsHost,
            @RequestParam("restHost") String restHost,
            @RequestParam("accessToken") String accessToken,
            @RequestParam(value = "proxy-address", required = false) String proxyAddress,
            @RequestParam(value = "proxy-port", required = false) Integer proxyPost
    ) ;

    // ### JumpTrading orderbook
    // 建立 JumpTrading orderbook ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/jumptrading/orderbook/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> jumpTradingOrderBookNew(
    		@RequestParam("connid") String connid,
            @RequestParam("streamHost") String streamHost,
            @RequestParam("apiKey") String apiKey,
            @RequestParam("secret") String secret,
            @RequestParam(value = "proxy-address", required = false) String proxyAddress,
            @RequestParam(value = "proxy-port", required = false) Integer proxyPost
    ) ;

    // ### CumberLand orderbook
    // 建立 CumberLand orderbook ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/cumberland/orderbook/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> cumberlandOrderBookNew(
    		@RequestParam("connid") String connid,
            @RequestParam("cbHost") String cbHost,
            @RequestParam("counterPartyId") String counterPartyId,
            @RequestParam("userId") String userId,
            @RequestParam("sslfile") MultipartFile sslfile, 
            @RequestParam("sslPassword") String sslPassword,
            @RequestParam(value = "proxy-address", required = false) String proxyAddress,
            @RequestParam(value = "proxy-port", required = false) Integer proxyPost
    ) throws IOException ;

    // ### HBiTexTrade orderbook
    // 建立 HBiTexTrade orderbook ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/hbitex/orderbook/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> hbitexOrderBookNew(
    		@RequestHeader("connid") String connid,
            @RequestHeader("exchangeCode") String exchangeCode,
            @RequestHeader(value = "proxy-address", required = false) String proxyAddr,
            @RequestHeader(value = "proxy-port", required = false) Integer proxyPost,
            @RequestParam("wshost") String wshost
    ) ;

    // ### HBiTexTrade K线
    // 建立 HBiTexTrade K线 ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/hbitex/kline/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> hbitexKLineNew(
    		@RequestHeader("connid") String connid,
            @RequestHeader("exchangeCode") String exchangeCode,
            @RequestHeader(value = "proxy-address", required = false) String proxyAddr,
            @RequestHeader(value = "proxy-port", required = false) Integer proxyPost,
            @RequestParam("wshost") String wshost
    ) ;

    // ### HBiTexTrade K线
    // ### 订阅 HBiTexTrade K线 变动
    // market.ethbtc.kline.1min
    @RequestMapping(value = "/hbitex/kline/sub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> hbitexKLineSub(
    		@RequestHeader("connid") String connid,
            @RequestParam("symbol") String symbol,
            @RequestParam("period") String period
    ) ;

    // ### 订阅 HBiTexTrade orderbook 变动
    @RequestMapping(value = "/hbitex/orderbook/sub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> hbitexOrderBookSub(
    		@RequestHeader("connid") String connid,
            @RequestParam("step") String step, 
            @RequestParam("symbol") String symbol
    ) ;

    // ### 取消订阅 HBiTexTrade orderbook 变动
    @RequestMapping(value = "/hbitex/orderbook/unsub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> hbitexOrderBookUnsub(
    		@RequestHeader("connid") String connid,
            @RequestParam("step") String step, 
            @RequestParam("symbol") String symbol
    ) ;

    // ### 取消订阅资产或订单状态变动
    @RequestMapping(value = "/hbitex/orderbalance/unsub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> hbitexOrderBalanceUnsub(
    		@RequestHeader("connid") String connid,
            @RequestParam("topic") String topic
    ) ;
    
    // ### HBiTexTrade orderbalance
    // 建立 HBiTexTrade orderbalance ws, 返回新建立的 websocket 的唯一ID
    @RequestMapping(value = "/hbitex/orderbalance/new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> hbitexOrderBalanceNew(
    		@RequestHeader("connid") String connid,
    		@RequestHeader("exchangeCode") String exchangeCode,
    		@RequestHeader("uid") String uid,
    		@RequestHeader(value = "proxy-address", required = false) String proxyAddress,
    		@RequestHeader(value = "proxy-port", required = false) Integer proxyPost,
    		@RequestParam("wshost") String wshost
    ) ;

    // ### HBiTexTrade orderbalance
    // orderbalance 登录成功后, 返回一个一次性口令, 代替后续需要提供 accessKey 的鉴权
    @RequestMapping(value = "/hbitex/orderbalance/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> hbitexOrderBalanceLogin(
    		@RequestHeader("connid") String connid,
    		@RequestHeader("uid") String uid,
            @RequestHeader("accessKey") String accessKey,
            @RequestHeader("secretKey") String secretKey, 
            @RequestParam(value = "accountId", required=false) String accountId
    ) ;

    // ### HBiTexTrade account subscribe 订阅账户资产变动
    @RequestMapping(value = "/hbitex/account/sub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> hbitexAccountSubscribe(
    		@RequestHeader("connid") String connid
	);

    // ### HBiTexTrade order subscribe 订阅订单更新, 相比现有用户订单更新推送主题“orders.$symbol”，
    // 新增主题“orders.$symbol.update”拥有更低的数据延迟以及更准确的消息顺序
    @RequestMapping(value = "/hbitex/order/sub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> hbitexOrderSubscribe(
    		@RequestHeader("connid") String connid,
            @RequestParam("symbol") String symbol
    ) ;

    // ### oanda 外汇
    // 连接 oanda 汇率 stream, 返回新建立的 stream 的唯一ID
    @RequestMapping(value = "/oanda/pricing/stream-setup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<String> oandaPricingStreamSetup(
    		@RequestParam("stream-host") String streamHost,
            @RequestParam("connid") String connid, 
            @RequestParam("account-id") String accountId,
            @RequestParam("access-token") String accessToken,
            @RequestParam("instruments") String[] instruments,
            @RequestParam(value = "proxy-address", required = false) String proxyAddress,
            @RequestParam(value = "proxy-port", required = false) Integer proxyPost) ;
    
}
