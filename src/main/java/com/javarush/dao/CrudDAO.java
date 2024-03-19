package com.javarush.dao;

import com.javarush.domain.City;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, I> {
    List<T> findAll();
    Optional<City> findById(I id);

    void deleteById(I id);
}
