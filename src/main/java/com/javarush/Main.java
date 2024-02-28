package com.javarush;

import redis.clients.jedis.Jedis;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Jedis jedis = new Jedis("localhost", 6379);
        String john = jedis.get("username");
        System.out.println(john);

    }
}