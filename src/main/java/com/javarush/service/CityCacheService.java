package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityCacheDAO;
import com.javarush.dao.CityDAO;
import com.javarush.domain.City;
import com.javarush.dto.CityCountry;
import com.javarush.exception.CityNotFoundInCacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;

import static com.javarush.config.Config.NUMBER_OF_REQUEST_TO_PUT_TO_CACHE;

public class CityCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            CityCacheService.class);
    private static final CityCacheService INSTANCE = new CityCacheService();
    private final CityCacheDAO cityCacheDAO = CityCacheDAO.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, Integer> countRequestsCitiesById = new HashMap<>();
    private Set<Integer> isInCache = new HashSet<>();

    private CityCacheService() {
    }

    static CityCacheService getInstance() {
        return INSTANCE;
    }

    void clearCountersOfCache() {
        countRequestsCitiesById = new HashMap<>();
        isInCache = new HashSet<>();
    }

    void cacheAdd(City city) {
        try {
            String citycountryInString = mapper.writeValueAsString(CityService.transformCityToCitycountry(city));
            cityCacheDAO.add(String.valueOf(city.getId()), citycountryInString);
            isInCache.add(city.getId());
            LOGGER.info(String.format("City with id %d was put to cache from cacheAdd", city.getId()));
        } catch (JedisConnectionException e) {
            System.out.println("Can't connect to cache redis!");
        }
        catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
        }
    }

    void incrementCounterRequestsCityById(Integer id) {
        if(countRequestsCitiesById.containsKey(id)) {
            countRequestsCitiesById.put(id, countRequestsCitiesById.get(id) + 1);
        } else {
            countRequestsCitiesById.put(id, 1);
        }
        LOGGER.info(String.format("City with id: %d was requested %d times", id, countRequestsCitiesById.get(id)));
    }

    void deleteCounterRequestsCityById(Integer id) {
        countRequestsCitiesById.remove(id);
    }

    void cacheDeleteById(Integer id) {
        try {
            cityCacheDAO.del(String.valueOf(id));
        } catch (JedisConnectionException e) {
            LOGGER.info(e.getMessage());
        }

        deleteCounterRequestsCityById(id);
        isInCache.remove(id);
    }

    CityCountry getFromCacheById(Integer id) {
        CityCountry cityCountry = null;
        try {
            String value = cityCacheDAO.get(String.valueOf(id));
            cityCountry = mapper.readValue(value, CityCountry.class);
            LOGGER.info(String.format("cacheGetById: data were read successfully! id: %d", id));
        } catch (JedisConnectionException e) {
            LOGGER.info("getFromCacheById: can't connect to cache redis!");
            throw new CityNotFoundInCacheException(String.format("!!! Can't get City with id from cache: %d", id));
        }
        catch (JsonProcessingException | CityNotFoundInCacheException e) {
            LOGGER.info(String.format("Can't get City with id from cache: %d", id));
            throw new CityNotFoundInCacheException(String.format("Can't get City with id from cache: %d\n", id));
        }
        return cityCountry;
    }

    boolean isGetFromCache(Integer id) {
        LOGGER.info("CityId: " + id + ": isMustGetFromCache: "
                + (countRequestsCitiesById.containsKey(id)
                && countRequestsCitiesById.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));

        if(countRequestsCitiesById.containsKey(id)
                && (countRequestsCitiesById.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE
                    || isInCache.contains(id))) {
            return true;
        }
        return false;
    }

    void putToCache(City city) {
        incrementCounterRequestsCityById(city.getId());
        if(isPutToCache(city.getId())) {
            try {
                cacheAdd(city);
            } catch (JedisConnectionException e) {
                LOGGER.info("putToCache: can't connect to cache redis!");
            }
        }
    }

    boolean isPutToCache(Integer id) {
        LOGGER.info("!!! CityId: " + id + ": isPutToCache: "
                + (countRequestsCitiesById.containsKey(id)
                && countRequestsCitiesById.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));
        if(countRequestsCitiesById.containsKey(id)
                && countRequestsCitiesById.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
            return true;
        }
        return false;
    }
}