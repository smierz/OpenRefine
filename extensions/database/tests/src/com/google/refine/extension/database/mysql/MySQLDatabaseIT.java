
package com.google.refine.extension.database.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.google.refine.extension.database.DatabaseConfiguration;

public class MySQLDatabaseIT {

    // container setup
    private static final String dockerImage = "mysql:8.0";

    private static final int DB_PORT = 3306;

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(dockerImage)
            .withExposedPorts(DB_PORT)
            .withDatabaseName("test_db")
            .withUsername("mysql_root")
            .withPassword("secret")
            .withInitScript("mysql/init.sql");

    // application setup
    private static DatabaseConfiguration testDbConfig;

    @BeforeClass
    public static void startContainer() {
        mySQLContainer.start();

        // set application parameters for db connection from container setup
        testDbConfig = new DatabaseConfiguration();
        testDbConfig.setDatabaseHost(mySQLContainer.getHost());
        testDbConfig.setDatabasePort(mySQLContainer.getMappedPort(DB_PORT));
        testDbConfig.setDatabaseType(MySQLDatabaseService.DB_NAME);
        testDbConfig.setDatabaseName(mySQLContainer.getDatabaseName());
        testDbConfig.setDatabasePassword(mySQLContainer.getPassword());
        testDbConfig.setDatabaseUser(mySQLContainer.getUsername());
        testDbConfig.setUseSSL(false);
    }

    @AfterClass
    public static void stopContainer() {
        mySQLContainer.stop();
    }

    static DatabaseConfiguration getTestDbConfig() {
        return testDbConfig;
    }
}
