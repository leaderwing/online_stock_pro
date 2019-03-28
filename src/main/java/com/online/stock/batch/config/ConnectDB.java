package com.online.stock.batch.config;

import oracle.jdbc.driver.OracleDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDB {
    private static ConnectDB instance;
    public Connection connection = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;

    private ConnectDB() throws SQLException {
        System.out.print("start connect db!");
        try {
            getDBInfo();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectDB getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectDB();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConnectDB();
        }

        return instance;
    }
    private void getDBInfo () {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
            // load a properties file
            prop.load(input);
            url = prop.getProperty("hibernate.connection.url");
            username = prop.getProperty("hibernate.connection.username");
            password = prop.getProperty("hibernate.connection.password");
            // get the property value and print it out
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
