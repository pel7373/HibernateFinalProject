package com.javarush.controller;

import com.javarush.domain.City;
import com.javarush.exception.CityNotFoundException;
import com.javarush.service.CityService;

public class MyController implements Controller {
    private String[] params;
    CityService cityService = new CityService();

    public MyController(String[] params) {
        this.params = params;
    }

    @Override
    public void handler(int choiceOperation) {
        if(choiceOperation == Operation.CITY_FIND_ALL.ordinal()) {
            cityService.getAll().forEach(System.out::println);
        } else if(choiceOperation == Operation.CITY_FIND_BY_ID.ordinal()) {
            System.out.println("Perform: get city by id: " + Integer.valueOf(params[1]));
            try {
                System.out.println(cityService.getById(Integer.valueOf(params[1])));
            } catch (CityNotFoundException e) {
                System.out.println(e.getMessage());
            }

        } else if(choiceOperation == Operation.CITY_FIND_BY_NAME.ordinal()) {

        } else if(choiceOperation == Operation.CITY_SAVE.ordinal()) {

        } else if(choiceOperation == Operation.CITY_UPDATE.ordinal()) {

        } else if(choiceOperation == Operation.CITY_DELETE_BY_ID.ordinal()) {

        }

        /*CityService cityService = new CityService();
        City city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);*/

        /*if (cityCacheService.isGetFromCache(id)) {
            return cityCacheService.cacheGetById(id);
        }

        cityCacheService.cacheAddHandler(city);*/

        //cityCacheService.cacheDeleteById(id);
    }
}
