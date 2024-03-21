package com.javarush.service;

import com.javarush.domain.*;
import com.javarush.dao.CityDAO;
import com.javarush.dto.CityCountry;
import com.javarush.dto.Language;
import com.javarush.exception.CityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.*;
import java.util.stream.Collectors;

public class CityService {
    private final CityDAO cityDAO = CityDAO.getInstance();
    CityCacheService cityCacheService = CityCacheService.getInstance();
    static Map<String, Integer> mapperCityNameToId = new HashMap<>();

    public List<CityCountry> getAll() {
        List<City> listCity = cityDAO.findAll();
        List<CityCountry> listCitycountry = new ArrayList<>();
        //cityCacheService.cacheAddHandlerList(listCity);
        listCity.forEach(city -> {
            listCitycountry.add(transformCityToCitycountry(city));
        });
        return listCitycountry;
    }

    public CityCountry getById(Integer id) {
        if(cityCacheService.isMustGetFromCache(id)) {
            return cityCacheService.cacheGetById(id);
        }
        City city = null;
        try {
            city = cityDAO.findById(id)
                    .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));
        } catch (NoResultException e) {
            throw new CityNotFoundException(String.format("City with id %d not found", id));
        }
        cityCacheService.cacheAddHandler(city);
        return transformCityToCitycountry(city);
    }

    public void deleteById(Integer id) {
        getById(id);
        cityDAO.deleteById(id);
    }

    public City getByName(String name) {
        return cityDAO.findByName(name)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with name %S not found", name)));
    }

    public static CityCountry transformCityToCitycountry(City city) {
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
