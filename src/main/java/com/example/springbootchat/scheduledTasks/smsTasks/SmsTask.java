package com.example.springbootchat.scheduledTasks.smsTasks;

import com.example.springbootchat.service.smsService.SMSService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SmsTask {
    @Resource
    private SMSService smsService;

    // 每天0点清空发送短信的次数
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearEveryDayCount() {
        smsService.clearTheNumberOfSendsPerDay();
    }

    // 每周一 0点清空发送短信的次数
    @Scheduled(cron = "0 0 0 ? * MON")
    public void clearEveryWeekCount() {
        smsService.clearTheNumberOfSendsPerWeek();
    }

    // 每月1号 0点清空发送短信的次数
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearEveryMonthCount() {
        smsService.clearTheNumberOfSendsPerMonth();
    }
}
