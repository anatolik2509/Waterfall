package ru.itis.antonov.waterfall.repositories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleJdbcTemplate {

    private DataSource dataSource;

    public SimpleJdbcTemplate(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object ... args) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            //System.out.println(sql);
            for(int i = 0; i < args.length; i++) {
                //System.out.println(args[i]);
                statement.setObject(i + 1, args[i]);
            }
            resultSet = statement.executeQuery();
            List<T> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }
    }

    public int update(String sql, Object ... args) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            //System.out.println(sql);
            for(int i = 0; i < args.length; i++) {
                //System.out.println(args[i]);
                statement.setObject(i + 1, args[i]);
            }
            result= statement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }
    }
}

