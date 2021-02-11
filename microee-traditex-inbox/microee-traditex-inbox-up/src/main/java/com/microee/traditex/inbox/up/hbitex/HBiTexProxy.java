package com.microee.traditex.inbox.up.hbitex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microee.plugin.http.assets.HttpAssets;
import com.microee.plugin.http.assets.HttpClient;
import com.microee.traditex.inbox.oem.hbitex.HBiTexAccount;
import com.microee.traditex.inbox.oem.hbitex.HBiTexHttpResult;
import com.microee.traditex.inbox.oem.hbitex.po.HBiTexOrderPlaceParam;
import com.microee.traditex.inbox.oem.hbitex.vo.AccountBalance;
import com.microee.traditex.inbox.oem.hbitex.vo.HBiTexOrderDetails;
import com.microee.traditex.inbox.oem.hbitex.vo.OrderMatchresults;

import okhttp3.Headers;

public class HBiTexProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexProxy.class); 
    
	public final HBiTexFactory factory;
	
	public HBiTexProxy(HBiTexFactory factory) {
		super();
		this.factory = factory;
	}
    
    /**
     * 取消订阅
     */
    public void unsub(String step, String... tickers) {
        // {"id":"28a1342d-49a8-4c81-a903-86700f655251","sub":"market.ltcht.depth.step0","pick":["asks.5","bids.5"]}
        // { "unsub": "topic to unsub", "id": "id generate by client" }
        for (String ticker : tickers) {
            if (ticker == null || ticker.trim().isEmpty()) {
                continue;
            }
            JSONObject json = new JSONObject();
            json.put("unsub", String.format("market.%s.depth.%s", ticker, step));
            json.put("id", UUID.randomUUID().toString());
            this.factory.wsHandler().writeMessage(json.toString());
        }
    }

    /**
     * 取消订阅资产或订单状态变动
     * @param topic
     */
    public void unsubtopic(String topic) {
        JSONObject json = new JSONObject();
        json.put("op", "unsub");
        json.put("topic", topic);
        json.put("cid", UUID.randomUUID().toString());
        this.factory.wsHandler().writeMessage(json.toString());
    }
    
    /**
     * 订阅相关交易对盘口深度
     * 
     * @param tickers
     */
    public void subscribeDepth(String... tickers) {
        for (String ticker : tickers) {
            if (ticker == null || ticker.trim().isEmpty()) {
                continue;
            }
            //@formatter:off
            String message = String.format("{\"id\":\"%s\",\"sub\":\"market.%s.depth.step0\",\"pick\":[\"asks.30\",\"bids.30\"]}", UUID.randomUUID().toString(), ticker);
            //@formatter:on
            this.factory.wsHandler().writeMessage(message);
        }
    }
    
    /**
     * 订阅相关交易对盘口深度
     * 
     * @param tickers
     */
    public void subscribeDepth(String step, Integer pick, String... tickers) {
        for (String ticker : tickers) {
            if (ticker == null || ticker.trim().isEmpty()) {
                continue;
            }
            String pickExp = "";
            if (pick != -1) {
                pickExp = String.format(",\"pick\":[\"asks.%s\",\"bids.%s\"]", pick, pick);
            }
            String message = String.format("{\"id\":\"%s\",\"sub\":\"market.%s.depth.%s\"%s}", UUID.randomUUID().toString(), ticker, step, pickExp);
            this.factory.wsHandler().writeMessage(message);
        }
    }

    // 订阅K线, market.ethbtc.kline.1min
	public void subscribeKLine(String period, String symbol) {
		String message = String.format("{\"id\":\"%s\",\"sub\":\"market.%s.kline.%s\"}", UUID.randomUUID().toString(), symbol, period);
        this.factory.wsHandler().writeMessage(message);
	}
	
    /**
     * 查询交易对列表
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<List<Map<String, Object>>> querySymbols(String resthost) {
        String endpoint = String.format("%s/v1/common/symbols", resthost);
        return HttpAssets.parseJson(this.factory.doGet(endpoint, null).getResult(), new TypeReference<HBiTexHttpResult<List<Map<String, Object>>>>() {});
    }

    /**
     * 查询订单详情
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<HBiTexOrderDetails> queryOrderDetail(String resthost, String orderId) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders/%s", resthost, orderId);
        return HttpAssets.parseJson(this.factory.doGet(endpoint, null, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<HBiTexOrderDetails>>() {});
    }
    /**
     * 查询订单详情
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<List<HBiTexOrderDetails>> queryOrders(String resthost, String symbol, String states) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders", resthost);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("symbol", symbol);
        queryMap.put("states", states);
        return HttpAssets.parseJson(this.factory.doGet(endpoint, queryMap, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<List<HBiTexOrderDetails>>>() {});
    }
    
    /**
     * 根据客户端订单id查询订单详情
     * @param resthost
     * @param clientOrderId
     * @return
     */
    public HBiTexHttpResult<HBiTexOrderDetails> queryOrderByClientId(String resthost,
            String clientOrderId) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders/getClientOrder", resthost);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("clientOrderId", clientOrderId);
        return HttpAssets.parseJson(this.factory.doGet(endpoint, queryMap, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<HBiTexOrderDetails>>() {});
    }
    
    /**
     * 创建订单
     * @param clientOrderId
     * @param orderType
     * @param price
     * @param amount
     * @param side
     */
    public HBiTexHttpResult<String> createOrder(String resthost, HBiTexOrderPlaceParam orderPlaceParam) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders/place", resthost);
        orderPlaceParam.setAccountId(hbiTexAccount.getAccountSpotId());
        return HttpAssets.parseJson(this.factory.doPost(endpoint, orderPlaceParam, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<String>>() {});
    }

    
    /**
     * 创建订单
     * @param clientOrderId
     * @param orderType
     * @param price
     * @param amount
     * @param side
     */
    public static HBiTexHttpResult<String> createOrder(HttpClient httpClient, Headers headers, String resthost, HBiTexAccount hbiTexAccount, HBiTexOrderPlaceParam orderPlaceParam) {
        String endpoint = String.format("%s/v1/order/orders/place", resthost);
        orderPlaceParam.setAccountId(hbiTexAccount.getAccountSpotId());
        return HttpAssets.parseJson(HBiTexFactory.doPostParam(httpClient, headers, endpoint, orderPlaceParam, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<String>>() {});
    }

    // symbol btcjpy
    // 订阅订单更新, 相比现有用户订单更新推送主题“orders.$symbol”， 新增主题“orders.$symbol.update”拥有更低的数据延迟以及更准确的消息顺序
    public void subOrderStat(String symbol) {
        String payload = String.format("{\"op\": \"sub\",\"cid\": \"%s\",\"topic\": \"orders.%s.update\"}", UUID.randomUUID().toString(), symbol);
        LOGGER.info("订阅订单状态变化: symbol={}, payload={}", symbol, payload);
        this.factory.wsHandler().writeMessage(payload);
    }
    
    /**
     * 撤单
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<String> orderCancel(String resthost, String orderId) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders/%s/submitcancel", resthost, orderId);
        return HttpAssets.parseJson(this.factory.doPost(endpoint, null, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<String>>() {});
    }
    
    /**
     * 批量撤单
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<Map<String, Object>> orderCancel(String resthost, String[] orderId) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/order/orders/batchcancel", resthost);
        JSONObject param = new JSONObject();
        param.put("order-ids", orderId);
        return HttpAssets.parseJson(this.factory.doPost(endpoint, param, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<Map<String, Object>>>() {});
    }

    /**
     * 查询账户信息
     * @param resthost
     * @return
     */
    public HBiTexHttpResult<List<Map<String, Object>>> queryAccounts(String resthost) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/account/accounts", resthost);
        return HttpAssets.parseJson(this.factory.doGet(endpoint, null, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<List<Map<String, Object>>>>() {});
    }
    
    // 订阅账户资产变动
    public void subAccount() {
        String payload = String.format("{\"op\": \"sub\",\"cid\": \"%s\",\"topic\": \"accounts\",\"model\": \"0\"}", UUID.randomUUID().toString());
        this.factory.wsHandler().writeMessage(payload);
    }

    /**
     * 查询订单撮合详情
     * @param resthost
     * @param orderId
     * @return
     */
    public HBiTexHttpResult<List<OrderMatchresults>> queryOrderMatchresults(String resthost, String orderId) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint =  resthost + "/v1/order/orders/" + orderId + "/matchresults";
        return HttpAssets.parseJson(this.factory.doGet(endpoint, null, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<List<OrderMatchresults>>>() {});
    }

    /**
     * 查询账户余额
     * @param resthost
     * @return
     */
    public HBiTexHttpResult<AccountBalance> querySpotAccountBalance(String resthost) {
        HBiTexAccount hbiTexAccount = this.factory.getAccount();
        String endpoint = String.format("%s/v1/account/accounts/%s/balance", resthost, hbiTexAccount.getAccountSpotId());
        return HttpAssets.parseJson(this.factory.doGet(endpoint, null, hbiTexAccount).getResult(), new TypeReference<HBiTexHttpResult<AccountBalance>>() {});
    }
    
}
