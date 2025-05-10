package com.example.users.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(JwtBlacklistService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token, long expirationMillis) {
        try {
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + token,
                    "true",
                    Duration.ofMillis(expirationMillis)
            );
            logger.info("Token blacklisted successfully. Expires in {} ms", expirationMillis);
        } catch (Exception e) {
            logger.error("Error blacklisting token", e);
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        try {
            String value = redisTemplate.opsForValue().get(BLACKLIST_PREFIX + token);
            return value != null;
        } catch (Exception e) {
            logger.error("Error checking token blacklist status", e);
            return false;
        }
    }
}