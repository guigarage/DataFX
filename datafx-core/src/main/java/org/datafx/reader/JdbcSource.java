package org.datafx.reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.datafx.reader.converter.JdbcConverter;
import org.datafx.reader.converter.JdbcDataSourceUtil;

/**
 *
 * @author johan
 */
public class JdbcSource<T> extends AbstractDataReader<T> {

    private final String jdbcUrl;
    private final String selectStatement;
    private final JdbcConverter<T> converter;
    private boolean connectionCreated;
    private boolean lastResult;
    private ResultSet resultSet;

    public JdbcSource(String jdbcUrl, JdbcConverter<T> converter, String table, String... cols) {
        this(jdbcUrl, JdbcDataSourceUtil.createSelectStatement(table, cols), converter);
    }

    public JdbcSource(String jdbcUrl, String selectStatement,
            JdbcConverter<T> converter) {
        this.jdbcUrl = jdbcUrl;
        this.selectStatement = selectStatement;
        this.converter = converter;
    }


    private synchronized void createConnection() {
        if (connectionCreated) {
            return;
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            Statement query = connection.createStatement();
            resultSet = query.executeQuery(selectStatement);
            lastResult = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionCreated = true;
    }

    @Override public T get() {
        if (!connectionCreated) {
            createConnection();
        }
        
        return converter.get();
    }

    @Override public boolean next() {
        return converter.next();
    }
    
}
