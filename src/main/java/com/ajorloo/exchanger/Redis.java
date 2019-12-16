package com.ajorloo.exchanger;

import redis.clients.jedis.Jedis;

public class Redis {

    public static void set(String key, String value, Integer ttl) {

        Jedis jedis = new Jedis("redis");
        jedis.set(key, value);
        if (ttl != null) {
            jedis.expire(key, ttl);
        }
        jedis.close();
    }

    public static String get(String key) {
        Jedis jedis = new Jedis("redis");
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

}
