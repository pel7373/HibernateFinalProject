package com.javarush.service;

import com.javarush.entity.*;
import com.javarush.dao.CityDAO;
import com.javarush.dto.*;
import com.javarush.exception.CityNotFoundException;
import com.javarush.exception.CityNotFoundInCacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class CityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            CityService.class);
    private final CityDAO cityDAO = CityDAO.getInstance();
    CityCacheService cityCacheService = CityCacheService.getInstance();

    public void shutdown() {
        cityDAO.shutdown();
    }

    public List<CityCountry> getAll() {
        List<City> listCity = cityDAO.findAll();

        List<CityCountry> listCityCountry = new ArrayList<>();
        listCity.forEach(city -> listCityCountry.add(transformCityToCitycountry(city)));
        return listCityCountry;
    }

    public CityCountry getById(Integer id) {
        if(cityCacheService.isGetFromCache(id)) {
            try {
                return cityCacheService.getFromCacheById(id);
            } catch (CityNotFoundInCacheException e) {
                LOGGER.info(e.getMessage());
            }
        }

        City city = getCityById(id);
        cityCacheService.putToCache(city);
        return transformCityToCitycountry(city);
    }

    public City getCityById(Integer id) {
        return cityDAO.findById(id)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));
    }

    public List<CityCountry> getByName(String name) throws CityNotFoundException {
        LOGGER.info(String.format("getByName: name %s", name));
        List<City> listOfCities = cityDAO.findByName(name);
        if(listOfCities.isEmpty()) {
            throw new CityNotFoundException(String.format("City with name %s not found", name));
        }
        if(listOfCities.size() > 1) {
            LOGGER.info(String.format("getByName: more than one city with name %s", name));
        }
        List<CityCountry> list = new ArrayList<>();
        listOfCities.forEach(city -> list.add(transformCityToCitycountry(city)));
        return list;
    }

    public void save(City city) {
        cityDAO.save(city);
    }

    public void update(City city) {
        cityDAO.update(city);
    }

    public void deleteById(Integer id) {
        getById(id);
        cityDAO.deleteById(id);
        cityCacheService.cacheDeleteById(id);
        LOGGER.info(String.format("deleteById: deleted successfully: city id: %d", id));
    }

    public void clearCountersOfCache() {
        cityCacheService.clearCountersOfCache();
    }
    static CityCountry transformCityToCitycountry(City city) {
        CityCountry res = new CityCountry();
        res.setId(city.getId());
        res.setName(city.getName());
        res.setPopulation(city.getPopulation());
        res.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        res.setAlternativeCountryCode(country.getAlternativeCode());
        res.setContinent(country.getContinent());
        res.setCountryCode(country.getCode());
        res.setCountryName(country.getName());
        res.setCountryPopulation(country.getPopulation());
        res.setCountryRegion(country.getRegion());
        res.setCountrySurfaceArea(country.getSurfaceArea());
        Set<CountryLanguage> countryLanguages = country.getLanguages();
        Set<Language> languages = countryLanguages.stream().map(cl -> {
            Language language = new Language();
            language.setLanguage(cl.getLanguage());
            language.setOfficial(cl.getOfficial());
            language.setPercentage(cl.getPercentage());
            return language;
        }).collect(Collectors.toSet());
        res.setLanguages(languages);
        return res;
    }
}