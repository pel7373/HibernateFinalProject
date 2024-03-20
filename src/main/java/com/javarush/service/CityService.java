package com.javarush.service;

import com.javarush.domain.*;
import com.javarush.dao.CityDAO;
import com.javarush.exception.CityNotFoundException;

import java.util.List;

public class CityService {
    private final CityDAO cityDAO = CityDAO.getInstance();
    CityCacheService cityCacheService = CityCacheService.getInstance();

    public List<City> getAll() {
        List<City> listCity = cityDAO.findAll();
        cityCacheService.cacheAddHandlerList(listCity);
        return listCity;
    }

    public City getById(Integer id) {
        if (cityCacheService.isGetFromCache(id)) {
            return cityCacheService.cacheGetById(id);
        }

        City city = cityDAO.findById(id)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));
        cityCacheService.cacheAddHandler(city);
        return city;
    }

    public void deleteById(Integer id) {
        getById(id);
        cityDAO.deleteById(id);
        cityCacheService.cacheDeleteById(id);
    }

    public City getByName(String name) {
        return cityDAO.findByName(name)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with name %S not found", name)));
    }
}
