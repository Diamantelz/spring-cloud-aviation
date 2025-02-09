package ru.itis.flightbookingservice.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Map<String,String>> bookingStatusCache() {
        return Caffeine.newBuilder().build();
    }
}
