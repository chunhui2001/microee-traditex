package com.microee.traditex.dashboard.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import com.microee.plugin.thread.NamedThreadFactory;
import com.microee.stacks.redis.support.RedisMessageListenerRegistry;
import com.microee.traditex.dashboard.app.message.RedisMessageListener;

@Configuration
public class TradiTexAppConfig
        implements SchedulingConfigurer, ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradiTexAppConfig.class);

    @Value("${topic.traditex.orderbook.message}")
    private String tradiTexOrderBookMessageTopic;

    @Value("${topic.traditex.pricing.message}")
    private String tradiTexPricingTopic;

    @Value("${topic.traditex.diskorders.message}")
    private String tradiTexDiskOrdersMessageTopic;

    @Value("${topic.traditex.revokeorder.count.message}")
    private String tradiTexRevokeOrderCountMessageTopic;

    @Value("${topic.traditex.httpnetwork.message}")
    private String tradiTexHttpNetworkMessageTopic;

    @Value("${topic.traditex.account.balance.message}")
    private String tradiTexAccountBalanceMessageTopic;

    @Value("${topic.traditex.account-disk.balancess.message}")
    private String tradiTexAccountDiskBalancessMessageTopic;

    @Value("${topic.traditex.account-solr.balancess.message}")
    private String tradiTexAccountSolrBalancessMessageTopic;

    @Value("${topic.traditex.httplog.listener.message}")
    private String tradiTexHttpLogListererMessageTopic;

    @Autowired
    private RedisMessageListener redisMessageListener;

    @Autowired
    private RedisMessageListenerRegistry redisMessageListenerRegistry;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.initialize();
        taskScheduler.setThreadFactory(new NamedThreadFactory("ARCHIVE-THREAD"));
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Autowired RedisConnectionFactory redisConnectionFactory) {
        String[] topics = new String[] {tradiTexPricingTopic, tradiTexOrderBookMessageTopic,
                tradiTexDiskOrdersMessageTopic, tradiTexHttpNetworkMessageTopic,
                tradiTexRevokeOrderCountMessageTopic, tradiTexAccountBalanceMessageTopic,
                tradiTexAccountDiskBalancessMessageTopic, tradiTexAccountSolrBalancessMessageTopic,
                tradiTexHttpLogListererMessageTopic};
        return redisMessageListenerRegistry.add(redisMessageListener, topics).get();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            LOGGER.info("TradiTex dashboard 启动成功");
        }
    }
}
