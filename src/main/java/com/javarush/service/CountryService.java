package com.javarush.service;

import com.javarush.dao.CityDAO;
import com.javarush.dao.CountryDAO;
import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.exception.CityNotFoundException;
import com.javarush.exception.CountryNotFoundException;

public class CountryService {
    private final CountryDAO countryDAO = CountryDAO.getInstance();

    public Country getCountryById(Integer id) {
        return countryDAO.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(String.format("Country with id %d not found", id)));

    }
}