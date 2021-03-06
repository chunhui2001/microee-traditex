package com.microee.traditex.hello.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class TradiTexHelloConfig implements SchedulingConfigurer, ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradiTexHelloConfig.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        taskScheduler.setPoolSize(3);
        taskScheduler.setThreadNamePrefix("TRADITEX-INBOX-TASK-THREAD-");
        taskScheduler.setErrorHandler((e) -> {
            LOGGER.error("异步线程异常: threadId={}, threadName={}, errorMessage={}", Thread.currentThread().getId(), Thread.currentThread().getName(), e.getMessage(), e);
        });
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            LOGGER.info("应用程序[{}]启动成功,端口={}", applicationName, serverPort);
        }
        if (event instanceof ContextClosedEvent) {
            LOGGER.info("应用程序[{}]已安全退出, evnent={}", applicationName, event.getClass().getName());
        }
    }

}
