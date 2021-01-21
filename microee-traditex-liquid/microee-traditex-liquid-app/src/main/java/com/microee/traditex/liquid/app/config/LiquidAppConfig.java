package com.microee.traditex.liquid.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import com.microee.plugin.thread.NamedThreadFactory;
import com.microee.stacks.redis.support.RedisMessageListenerRegistry;
import com.microee.traditex.liquid.app.message.LiquidRedisMessageListener;

@Configuration
public class LiquidAppConfig implements SchedulingConfigurer, ApplicationListener<ApplicationEvent>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidAppConfig.class);

    @Value("${topic.traditex.httplog.listener.message}")
    private String tradiTexHttpLogListererMessageTopic;
    
    @Autowired
    private LiquidRedisMessageListener redisMessageListener;

    @Autowired
    private RedisMessageListenerRegistry redisMessageListenerRegistry;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.initialize();
        taskScheduler.setThreadFactory(new NamedThreadFactory("LIQUID-TASK-THREAD"));
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
    
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        return redisMessageListenerRegistry.add(redisMessageListener, tradiTexHttpLogListererMessageTopic).get();
    }
    
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            LOGGER.info("microee-traditex-liquid-app 启动成功");
        }
    }

}
