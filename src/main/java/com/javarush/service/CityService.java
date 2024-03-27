package com.javarush.service;

import com.javarush.domain.*;
import com.javarush.dao.CityDAO;
import com.javarush.dto.CityCountry;
import com.javarush.dto.Language;
import com.javarush.exception.CityNotFoundException;
import com.javarush.exception.CityNotFoundInCacheException;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;
import java.util.stream.Collectors;

public class CityService {
    private final CityDAO cityDAO = CityDAO.getInstance();
    CityCacheService cityCacheService = CityCacheService.getInstance();

    public List<CityCountry> getAll() {
        List<City> listCity = cityDAO.findAll();
        List<CityCountry> listCitycountry = new ArrayList<>();
        listCity.forEach(city -> listCitycountry.add(transformCityToCitycountry(city)));
        return listCitycountry;
    }

    public CityCountry getById(Integer id) {
        if(cityCacheService.isGetFromCache(id)) {
            return cityCacheService.getFromCacheById(id);
        }
        City city = getCityById(id);

        cityCacheService.putToCache(city);

        return transformCityToCitycountry(city);
    }

    public City getCityById(Integer id) {
        City city = cityDAO.findById(id)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));

        return city;
    }

    public List<CityCountry> getByName(String name) throws CityNotFoundException {
        System.out.printf("!!! CityService: try to get by name: %s\n", name);
        List<City> listOfCities = cityDAO.findByName(name);
        if(listOfCities.isEmpty()) {
            throw new CityNotFoundException(String.format("City with name %s not found", name));
        }
        if(listOfCities.size() > 1) {
            System.out.printf("!!! There are more than one city with name: %s\n", name);

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
        System.out.printf("!!!Deleted successfully: city id: %d\n", id);
        removeFromMappersCityNameToIdAndViseVersa(getNameById(id));
        isDBchanged = true;
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

    static int getIdByName(String name) throws CityNotFoundException {
        if(mapperCityNameToId.containsKey(name)) {
            return mapperCityNameToId.get(name);
        } else {
            throw new CityNotFoundException(String.format("Name %s wasn't found in mapperCityNameToId", name));
        }
    }

    static String getNameById(Integer id) throws CityNotFoundException {
        if(mapperCityIdToName.containsKey(id)) {
            return mapperCityIdToName.get(id);
        } else {
            throw new CityNotFoundException(String.format("Id %d wasn't found in mapperCityIdToName", id));
        }
    }

}
