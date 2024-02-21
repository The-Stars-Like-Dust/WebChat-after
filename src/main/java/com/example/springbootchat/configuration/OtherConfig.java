package com.example.springbootchat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;

@Configuration
public class OtherConfig {
    @Bean
    public KeyGenerator getKeyGenerator() throws Exception {
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(128);
        return aes;
    }
}
