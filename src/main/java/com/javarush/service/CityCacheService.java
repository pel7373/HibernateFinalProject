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

    private Map<String, Integer> mapperCityNameToId = new HashMap<>();
    private Map<Integer, Integer> countRequestsCities = new HashMap<>();

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
            countRequestsCities.put(city.getId(), countRequestsCities.get(city.getId()) + 1);
            if(countRequestsCities.get(city.getId()) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
                try {
                    cityCacheDAO.add(String.valueOf(city.getId()), mapper.writeValueAsString(city));
                    System.out.println("!!! City with id: " + city.getId() + " was put to cache from cacheAddHandler" );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            countRequestsCities.put(city.getId(), 1);
        }
        System.out.println("!!! City with id: " + city.getId() + " was requested " + countRequestsCities.get(city.getId()) + " times");
    }

    public void cacheDeleteById(Integer id) {
    }
    private City cacheGetByName(String name) {
        return null;
    }

    public City cacheGetById(Integer id) {
        City city = null;
        try {
            city = mapper.readValue(String.valueOf(id), City.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("!!! Can't get City with id: " + id);
        }
        System.out.println("!!! get city by id: " + id + ":");
        System.out.println(city);
        return city;
    }

    public boolean isGetFromCache(String name) {
        return false;
    }

    public boolean isGetFromCache(Integer id) {
        System.out.println("!!! CityId: " + id + ": isGetFromCache: "
        + (countRequestsCities.containsKey(id)
                && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE));
        if(countRequestsCities.containsKey(id)
            && countRequestsCities.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE) {
            return true;
        }
        return false;
    }



}
