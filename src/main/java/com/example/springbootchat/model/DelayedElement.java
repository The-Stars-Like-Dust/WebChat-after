package com.example.springbootchat.model;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayedElement implements Delayed {
    private long delayTime;  // 延迟时间
    private long expireTime; // 到期时间
    private String key;

    // 构造方法
    public DelayedElement(long delayTime, String key) {
        this.key = key;
        this.delayTime = delayTime;
        this.expireTime = System.currentTimeMillis() + delayTime;
    }


    // 获取剩余延迟时间
    @Override
    public long getDelay(TimeUnit unit) {
        long remainingTime = expireTime - System.currentTimeMillis();
        return unit.convert(remainingTime, TimeUnit.MILLISECONDS);
    }

    // 定义延迟元素的顺序
    @Override
    public int compareTo(Delayed other) {
        long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
        return Long.compare(diff, 0);
    }
}
