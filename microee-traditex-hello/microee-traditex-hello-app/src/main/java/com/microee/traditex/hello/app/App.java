package com.microee.traditex.hello.app;

import java.text.ParseException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.microee.stacks.redis.config.RedisEnabled;
import com.microee.stacks.starter.MainApp;

@EnableAutoConfiguration()
@ComponentScan(basePackages = {"com.microee"})
@SpringBootApplication
@RedisEnabled()
public class App extends MainApp {
    public static void main(String[] args) throws ParseException {
        startup(App.class, args);
    }
}
