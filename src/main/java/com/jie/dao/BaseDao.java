package com.jie.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 操作数据库的公共类
public class BaseDao {

    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;

    // 静态代码块，类加载的时候就初始化
    static {
        // 通过类加载器读取资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    // 获取数据库的连接
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // 编写查询公告类
    public static ResultSet execute(Connection connection, String sql, Object[] params, ResultSet resultSet,
                                    PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    // 编写查询公告类
    public static int execute(Connection connection, String sql, Object[] params,
                                    PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }

        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    // 释放资源
    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement,
                                        ResultSet resultSet) {
        boolean flag=true;

        if (resultSet!=null) {
            try {
                resultSet.close();

                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if (connection!=null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if (preparedStatement!=null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return false;
    }

}
