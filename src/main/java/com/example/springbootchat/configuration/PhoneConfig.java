package com.example.springbootchat.configuration;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author 86175
 */
@Slf4j
@Configuration
public class PhoneConfig {
    // 从application.yaml文件中获取相关配置信息
    @Value("${phone.signName}")
    private String signName;
    @Value("${phone.templateCode}")
    private String templateCode;
    @Value("${phone.accessKeyId}")
    private String accessKeyId;
    @Value("${phone.accessKeySecret}")
    private String accessKeySecret;

    // 创建一个Config Bean，用于存储accessKeyId和accessKeySecret
    @Bean
    public Config phConfig() {
        Config config = new Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        return config;
    }

    // 创建一个SendSmsRequest对象，用于发送短信
    public SendSmsRequest getSendSmsRequest() {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.signName = signName;
        sendSmsRequest.templateCode = templateCode;
        return sendSmsRequest;
    }

    // 创建一个Client Bean，用于发送短信
    @Bean
    public Client getClient(Config phConfig) throws Exception {
        return new Client(phConfig);
    }

    // 创建一个Random Bean，用于生成随机数
    @Bean
    public Random getRandom() {
        return new Random();
    }

    // 创建一个Pattern Bean，用于验证手机号码格式
    @Bean
    public Pattern getPattern() {
        // 定义手机号的正则表达式
        String regex = "^1[3-9]\\d{9}$";
        // 编译正则表达式
        return Pattern.compile(regex);
    }
}
