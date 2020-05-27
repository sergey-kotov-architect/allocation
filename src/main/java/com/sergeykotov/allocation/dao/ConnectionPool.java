package com.sergeykotov.allocation.dao;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setUrl("jdbc:sqlite:src/main/resources/allocation.sqlite");
        dataSource.setConnectionProperties("foreign_keys=true;");
        dataSource.setMinIdle(1);
        dataSource.setMaxIdle(5);
        dataSource.setMaxTotal(500);
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}