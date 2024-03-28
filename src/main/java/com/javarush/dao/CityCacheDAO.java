package com.javarush.dao;

import com.javarush.exception.CityNotFoundInCacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import static com.javarush.config.Config.REDIS_PORT;
import static com.javarush.config.Config.REDIS_URL;


public class CityCacheDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            CityCacheDAO.class);
    private static final CityCacheDAO INSTANCE = new CityCacheDAO();
    private Jedis jedis = new Jedis(REDIS_URL, REDIS_PORT);

    private CityCacheDAO() {
    }

    public void add(String key, String value) {
        jedis.set(key, value);
        LOGGER.info(String.format("key %s was put to cache", key));
    }

    public String get(String key) {
        String result;
        if(jedis. exists(key)) {
            result = jedis.get(key);
        } else {
            throw new CityNotFoundInCacheException(String.format("### City with id: %s was not found in cache", key));
        }
        LOGGER.info(String.format("key %s was get from cache", key));
        return result;
    }

    public void del(String key) {
        jedis.del(key);
        LOGGER.info(String.format("key %s was deleted from cache", key));
    }

    public static CityCacheDAO getInstance() {
        return INSTANCE;
    }
}
