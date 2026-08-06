package com.zomato.app.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RedisCacheService {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public Map<String, String> all() {
        return cache;
    }

    public Map<String, String> put(String key, String value) {
        cache.put(key, value);
        return cache;
    }
}
