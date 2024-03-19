package com.javarush.dao;

import com.javarush.domain.City;
import redis.clients.jedis.Jedis;

import static com.javarush.config.Config.REDIS_PORT;
import static com.javarush.config.Config.REDIS_URL;

public class CityCache {
    private static final CityCache INSTANCE = new CityCache();

    private final Jedis client = new Jedis(REDIS_URL, REDIS_PORT);

    private CityCache() {
    }

    public void add(String key, String value) {
        client.set(key, value);
    }
    public String get(String key) {
        return client.get(key);
    }

    public static CityCache getInstance() {
        return INSTANCE;
    }
}
