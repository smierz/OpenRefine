
package com.google.refine.extension.database.mysql;

import java.sql.Connection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.refine.extension.database.DBExtensionTestUtils;
import com.google.refine.extension.database.DatabaseService;
import com.google.refine.extension.database.DatabaseServiceException;
import com.google.refine.extension.database.model.DatabaseColumn;
import com.google.refine.extension.database.model.DatabaseInfo;
import com.google.refine.extension.database.model.DatabaseRow;

public class MySQLDatabaseServiceIT extends MySQLDatabaseIT {

    @Test
    public void testGetDatabaseUrl() {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService.get(MySQLDatabaseService.DB_NAME);
        String dbUrl = pgSqlService.getDatabaseUrl(getTestDbConfig());
        // System.out.println("dbUrl:" + dbUrl);
        Assert.assertNotNull(dbUrl);
        Assert.assertEquals(dbUrl, DBExtensionTestUtils.getJDBCUrl(getTestDbConfig()));
    }

    @Test
    public void testGetConnection() throws DatabaseServiceException {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService.get(MySQLDatabaseService.DB_NAME);
        Connection conn = pgSqlService.getConnection(getTestDbConfig());

        Assert.assertNotNull(conn);
    }

    @Test
    public void testTestConnection() throws DatabaseServiceException {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService.get(MySQLDatabaseService.DB_NAME);

        boolean result = pgSqlService.testConnection(getTestDbConfig());
        Assert.assertTrue(result);
    }

    @Test
    public void testConnect() throws DatabaseServiceException {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService.get(MySQLDatabaseService.DB_NAME);
        DatabaseInfo databaseInfo = pgSqlService.connect(getTestDbConfig());
        Assert.assertNotNull(databaseInfo);
    }

    @Test
    public void testExecuteQuery() throws DatabaseServiceException {

        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService
                .get(MySQLDatabaseService.DB_NAME);
        DatabaseInfo databaseInfo = pgSqlService.testQuery(getTestDbConfig(), "SELECT * FROM " + getTestTableName());

        Assert.assertNotNull(databaseInfo);
    }

    @Test
    public void testBuildLimitQuery() {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService.get(MySQLDatabaseService.DB_NAME);
        String limitQuery = pgSqlService.buildLimitQuery(100, 0, "SELECT * FROM " + getTestTableName());
        Assert.assertNotNull(limitQuery);
        Assert.assertEquals(limitQuery,
                "SELECT * FROM (SELECT * FROM " + getTestTableName() + ") data LIMIT " + 100 + " OFFSET " + 0 + ";");
    }

    @Test
    public void testGetRows() throws DatabaseServiceException {
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService
                .get(MySQLDatabaseService.DB_NAME);
        List<DatabaseRow> dbRows = pgSqlService.getRows(getTestDbConfig(), "SELECT * FROM " + getTestTableName());

        Assert.assertNotNull(dbRows);
    }

    @Test
    public void testGetInstance() {
        MySQLDatabaseService instance = MySQLDatabaseService.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetColumns() throws DatabaseServiceException {
        List<DatabaseColumn> dbColumns;
        MySQLDatabaseService pgSqlService = (MySQLDatabaseService) DatabaseService
                .get(MySQLDatabaseService.DB_NAME);

        dbColumns = pgSqlService.getColumns(getTestDbConfig(), "SELECT * FROM " + getTestTableName());

        Assert.assertNotNull(dbColumns);
    }
}
