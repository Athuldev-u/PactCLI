package com.week2gate2.database.test;

import com.week2gate2.database.config.DatabaseConfig;
import com.week2gate2.database.support.DbSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitialDatabaseTest
{
    private static DbSupport database;
    @BeforeAll
    static void setup()
    {
        database=new DbSupport(DatabaseConfig.fromEnvironment());

    }
    @Test
    @DisplayName("Local M1:MySQL is reachable through JDBC")
    void localMySqlIsRechargable() throws Exception
    {
        assertTrue(database.isReachable());
    }
}
