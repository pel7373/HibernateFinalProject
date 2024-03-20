package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityCacheDAO;
import com.javarush.domain.City;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.javarush.config.Config.NUMBER_OF_REQUEST_TO_PUT_TO_CACHE;

public class CityCacheService {
    private static final CityCacheService INSTANCE = new CityCacheService();
    private final CityCacheDAO cityCacheDAO = CityCacheDAO.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Long> mapperCityNameToId = new HashMap<>();
    private Map<Long, Integer> countRequestsCities = new HashMap<>();

    private CityCacheService() {
    }

    public static CityCacheService getInstance() {
        return INSTANCE;
    }
    public void cacheAddHandlerList(List<City> listCity) {
        listCity.forEach(this::cacheAddHandler);
    }

    public void cacheAddHandler(City city) {
        if(countRequestsCities.containsKey(city.getId())) {
            countRequestsCities.put(city.getId(), countRequestsCities.get(city.getId() + 1));
            if(countRequestsCities.get(city.getId()) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
                try {
                    cityCacheDAO.add(String.valueOf(city.getId()), mapper.writeValueAsString(city));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            countRequestsCities.put(city.getId(), 1);
        }
    }

    public void cacheDeleteById(Long id) {
    }
    private City cacheGetByName(String name) {
        return null;
    }

    public City cacheGetById(Long cityId) {
        return null;
    }

    public boolean isExistInCache(String name) {
        return false;
    }

    public boolean isExistInCache(Long id) {
        return false;
    }

    /*try {
        mapper.readValue(value, CityCountry.class);
    } catch (
    JsonProcessingException e) {
        e.printStackTrace();
    }*/

}
