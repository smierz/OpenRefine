
package com.google.refine.extension.database.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MySQLDatabaseIT {

    private static final String dockerImage = "mysql:8.0";

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(dockerImage)
            .withExposedPorts(3306)
            .withDatabaseName("test_db")
            .withUsername("mysql_root")
            .withPassword("secret");

    @BeforeClass
    public static void startContainer() {
        mySQLContainer.start();
    }

    @AfterClass
    public static void stopContainer() {
        mySQLContainer.stop();
    }

    @Test
    public void test() {
        System.out.println(mySQLContainer.getDatabaseName());
        System.out.println(mySQLContainer.getUsername());
        System.out.println(mySQLContainer.getPassword());
        System.out.println(mySQLContainer.getJdbcUrl());
        System.out.println(mySQLContainer.getTestQueryString());
    }
}
