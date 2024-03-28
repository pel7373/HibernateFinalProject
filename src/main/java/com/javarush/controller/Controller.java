package com.javarush.controller;

public interface Controller {
    void handler(int choiceOperation);

    void shutdown();
}
