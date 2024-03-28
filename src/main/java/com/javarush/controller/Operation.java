package com.javarush.controller;

public enum Operation {
    CITY_FIND_ALL("Find all cities"),
    CITY_FIND_BY_ID ("Find city by id"),
    CITY_FIND_BY_NAME ("Find city by name"),
    CITY_SAVE  ("Save city"),
    CITY_UPDATE ("Update city"),
    CITY_DELETE_BY_ID ("Delete city by id"),
    RUN_LIST_OF_OPERATIONS ("Run list of operations"),
    GET_SOME_CITIES_FROM_DB_AND_CACHE ("Test extraction of several cities from the database and cache"),
    GET_ALL_CITIES_FROM_DB_AND_CACHE ("Test extraction of ALL cities from the database and cache"),
    EXIT ("Exit the application");

    private String title;

    Operation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
