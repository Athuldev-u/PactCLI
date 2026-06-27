package com.week2gate2.api.config;

public class BaseConfig
{
    public static final String BASE_URL_CONFIG=System.getProperty(
            "baseUrl",
            System.getenv().getOrDefault("BASE_URL","http://localhost:4000")
    );
}
