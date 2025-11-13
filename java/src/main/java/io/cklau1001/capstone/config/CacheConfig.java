package io.cklau1001.capstone.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache dealersCache = new CaffeineCache("dealers",
                Caffeine.newBuilder()
                        .expireAfterAccess(Duration.ofMinutes(30))
                        .expireAfterWrite(Duration.ofMinutes(30))
                        .maximumSize(100)
                        .build());

        CaffeineCache reviewsCache = new CaffeineCache("reviews",
                Caffeine.newBuilder()
                        .expireAfterAccess(Duration.ofMinutes(30))
                        .expireAfterWrite(Duration.ofMinutes(30))
                        .maximumSize(100)
                        .build());

        CaffeineCache carInventoryCache = new CaffeineCache("carInventory",
                Caffeine.newBuilder()
                        .expireAfterAccess(Duration.ofMinutes(30))
                        .expireAfterWrite(Duration.ofMinutes(30))
                        .maximumSize(10)
                        .build());

        cacheManager.setCaches(List.of(dealersCache, reviewsCache, carInventoryCache));
        log.info("[cacheManager]: Created cacheManager");

        return cacheManager;
    }

}
