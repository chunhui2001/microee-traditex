package com.microee.traditex.hello.app.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.microee.plugin.http.assets.HttpAssets;
import com.microee.stacks.redis.support.RedisZSet;

@Service
public class KLineRepository {

	private static final String _KLINE_CACHE_PREFIX = "__hbitex_kline_cache";
	
	@Autowired
	private RedisZSet redisZSet;
	
	public Map<Long, Double[]> fetchHBitexKLine(@NotNull Long ts, @NotNull String symbol, @NotNull String period, @NotNull JSONObject lastedKLine) {
		Function<JSONObject, Double[]> func = (_tick) -> {
			return new Double[] { _tick.getDouble("open"), _tick.getDouble("high"), _tick.getDouble("low"), _tick.getDouble("close") };
		};
		String _cacheKey = String.join(":", _KLINE_CACHE_PREFIX, symbol, period);
		redisZSet.incr(_cacheKey, HttpAssets.toJsonString(ImmutableMap.of(ts, func.apply(lastedKLine.getJSONObject("tick")))), ts);
		redisZSet.del(_cacheKey, 1);
		Set<Object> set = redisZSet.top(_cacheKey, 1);
		Map<Long, Double[]> result = new LinkedHashMap<>();
		for (Object i : set) {
			Map<Long, Double[]> _map = HttpAssets.parseJson(i.toString(), new TypeReference<Map<Long, Double[]>>() {});
			Long _ts = _map.keySet().iterator().next();
			Double[] _val = _map.get(_ts);
			result.put(_ts, _val);
		}
		return result;
	}
	
}
