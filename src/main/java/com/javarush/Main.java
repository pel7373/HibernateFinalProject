package com.javarush;

import com.javarush.domain.*;
import com.javarush.redis.CityCountry;
import com.javarush.redis.Language;
import com.javarush.service.CityService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        CityService cityService = new CityService();
        City city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        city = cityService.getById(3);
        //city = cityService.getById(3);
        //city = cityService.getById(3);
        //city = cityService.getById(3);
        //System.out.println(city);
        //System.out.println(city.getCountry());
        //System.out.println(city.getCountry().getLanguages());

        /*
        Main main = new Main();
        List<City> allCities = null;// =  main.fetchData(main);
        List<CityCountry> preparedData = main.transformData(allCities);
        main.pushToRedis(preparedData);

        //закриємо поточну сесію, щоб точно зробити запит до БД, а не витянути дані з кеша
        main.sessionFactory.getCurrentSession().close();

        //обираємо 10 випадкових id міст
        //оскільки ми не робили обробку невалідних ситуацій, використовуй id, які існують БД
        List<Long> ids = List.of(3L, 2545L, 123L, 4L, 189L, 89L, 3458L, 1189L, 10L, 102L);

        long startRedis = System.currentTimeMillis();
        main.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        main.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        main.shutdown();

         */
    }

/*
    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void testRedisData(List<Long> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Long id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
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
        }).collect(Collectors.toList());
    }

    private void testMysqlData(List<Long> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Long id : ids) {
                City city = null;
                //City city = cityDAO.findById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

 */
}