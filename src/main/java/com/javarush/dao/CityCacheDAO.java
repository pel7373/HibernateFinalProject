package com.javarush.dao;

import com.javarush.exception.CityNotFoundInCacheException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import static com.javarush.config.Config.REDIS_PORT;
import static com.javarush.config.Config.REDIS_URL;

public class CityCacheDAO {
    private static final CityCacheDAO INSTANCE = new CityCacheDAO();

    private Jedis jedis = new Jedis(REDIS_URL, REDIS_PORT);
    private boolean isJedisAvailable = true;

    private CityCacheDAO() {
    }

    public void add(String key, String value) {
        isJedisAvailable = false;
        jedis.set(key, value);
        System.out.println("### CityCacheDao: key " + key + " was put to cache!");
        isJedisAvailable = true;
    }
    public String get(String key) {
        String result;
        if(jedis.exists(key)) {
            result = jedis.get(key);
        } else {
            throw new CityNotFoundInCacheException(String.format("### City with id: %s was not found in cache", key));
        }
        System.out.println("### CityCacheDao: key " + key + " was get from cache!");
        return result;
    }

    public void del(String key) {
        System.out.println("### key: " + key + " was delete from cache!");
        jedis.del(key);
    }

    public static CityCacheDAO getInstance() {
        return INSTANCE;
    }

    public boolean isCacheRunning() {
        return isJedisAvailable;
    }
}
