package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityCacheDAO;
import com.javarush.domain.City;
import com.javarush.dto.CityCountry;
import com.javarush.exception.CityNotFoundInCacheException;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;

import static com.javarush.config.Config.NUMBER_OF_REQUEST_TO_PUT_TO_CACHE;

public class CityCacheService {
    private static final CityCacheService INSTANCE = new CityCacheService();
    private final CityCacheDAO cityCacheDAO = CityCacheDAO.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, Integer> countRequestsCities = new HashMap<>();
    private Set<Integer> isInCache = new HashSet<>();

    static boolean isPutToCacheAllCitiesAndDoesntChanged = false;

    private CityCacheService() {
    }

    static CityCacheService getInstance() {
        return INSTANCE;
    }

    void cacheAddHandlerList(List<City> listCity) {
        if(!cityCacheDAO.isCacheRunning()) {
            System.out.println("Cache isn't available right now!");
            return;
        }
        listCity.forEach(this::cacheAddHandler);
        isPutToCacheAllCitiesAndDoesntChanged = true;
    }

    void cacheAddHandler(City city) {
        addCounterRequestsCityById(city.getId());

        if(!cityCacheDAO.isCacheRunning()) {
            System.out.println("Cache isn't available right now!");
            return;
        }
        if(countRequestsCities.get(city.getId()) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
            cacheAdd(city);
        }
        System.out.println("!!! City with id: " + city.getId() + " was requested " + countRequestsCities.get(city.getId()) + " times");
    }

    void cacheAdd(City city) {
        try {
            String s = mapper.writeValueAsString(CityService.transformCityToCitycountry(city));
            cityCacheDAO.add(String.valueOf(city.getId()), mapper.writeValueAsString(CityService.transformCityToCitycountry(city)));
            isInCache.add(city.getId());
            System.out.printf("!!!! addHandler: city with id %d was put to cache from cacheAdd. CityCountry: %s\n", city.getId(), s);
        } catch (JedisConnectionException e) {
            System.out.println("Can't connect to cache redis!");
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    void addCounterRequestsCityById(Integer id) {
        if(countRequestsCities.containsKey(id)) {
            countRequestsCities.put(id, countRequestsCities.get(id) + 1);
        } else {
            countRequestsCities.put(id, 1);
        }
        System.out.println("!!! City with id: " + id + " was requested " + countRequestsCities.get(id) + " times");
    }

    void cacheDeleteById(Integer id) {
        cityCacheDAO.del(String.valueOf(id));
        isPutToCacheAllCitiesAndDoesntChanged = false;
    }

    CityCountry cacheGetById(Integer id) {
        CityCountry cityCountry = null;
        try {
            String value = cityCacheDAO.get(String.valueOf(id));
            cityCountry = mapper.readValue(value, CityCountry.class);
            System.out.printf("!!! cacheGetById: data were read successfully! id: %d\n", id);
        } catch (JedisConnectionException e) {
            System.out.println("Can't connect to cache redis!");
            throw new CityNotFoundInCacheException(String.format("!!! Can't get City with id from cache: %d", id));
        }
        catch (JsonProcessingException | CityNotFoundInCacheException e) {
            System.out.println(String.format("!!! Can't get City with id from cache: %d\n", id));
            throw new CityNotFoundInCacheException(String.format("!!! Can't get City with id from cache: %d\n", id));
        }
        return cityCountry;
    }

    boolean isMustGetAllFromCache() {
        return isPutToCacheAllCitiesAndDoesntChanged;
    }

    List<CityCountry> cacheGetAll() {
        return null;
    }

    boolean isMustGetFromCache(Integer id) {
        if(!cityCacheDAO.isCacheRunning()) {
            System.out.println("Cache isn't available right now!");
            return false;
        }

        System.out.println("!!! CityId: " + id + ": isMustGetFromCache: "
                + (countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));
        if(countRequestsCities.containsKey(id)
                && (countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE || isInCache.contains(id))) {
            return true;
        }
        return false;
    }

    boolean isPutToCache(Integer id) {
        System.out.println("!!! CityId: " + id + ": isPutToCache: "
                + (countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));
        if(countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
            return true;
        }
        return false;
    }
}
