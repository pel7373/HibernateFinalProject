package com.javarush.controller;

import com.javarush.dao.CityDAO;
import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import com.javarush.dto.CityCountry;
import com.javarush.exception.CityNotFoundException;
import com.javarush.service.CityService;
import com.javarush.service.CountryService;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.javarush.controller.Operation.*;

public class MyController implements Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            MyController.class);

    private String[] params;
    private static final String REPORT_HEADER = "###Report for user: ";
    CityService cityService = new CityService();
    CountryService countryService = new CountryService();

    public MyController(String[] params) {
        this.params = params;
    }

    @Override
    public void handler(int choiceOperation) {

        if(choiceOperation == GET_SOME_CITIES_FROM_DB_AND_CACHE.ordinal()) {
            testSomeCitiesFromDbAndCache();
        } else if(choiceOperation == GET_ALL_CITIES_FROM_DB_AND_CACHE.ordinal()) {
            testAllCitiesFromDbAndCache();
        } else if(choiceOperation == CITY_FIND_ALL.ordinal()) {
            getAllCities();
        } else if(choiceOperation == CITY_FIND_BY_ID.ordinal()) {
            getCityById();
        } else if(choiceOperation == CITY_FIND_BY_NAME.ordinal()) {
            getCityByName();
        } else if(choiceOperation == CITY_SAVE.ordinal()) {
            saveCity();
        } else if(choiceOperation == CITY_UPDATE.ordinal()) {
            updateCity();
        } else if(choiceOperation == CITY_DELETE_BY_ID.ordinal()) {
            deleteCityById();
        }
    }

    @Override
    public void shutdown() {
        cityService.shutdown();
        LOGGER.info("shutdown");
    }

    private List<CityCountry> getAllCities() {
        System.out.printf("Perform: get all cities\n");
        LOGGER.info("Perform: get all cities");
        long startMysql = System.currentTimeMillis();
        try {
            List<CityCountry> list = cityService.getAll();
            long stopMysql = System.currentTimeMillis();
            params[0] = String.format("%sall cities were get for %d ms. Size of list: %d", REPORT_HEADER, (stopMysql - startMysql), list.size());
            LOGGER.info(params[0]);
            return list;
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%scan't get all cities from DB", REPORT_HEADER);
        }
        return null;
    }

    private void getCityById() {
        System.out.printf("Perform: get city by id: %d\n", Integer.valueOf(params[1]));
        LOGGER.info(String.format("Perform: get city by id: %d", Integer.valueOf(params[1])));
        try {
            CityCountry cityCountry = cityService.getById(Integer.valueOf(params[1]));
            params[0] = String.format("%s%s", REPORT_HEADER, cityCountry);
            LOGGER.info(params[0]);
        } catch (CityNotFoundException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void getCityByName() {
        System.out.printf("Perform: get city by name: %s\n", params[1]);
        LOGGER.info(String.format("Perform: get city by name: %s", params[1]));
        try {
            StringBuilder sb = new StringBuilder();
            cityService.getByName(params[1]).forEach(city -> {
                sb.append(city);
                sb.append(System.lineSeparator());
            });
            params[0] = String.format("%s%s", REPORT_HEADER, sb);
            LOGGER.info(params[0]);
        } catch (CityNotFoundException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void saveCity() {
        System.out.printf("Perform: save new city: %s\n", params[1]);
        LOGGER.info(String.format("Perform: save new city: %s\n", params[1]));
        City city = new City();
        city.setName(params[1]);
        city.setPopulation(Integer.valueOf(params[2]));
        Country country = countryService.getCountryById(Integer.valueOf(params[3]));
        city.setCountry(country);
        city.setDistrict(String.format("District"));
        System.out.printf("Perform: save the city. Name: %s, population: %d, country id: %d, country name: %s\n", city.getName(), city.getPopulation(), country.getId(), country.getName());
        LOGGER.info(String.format("Perform: save the city. Name: %s, population: %d, country id: %d, country name: %s", city.getName(), city.getPopulation(), country.getId(), country.getName()));
        try {
            cityService.save(city);
            params[0] = String.format("%sthe city was saved. Name: %s, population: %d, country id: %d, country name: %s", REPORT_HEADER, city.getName(), city.getPopulation(), country.getId(), country.getName());
            LOGGER.info(params[0]);
        }  catch (PersistenceException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void updateCity() {
        System.out.printf("Perform: update the city. id: %s", params[1]);
        LOGGER.info(String.format("Perform: update the city. id: %s", params[1]));
        City city;
        try {
            city = cityService.getCityById(Integer.valueOf(params[1]));
        }  catch (CityNotFoundException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
            return;
        }
        city.setName(city.getName() + "****99");
        city.setPopulation(20000);
        System.out.printf("Perform: update the city. id: %d, name: %s, population: %d\n", city.getId(), city.getName(), city.getPopulation());
        LOGGER.info(String.format("Perform: update the city. id: %d, name: %s, population: %d", city.getId(), city.getName(), city.getPopulation()));
        try {
            cityService.update(city);
            params[0] = String.format("%sthe city was updated. id: %d, name: %s, population: %d", REPORT_HEADER, city.getId(), city.getName(), city.getPopulation());
            LOGGER.info(params[0]);
        }  catch (PersistenceException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void deleteCityById() {
        System.out.printf("Perform: delete the city, id: %d\n", Integer.valueOf(params[1]));
        LOGGER.info(String.format("Perform: delete the city, id: %d", Integer.valueOf(params[1])));
        try {
            cityService.deleteById(Integer.valueOf(params[1]));
            params[0] = String.format("%sthe city with id %d was deleted!", REPORT_HEADER, Integer.valueOf(params[1]));
            LOGGER.info(params[0]);
        }  catch (CityNotFoundException e) {
            LOGGER.info(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void testAllCitiesFromDbAndCache() {
        System.out.printf("Perform: %s\n", GET_ALL_CITIES_FROM_DB_AND_CACHE.getTitle());
        LOGGER.info(String.format("Perform: %s", GET_ALL_CITIES_FROM_DB_AND_CACHE.getTitle()));

        List<Integer> ids = new ArrayList<>();
        long startMysql = System.currentTimeMillis();
        List<CityCountry> listAll = getAllCities();
        long stopMysql = System.currentTimeMillis();
        listAll.forEach(cityCountry -> ids.add(cityCountry.getId()));
        cityService.clearCountersOfCache();

        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);

        //at this moment we've to get cities from cache
        long startRedis = System.currentTimeMillis();
        runListOfCitiesToCityService(ids);
        long stopRedis = System.currentTimeMillis();

        String redis = String.format("%s:\t%d ms", "Redis", (stopRedis - startRedis));
        String mysql = String.format("%s:\t%d ms", "MySQL", (stopMysql - startMysql));
        params[0] = String.format("%s%s: %s; %s", REPORT_HEADER, GET_ALL_CITIES_FROM_DB_AND_CACHE.getTitle(),  mysql, redis);
        LOGGER.info(String.format("%s%s: %s; %s", REPORT_HEADER, GET_ALL_CITIES_FROM_DB_AND_CACHE.getTitle(),  mysql, redis));
    }

    private void testSomeCitiesFromDbAndCache() {
        System.out.printf("Perform: %s\n", GET_SOME_CITIES_FROM_DB_AND_CACHE.getTitle());
        LOGGER.info(String.format("Perform: %s", GET_SOME_CITIES_FROM_DB_AND_CACHE.getTitle()));
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        System.out.println("List id's of cities for testing:");
        for (Integer id : ids) {
            System.out.printf("%d ", id);
        }
        System.out.println();
        testListOfCities(ids);
    }

    private void testListOfCities(List<Integer> ids) {
        cityService.clearCountersOfCache();

        long startMysql = System.currentTimeMillis();
        testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);
        runListOfCitiesToCityService(ids);

        //at this moment we've to get cities from cache
        long startRedis = System.currentTimeMillis();
        runListOfCitiesToCityService(ids);
        long stopRedis = System.currentTimeMillis();

        String redis = String.format("%s:\t%d ms", "Redis", (stopRedis - startRedis));
        String mysql = String.format("%s:\t%d ms", "MySQL", (stopMysql - startMysql));
        params[0] = String.format("%s%s: %s; %s", REPORT_HEADER, GET_SOME_CITIES_FROM_DB_AND_CACHE.getTitle(),  mysql, redis);
        LOGGER.info(String.format("%s%s: %s; %s", REPORT_HEADER, GET_SOME_CITIES_FROM_DB_AND_CACHE.getTitle(),  mysql, redis));
    }

    private void testMysqlData(List<Integer> ids) {
        for (Integer id : ids) {
            City city = cityService.getCityById(id);
            Set<CountryLanguage> languages = city.getCountry().getLanguages();
        }
    }

    private void runListOfCitiesToCityService(List<Integer> ids) {
        for (Integer id : ids) {
            cityService.getById(id);
        }
    }
}