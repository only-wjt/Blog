package com.onlyWjt.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class TestJob {
    @Scheduled(cron = "0/5 * * * * ?")
    public void testjob(){
        System.out.println("当前时间：：" + System.currentTimeMillis());
    }
}
