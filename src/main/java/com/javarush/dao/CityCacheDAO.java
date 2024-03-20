package com.javarush.dao;

import redis.clients.jedis.Jedis;

import static com.javarush.config.Config.REDIS_PORT;
import static com.javarush.config.Config.REDIS_URL;

public class CityCacheDAO {
    private static final CityCacheDAO INSTANCE = new CityCacheDAO();

    private final Jedis client = new Jedis(REDIS_URL, REDIS_PORT);

    private CityCacheDAO() {
    }

    public void add(String key, String value) {
        client.set(key, value);
    }
    public String get(String key) {
        return client.get(key);
    }

    public static CityCacheDAO getInstance() {
        return INSTANCE;
    }
}
