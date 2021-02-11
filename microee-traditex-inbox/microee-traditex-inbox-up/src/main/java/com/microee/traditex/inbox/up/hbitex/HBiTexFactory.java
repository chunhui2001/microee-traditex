package com.microee.traditex.inbox.up.hbitex;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.microee.plugin.http.assets.HttpAssets;
import com.microee.plugin.http.assets.HttpClient;
import com.microee.plugin.http.assets.HttpClientResult;
import com.microee.plugin.http.assets.HttpWebsocketListener;
import com.microee.plugin.response.R;
import com.microee.plugin.response.exception.RestException;
import com.microee.traditex.inbox.oem.connector.ITridexTradFactory;
import com.microee.traditex.inbox.oem.constrants.ConnectStatus;
import com.microee.traditex.inbox.oem.hbitex.ApiSignature;
import com.microee.traditex.inbox.oem.hbitex.HBiTexAccount;
import com.microee.traditex.inbox.up.CombineMessageListener;
import com.microee.traditex.inbox.up.TradiTexAssists;

import okhttp3.Headers;

public abstract class HBiTexFactory implements ITridexTradFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HBiTexFactory.class); 
    
    protected final HBiTexProxy proxy;
    protected final HBiTexFactoryConf conf;
    protected final JSONObject auth;
    protected final HttpClient httpClient;
    protected final HttpClient wsHttpClient;
    protected final CombineMessageListener combineMessageListener;
    protected final HBiTexThreader theader;
    protected final List<String> topics;
    protected Long lastTime;
    protected String lastEvent;
    protected Long lastPing; // 最后一次续约时间
    protected Long lease; // 租约有效期, 超过这个时间将被销毁
    
    public HBiTexFactory(HBiTexFactoryConf conf, CombineMessageListener combineMessageListener, InetSocketAddress proxy) {
        this.conf = conf;
        this.auth = new JSONObject();
        this.wsHttpClient = HttpClient.create(null, proxy, "OKHTTP-WEBSOCKET");
        this.httpClient = HttpClient.create(null, proxy, "OKHTTP-CLIENT").setListener(this.conf.logger);
        this.combineMessageListener = combineMessageListener;
        this.theader = new HBiTexThreader(this, this.conf.threadName);
        this.topics = new ArrayList<>();
        this.lastEvent = null;
        this.lastTime = null;
        this.lastPing = null;
        this.lease = null;
        this.proxy = new HBiTexProxy(this);
    }
    
    public Headers headers() {
    	if (this.conf.uid != null) {
    		return Headers.of("connid", this.conf.connid, "cloud-exchange", this.conf.exchangeCode, "uid", this.conf.uid);
    	}
    	return Headers.of("connid", this.conf.connid, "cloud-exchange", this.conf.exchangeCode);
    }
    
    // 子类重写该方法
    public abstract String connectType();
    public abstract HBiTexWebsocketHandler wsHandler();
    public abstract HttpWebsocketListener wsListener();

    public HBiTexFactory createWebSocket() {
        this.wsHttpClient.websocket(this.conf.wshost, this.headers(), this.wsListener());
        return this;
    }
    
    public HBiTexProxy proxy() {
    	return this.proxy;
    }
    
    @Override
    public Object factory() {
        return this;
    }

    @Override
    public String connid() {
        return this.conf.connid;
    }
    
    public String uid() {
        return this.conf.uid;
    }

    @Override
    public List<String> topics() {
        return this.topics;
    }

    @Override
    public void addTopic(String topic) { 
        if (!this.topics.contains(topic)) {
            this.topics.add(topic);
        }
    }

    @Override
    public boolean removeTopics(String topic) {
        return this.topics.remove(topic);
    }
    
    @Override
    public JSONObject config() {
    	Headers _headers = this.headers();
        JSONObject json = new JSONObject();
        json.put("vender", "HBiTex");
        json.put("connectType", this.connectType());
        json.put("headers", ImmutableList.copyOf(_headers.iterator()).toString()); 
        json.put("wshost", this.conf.wshost);
        json.put("status", this.status().name());
        json.put("topic", this.topics());
        json.put("lastEvent", this.lastEvent == null ? "N/a" : (this.lastEvent + "/" + this.lastTime));
        json.put("lastPing", this.lastPing == null ? "N/a" : (this.lastPing + "/" + this.lease));
        if (this.auth != null && this.auth.has("accessKey")) {
            JSONObject authjson = new JSONObject();
            authjson.put("accessKey", TradiTexAssists.shorterContent(this.auth.getString("accessKey"), (short)8));
            authjson.put("secretKey", TradiTexAssists.shorterContent(this.auth.getString("secretKey"), (short)8));
            json.put("auth", authjson);
        }
        return json;
    }

    @Override
    public ConnectStatus status() {
    	if (this.wsHandler() == null) {
            return ConnectStatus.UNKNOW;
        }
        return this.wsHandler().getConnectStatus();
    }

    @Override
    public String genOTP(String connid) {
        if (this.config().has("auth")) {
            JSONObject auth = this.config().getJSONObject("auth");
            if (auth.has("accessKey") && auth.has("secretKey")) {
                return _otp(connid, auth.getString("accessKey"), auth.getString("secretKey"));
            }
        }
        return null;
    }
    
    @Override
    public Boolean otp(String connid, String otp) {
        if (this.config().has("auth")) {
            JSONObject auth = this.config().getJSONObject("auth");
            if (auth.has("accessKey") && auth.has("secretKey")) {
                String theOtp = _otp(connid, auth.getString("accessKey"), auth.getString("secretKey"));
                return theOtp.equals(otp);
            }
        }
        return false;
    }
    
    private String _otp(String connid, String accessKey, String secretKey) {
        return DigestUtils.md5Hex(String.format("%s:%s:%s", connid, accessKey, secretKey));
    }
    
    public void addHeaders(Headers theHeaders) {
        if (theHeaders == null) {
            return;
        }
    	Headers _headers = this.headers();
        Map<String, String> currentHeaderMap = new HashMap<>();
        if (_headers != null) {
        	_headers.forEach(a -> {
                currentHeaderMap.put(a.getFirst(), a.getSecond());
            });
        }
        theHeaders.forEach(a -> {
            currentHeaderMap.put(a.getFirst(), a.getSecond());
        });
        _headers = Headers.of(currentHeaderMap);
    }

    @Override
    public void shutdown() {
        LOGGER.info("HBiTex-关闭连接: connid={}, connectConfig={}", this.connid(), this.config());
        this.wsHandler().closeWebsocket();
        this.theader.shutdown();
    }
    
    /**
     * 建立 ws 连接
     */
    @Override
    public void connect(boolean retryer) {
        if (retryer) {
        	this.wsHandler().setConnectStatus(ConnectStatus.CONNECTING);
            LOGGER.info("HBiTex-WEBSOCKET-{}-启动重连: connid={}, connectConfig={}", this.conf.title, this.connid(), this.config());
            this.theader.start();
            return;
        }
        this.wsHandler().setConnectStatus(ConnectStatus.CONNECTING);
        LOGGER.info("HBiTex-WEBSOCKET-{}-建立连接: connid={}, connectConfig={}", this.conf.title, this.connid(), this.config());
        this.theader.start();
    }

    protected HttpClientResult doGet(String endpoint, Map<String, Object> queryMap) {
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = this.httpClient.doGetWithQueryParams(endpoint, this.headers(), queryMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (httpClientResult == null) {
            throw new RestException(R.TIME_OUT, "超时");
        }
        if (!httpClientResult.isSuccess()) {
            throw new RestException(R.FAILED, HttpAssets.toJsonString(httpClientResult));
        }
        return httpClientResult;
    }

    protected HttpClientResult doGet(String endpoint, Map<String, Object> queryMap, HBiTexAccount hbiTexAccount) {
        String connector = HBiTexFactory.getConnector("get", endpoint, queryMap, hbiTexAccount.getAccesskey(), hbiTexAccount.getSecreckey());
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = this.httpClient.doGetWithQueryParams(connector, this.headers(), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (httpClientResult == null) {
            throw new RestException(R.TIME_OUT, "超时");
        }
        if (!httpClientResult.isSuccess()) {
            throw new RestException(R.FAILED, HttpAssets.toJsonString(httpClientResult));
        }
        return httpClientResult;
    }
    
    public HttpClientResult doPost(String endpoint, Object bodyParam, HBiTexAccount hbiTexAccount) {
        String connector = HBiTexFactory.getConnector("post", endpoint, null, hbiTexAccount.getAccesskey(), hbiTexAccount.getSecreckey());
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = this.httpClient.postJsonBody(connector, this.headers(), this.httpClient.getJsonString(bodyParam));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (httpClientResult == null) {
            throw new RestException(R.TIME_OUT, "超时");
        }
        if (!httpClientResult.isSuccess()) {
            throw new RestException(R.FAILED, HttpAssets.toJsonString(httpClientResult));
        }
        return httpClientResult;
    }
    
    public static HttpClientResult doPostParam(HttpClient httpClient, Headers headers, String endpoint, Object bodyParam, HBiTexAccount hbiTexAccount) {
        String connector = HBiTexFactory.getConnector("post", endpoint, null, hbiTexAccount.getAccesskey(), hbiTexAccount.getSecreckey());
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = httpClient.postJsonBody(connector, headers, HttpAssets.toJsonString(bodyParam));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (httpClientResult == null) {
            throw new RestException(R.TIME_OUT, "超时");
        }
        if (!httpClientResult.isSuccess()) {
            throw new RestException(R.FAILED, HttpAssets.toJsonString(httpClientResult));
        }
        return httpClientResult;
    }
    
    public static String getConnector(String method, String hostname, Map<String, Object> queryMap, String accessKey, String secrecKey) {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });
        if (queryMap != null && queryMap.size() > 0) {
            map.putAll(queryMap);
        }
        map.put("AccessKeyId", accessKey);
        map.put("Timestamp", Instant.ofEpochSecond(Instant.now().getEpochSecond()).atZone(ZoneId.of("Z")).format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")));
        map.put("SignatureMethod", "HmacSHA256");
        map.put("SignatureVersion", 2);
        StringJoiner joiner = new StringJoiner("&");
        for (Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() == null) {
                continue;
            }
            joiner.add(e.getKey() + "=" + urlEncode(e.getValue().toString()));

        }
        String queryStringJoin = joiner.toString();
        URI uri = getURI(hostname);
        String payload = String.join("\n", method.toUpperCase(), uri.getHost(), uri.getPath(),queryStringJoin);
        String result = hostname + "?" + queryStringJoin + "&Signature=" + urlEncode(getSignature(payload, secrecKey));
        LOGGER.info("HBiTexConnectorString={}, payload={}", result, payload);
        return result;
    }

    public static String getSignature(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return encoderbase64(sha256_HMAC.doFinal(data.getBytes()));
        } catch (IllegalStateException | NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("errorMessage={}", e.getMessage());
        }
        return null;
    }

    public static String encoderbase64(byte[] str)  {
        try {
            return new String(new Base64().encode(str), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static URI getURI(String s) {
        try {
            return new URI(s.trim());
        } catch (URISyntaxException e) {
            LOGGER.error("url={}, errorMessage={}", s, e.getMessage(), e);
            return null;
        }
    }
    
    public static String urlEncode(String payload) {
        try {
            return URLEncoder.encode(payload, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public HBiTexAccount getAccount() {
        if (this.auth == null || !this.auth.has("secretKey")) {
            throw new IllegalStateException("未进行认证");
        }
    	Headers _headers = this.headers();
        String secretKey = this.auth.getString("secretKey");
        String accessKey = this.auth.getString("accessKey");
        String exchangeCode = _headers.get("cloud-exchange");
        String accountId = _headers.get("accountId");
        return new HBiTexAccount(accountId, accessKey, secretKey, exchangeCode);
    }
    
    // ws鉴权
    public void authentication(String accessKey, String secretKey) {
        this.auth.put("accessKey", accessKey);
        this.auth.put("secretKey", secretKey);
        Map<String, String> map = new HashMap<>();
        ApiSignature as = new ApiSignature();
        try {
            URI uri = new URI(this.conf.wshost);
            int port = uri.getPort();
            String theHost;
            if (port != -1) {
                theHost = uri.getHost() + ":" + uri.getPort();
            } else {
                theHost = uri.getHost();
            }
            as.createSignature(accessKey, secretKey, "GET", theHost, uri.getPath(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(ApiSignature.op, ApiSignature.opValue);
        map.put("cid", System.currentTimeMillis() + "");
        this.wsHandler().writeMessage(HttpAssets.toJsonString(map));
    }

    @Override
    public ITridexTradFactory putLastTime(Long timestamp) { 
        this.lastTime = timestamp;
        return this;
    }

    @Override
    public ITridexTradFactory putLastEvent(String event) { 
        this.lastEvent = event;
        return this;
    }

    @Override
    public void putLastPing(long epochMilli, long livetime) {
        this.lastPing = epochMilli;
        this.lease = this.lastPing + livetime; // 延长5分钟
    }

    @Override
    public Long lease() {
        return this.lease;
    }

}