package com.javarush.service;

import com.javarush.dao.CityCache;
import com.javarush.domain.*;
import com.javarush.dao.CityDAO;
import com.javarush.exception.CityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javarush.config.Config.NUMBER_OF_REQUEST_TO_PUT_TO_CACHE;

public class CityService {
    private final CityDAO cityDAO = CityDAO.getInstance();
    private final CityCache cityCache = CityCache.getInstance();

    private Map<String, Integer> cacheByName = new HashMap<>();
    private Map<Long, Integer> cacheById = new HashMap<>();

    public List<City> getAll() {
        return cityDAO.findAll();
    }

    public City getById(Long id) {

        City city = cityDAO.findById(id)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));
        return city;
    }

    public void deleteById(Long id) {
        getById(id);
        cityDAO.deleteById(id);
    }

    public City getByName(String name) {

//        if(cacheById.containsKey(id)
//                && cacheById.get(id) >= NUMBER_OF_REQUEST_TO_PUT_TO_CACHE - 1) {
//
//        }

        return cityDAO.findByName(name)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with name %S not found", name)));
    }
}
