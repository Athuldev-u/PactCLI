package com.week2gate2.api.config;

public class TestEnvConfig {

    public static String configenv(String name, String fallback)
    {
        String system=System.getProperty(name);
        if(system !=null && !system.isBlank())
        {
            return system;
        }
        String envValue=System.getenv(name);
        return envValue==null||envValue.isBlank()?fallback:envValue;
    }
}
