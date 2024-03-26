package com.javarush.controller;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.dto.CityCountry;
import com.javarush.exception.CityNotFoundException;
import com.javarush.service.CityService;
import com.javarush.service.CountryService;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class MyController implements Controller {
    private String[] params;
    private static final String REPORT_HEADER = "###Report for user: ";
    CityService cityService = new CityService();
    CountryService countryService = new CountryService();

    public MyController(String[] params) {
        this.params = params;
    }

    @Override
    public void handler(int choiceOperation) {
        if(choiceOperation == Operation.CITY_FIND_ALL.ordinal()) {
            getAllCities();
        } else if(choiceOperation == Operation.CITY_FIND_BY_ID.ordinal()) {
            getCityById();
        } else if(choiceOperation == Operation.CITY_FIND_BY_NAME.ordinal()) {
            getCityByName();
        } else if(choiceOperation == Operation.CITY_SAVE.ordinal()) {
            saveCity();
        } else if(choiceOperation == Operation.CITY_UPDATE.ordinal()) {
            updateCity();
        } else if(choiceOperation == Operation.CITY_DELETE_BY_ID.ordinal()) {
            deleteCityById();
        }
    }

    private void getAllCities() {
        long startMysql = System.currentTimeMillis();
        List<CityCountry> list = cityService.getAll();
        long stopMysql = System.currentTimeMillis();
        params[0] = String.format("%s all cities were get for %d ms. Size of list: %d", REPORT_HEADER, (stopMysql - startMysql), list.size());
    }

    private void getCityById() {
        System.out.printf("Perform: get city by id: %d\n", Integer.valueOf(params[1]));
        try {
            params[0] = String.format("%s%s", REPORT_HEADER, cityService.getById(Integer.valueOf(params[1])).toString());
        } catch (CityNotFoundException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void getCityByName() {
        System.out.printf("Perform: get city by name: %s\n", params[1]);
        try {
            StringBuilder sb = new StringBuilder();
            cityService.getByName(params[1]).forEach(city -> {
                sb.append(city);
                sb.append(System.lineSeparator());
            });
            params[0] = String.format("%s%s", REPORT_HEADER, sb);
        } catch (CityNotFoundException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void saveCity() {
        System.out.printf("Perform: save new city: %s\n", params[1]);
        City city = new City();
        city.setName(params[1]);
        city.setPopulation(Integer.valueOf(params[2]));
        Country country = countryService.getCountryById(Integer.valueOf(params[3]));
        city.setCountry(country);
        city.setDistrict(String.format("District"));
        System.out.printf("Perform: save the city. Name: %s, population: %d, country id: %d, country name: %s\n", city.getName(), city.getPopulation(), country.getId(), country.getName());
        try {
            cityService.save(city);
            params[0] = String.format("%sthe city was saved. Name: %s, population: %d, country id: %d, country name: %s", REPORT_HEADER, city.getName(), city.getPopulation(), country.getId(), country.getName());
        }  catch (PersistenceException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void updateCity() {
        System.out.printf("Perform: update the city. id: %s", params[1]);
        City city;
        try {
            city = cityService.getCityById(Integer.valueOf(params[1]));
        }  catch (CityNotFoundException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
            return;
        }
        city.setName(city.getName() + "****99");
        city.setPopulation(20000);
        System.out.printf("Perform: update the city. id: %d, name: %s, population: %d\n", city.getId(), city.getName(), city.getPopulation());
        try {
            cityService.update(city);
            params[0] = String.format("%sthe city was updated. id: %d, name: %s, population: %d", REPORT_HEADER, city.getId(), city.getName(), city.getPopulation());
        }  catch (PersistenceException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }

    private void deleteCityById() {
        System.out.printf("Perform: delete the city, id: %d\n", Integer.valueOf(params[1]));
        try {
            cityService.deleteById(Integer.valueOf(params[1]));
            params[0] = String.format("%sthe city with id %d was deleted!", REPORT_HEADER, Integer.valueOf(params[1]));
        }  catch (CityNotFoundException e) {
            System.out.println(e.getMessage());
            params[0] = String.format("%s%s", REPORT_HEADER, e.getMessage());
        }
    }
}