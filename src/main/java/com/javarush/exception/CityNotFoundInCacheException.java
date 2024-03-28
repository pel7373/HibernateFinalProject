package com.javarush.exception;

public class CityNotFoundInCacheException extends RuntimeException {
    public CityNotFoundInCacheException(String message) {
        super(message);
    }
}
