package com.microee.traditex.hello.app.components;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microee.stacks.kafka.consumer.KafkaSubscribe;
import com.microee.traditex.hello.app.process.HBiTexKLineProcess;
import com.microee.traditex.hello.app.props.AppConfigurationProps;
import com.microee.traditex.inbox.oem.constrants.InBoxMessageTypeEnum;
import com.microee.traditex.inbox.oem.constrants.VENDER;

@Component
public class KLineComsumer implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(KLineComsumer.class);

	@Autowired
	private AppConfigurationProps appConf;

	@Autowired
	private KafkaSubscribe kafkaSubscribe;

	@Autowired
	private HBiTexKLineProcess hbitexKLineProcess;

	@Override
	public void afterPropertiesSet() throws Exception {
		kafkaSubscribe.create(this::klineConsumerProccess, appConf.getKlineEventTopic()).start();
		kafkaSubscribe.create(this::klineConsumerProccess, appConf.getKlineEventTopic()).start();
		kafkaSubscribe.create(this::klineConsumerProccess, appConf.getKlineEventTopic()).start();
		kafkaSubscribe.create(this::klineConsumerProccess, appConf.getKlineEventTopic()).start();
	}

	// 处理所有k线
	public void klineConsumerProccess(ConsumerRecord<String, String> klineEventTopic) {
		String _message = klineEventTopic.value();
		JSONObject jsonObjectMessage = new JSONObject(_message);
		String venderString = jsonObjectMessage.getString("vender");
		String event = jsonObjectMessage.getString("event");
		if (!event.equalsIgnoreCase(InBoxMessageTypeEnum.KLINE.code)) {
			LOGGER.warn("当前消息不是K线: topic={}, messageKey={}, message={}", klineEventTopic.topic(), klineEventTopic.key(), _message);
			return;
		}
		VENDER vender = VENDER.get(venderString);
		if (vender == null) {
			LOGGER.warn("不支持的vender: topic={}, messageKey={}, message={}", klineEventTopic.topic(), klineEventTopic.key(), _message);
			return;
		}
		switch (vender) {
			case HBiTex:
				hbitexKLineProcess.process(jsonObjectMessage.getJSONObject("message"));
				break;
			default:
				LOGGER.warn("当前消息不是K线: topic={}, messageKey={}, message={}", klineEventTopic.topic(), klineEventTopic.key(), _message);
				break;
		}
	}

}
