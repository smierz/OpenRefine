
package com.google.refine.extension.database.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.refine.extension.database.DatabaseServiceException;

public class MySQLConnectionManagerIT extends MySQLDatabaseIT {

    @Test
    public void testTestConnection() throws DatabaseServiceException {
        boolean conn = MySQLConnectionManager.getInstance().testConnection(getTestDbConfig());
        Assert.assertTrue(conn);
    }

    @Test
    public void testGetConnection() throws DatabaseServiceException {
        Connection conn = MySQLConnectionManager.getInstance().getConnection(getTestDbConfig(), true);
        Assert.assertNotNull(conn);
    }

    @Test
    public void testShutdown() throws DatabaseServiceException, SQLException {
        Connection conn = MySQLConnectionManager.getInstance().getConnection(getTestDbConfig(), true);
        Assert.assertNotNull(conn);

        MySQLConnectionManager.getInstance().shutdown();

        if (conn != null) {
            Assert.assertTrue(conn.isClosed());
        }
    }
}
