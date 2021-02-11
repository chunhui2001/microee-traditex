package com.microee.traditex.inbox.up.hbitex.factory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBiTexThreader implements Runnable, UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(HBiTexThreader.class);
    
    private final String THREAD_NAME;
    private final HBiTexFactory factory;
    private AtomicInteger threadId = new AtomicInteger(0);
    private Thread currentThread;
    
    public static HBiTexThreader create(HBiTexFactory factory, String threadName) {
        return new HBiTexThreader(factory, threadName);
    }
    
    public HBiTexThreader(HBiTexFactory factory, String threadName) {
        this.factory = factory;
        this.THREAD_NAME = threadName;
    }
    
    @Override
    public void run() {
        this.factory.createWebSocket();
    }
    
    public Thread thread() {
        this.currentThread = new Thread(this, THREAD_NAME + this.threadId.incrementAndGet());
        this.currentThread.setUncaughtExceptionHandler(this);
        return this.currentThread;
    }
    
    public void start() {
        this.thread().start();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.info("异步线程异常: factory={}, threadId={}, threadName={}, errorMessage={}",  this.factory.config(), t.getId(), t.getName(), e.getMessage(), e);
        this.start();
    }

    public void shutdown() {
        if (this.currentThread != null) {
            this.currentThread.interrupt();
        }
    }
    
}