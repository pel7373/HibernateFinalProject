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
    static Map<String, Integer> mapperCityNameToId = new HashMap<>();
    static Map<Integer, String> mapperCityIdToName = new HashMap<>();

    static boolean isDBchanged = false;

    public List<CityCountry> getAll() {
        if(cityCacheService.isMustGetAllFromCache()) {
            return cityCacheService.cacheGetAll();
        }

        List<City> listCity = cityDAO.findAll();
        List<CityCountry> listCitycountry = new ArrayList<>();
        listCity.forEach(city -> {
            listCitycountry.add(transformCityToCitycountry(city));
            addMappersCityNameToIdAndViseVersa(city.getName(), city.getId());
        });
        cityCacheService.cacheAddHandlerList(listCity);
        return listCitycountry;
    }

    public City getCityById(Integer id) {
        City city = cityDAO.findById(id)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with id %d not found", id)));

        addMappersCityNameToIdAndViseVersa(city.getName(), city.getId());
        cityCacheService.addCounterRequestsCityById(city.getId());
        if(cityCacheService.isPutToCache(id)) {
            try {
                cityCacheService.cacheAdd(city);
            } catch (JedisConnectionException e) {
                System.out.println("Can't connect to cache redis!");
            }
        }
        return city;
    }

    public CityCountry getById(Integer id) {
        if(cityCacheService.isMustGetFromCache(id)) {
            try {
                return cityCacheService.cacheGetById(id);
            } catch (CityNotFoundInCacheException e) {
                System.out.println(String.format("CityNotFoundInCacheException. City will be get from DB. id: %d", id));
            }
        }

        try {
            return transformCityToCitycountry(getCityById(id));
        }  catch (CityNotFoundException e) {
            throw new CityNotFoundException(e.getMessage());
        }
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

    private void addMappersCityNameToIdAndViseVersa(String name, int id) {
            mapperCityNameToId.put(name, id);
            mapperCityIdToName.put(id, name);
    }

    private void removeFromMappersCityNameToIdAndViseVersa(String name) {
        Integer id = null;
        if(mapperCityNameToId.containsKey(name)) {
            id = mapperCityNameToId.get(name);
            mapperCityNameToId.remove(name);
        }
        if(mapperCityIdToName.containsKey(id)) {
            mapperCityIdToName.remove(id);
        }
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
