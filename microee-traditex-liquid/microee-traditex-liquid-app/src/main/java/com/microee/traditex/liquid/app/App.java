package com.microee.traditex.liquid.app;

import java.text.ParseException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.microee.stacks.es6.config.ElasticSearchEnabled;
import com.microee.stacks.redis.config.RedisEnabled;
import com.microee.stacks.starter.MainApp;

@EnableAutoConfiguration()
@ComponentScan(basePackages = {"com.microee"})
@SpringBootApplication
@RedisEnabled()
@ElasticSearchEnabled()
public class App extends MainApp {
    public static void main(String[] args) throws ParseException {
        startup(App.class, args);
    }
}

