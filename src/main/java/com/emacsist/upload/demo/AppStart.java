package com.emacsist.upload.demo;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class AppStart {

    private static volatile boolean run = true;

    @PostConstruct
    public void init() {
        Thread t = new Thread(() -> {
            final Runtime runtime = Runtime.getRuntime();
            while (run) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final long useMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("占用内存: " + useMemory / 1024 / 1024 + " MB" + ", tmp " + System.getProperty("java.io.tmpdir"));
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
