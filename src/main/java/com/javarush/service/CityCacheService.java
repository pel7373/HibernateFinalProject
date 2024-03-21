package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityCacheDAO;
import com.javarush.domain.City;
import com.javarush.dto.CityCountry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javarush.config.Config.NUMBER_OF_REQUEST_TO_PUT_TO_CACHE;

public class CityCacheService {
    private static final CityCacheService INSTANCE = new CityCacheService();
    private final CityCacheDAO cityCacheDAO = CityCacheDAO.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, Integer> countRequestsCities = new HashMap<>();

    static int countercountRequestsAllCities = 0;
    private CityCacheService() {
    }

    static CityCacheService getInstance() {
        return INSTANCE;
    }

    void cacheAddHandlerList(List<City> listCity) {
        listCity.forEach(this::cacheAddHandler);
    }

    void cacheAddHandler(City city) {
        if(!CityService.mapperCityNameToId.containsKey(city.getName())) {
            CityService.mapperCityNameToId.put(city.getName(), city.getId());
        }

        if(countRequestsCities.containsKey(city.getId())) {
            countRequestsCities.put(city.getId(), countRequestsCities.get(city.getId()) + 1);
            if(countRequestsCities.get(city.getId()) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
                try {
                    String s = mapper.writeValueAsString(CityService.transformCityToCitycountry(city));
                    System.out.println(String.format("!!!! addHandler: city with id %d was put to cache from cacheAddHandler. CityCountry: %s", city.getId(), s));
                    //cityCacheDAO.add(String.valueOf(city.getId()), mapper.writeValueAsString(CityService.transformCityToCitycountry(city)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            countRequestsCities.put(city.getId(), 1);
        }
        System.out.println("!!! City with id: " + city.getId() + " was requested " + countRequestsCities.get(city.getId()) + " times");
    }

    void cacheDeleteById(Integer id) {
    }
    CityCountry cacheGetByName(String name) {
        return null;
    }

    CityCountry cacheGetById(Integer id) {
        CityCountry cityCountry = null;
        System.out.println(String.format("!!! cacheGetById: %d: data were read!!!!!!!!!", id));
/*
        try {
            System.out.println("!!! cacheGetById: data were read!!!!!!!!!");
            String value = cityCacheDAO.get(String.valueOf(id));
            cityCountry = mapper.readValue(String.valueOf(id), CityCountry.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("!!! Can't get City with id: " + id);
        }
*/
        return cityCountry;
    }

    boolean isMustGetFromCacheAll() {
        if(countercountRequestsAllCities >= 0) {

        }
        return false;
    }

    boolean isMustGetFromCache(String name) {
        return false;
    }

    boolean isMustGetFromCache(Integer id) {
        System.out.println("!!! CityId: " + id + ": isMustGetFromCache: "
                + (countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));
        if(countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
            return true;
        }
        return false;
    }
}
