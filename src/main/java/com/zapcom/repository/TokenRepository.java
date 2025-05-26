package com.zapcom.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class TokenRepository {
    private final RedisTemplate redisTemplate;
    private static final String ACCESS_TOKEN_KEY_PREFIX = "user:access:";
    private static final String ACCESS_BLACKLIST_PREFIX = "blacklist:access:";
    @Value("${jwt.EXPIRATION_TIME}")
    private long EXPIRATION_TIME;

    public void storeToken(String username, String accessToken) {
        log.info("store token");
        String accessKey = ACCESS_TOKEN_KEY_PREFIX + username;
        storeToken(accessKey, accessToken, EXPIRATION_TIME);
    }

    private void storeToken(String key, String token, long expiration) {
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key, expiration, TimeUnit.MILLISECONDS);
    }

    public String getAccessTokenKey(String username) {
        String accessKey = ACCESS_TOKEN_KEY_PREFIX + username;
        return getToken(accessKey);
    }

    private String getToken(String accessKey) {
        Object token = redisTemplate.opsForValue().get(accessKey);
        return token != null ? token.toString() : null;
    }

    public void removeToken(String username) {
        String accessToken = getAccessTokenKey(username);
        String accessKey = ACCESS_TOKEN_KEY_PREFIX + username;
        redisTemplate.delete(accessKey);

        if (accessToken != null) {
            String accessBlacklistKey = ACCESS_BLACKLIST_PREFIX + accessToken;
            blacklistToken(accessBlacklistKey, EXPIRATION_TIME);
        }
    }

    private void blacklistToken(String blacklistKey, long expiration) {
        redisTemplate.opsForValue().set(blacklistKey, expiration);
        redisTemplate.expire(blacklistKey, expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isAccessTokenBlacklisted(String token) {
        String key = ACCESS_BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void removeAccessToken(String username) {
        String accessToken = getToken(username);
        String accessKey = ACCESS_TOKEN_KEY_PREFIX + username;
        redisTemplate.delete(accessKey);
        String accessBlacklistKey = ACCESS_BLACKLIST_PREFIX + accessToken;
        blacklistToken(accessBlacklistKey, EXPIRATION_TIME);
    }
}
