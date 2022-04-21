package com.revature.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.UnavailableException;

public class PostgreSQLDatabase {
  private static Connection conn;

  private PostgreSQLDatabase() {
  };

  public static Connection getConnection() throws SQLException, UnavailableException {

    if (conn == null) {
      connect();
    }
    return conn;
  }

  public static void disconnect() throws SQLException {
    conn.close();
  }

  private static void connect() throws SQLException, UnavailableException {
    // preload dbDriver
    String dbDriver = "org.postgresql.Driver";
    try {
      Class.forName(dbDriver).newInstance();
    } catch (ClassNotFoundException e) {
      throw new UnavailableException(
          "UserServlet.init(  ) ClassNotFoundException: " +
              e.getMessage());
    } catch (IllegalAccessException e) {
      throw new UnavailableException(
          "UserServlet.init(  ) IllegalAccessException: " +
              e.getMessage());
    } catch (InstantiationException e) {
      throw new UnavailableException(
          "UserServlet.init(  ) InstantiationException: " +
              e.getMessage());
    }
    // get username password postgreSql string from application.properties file
    Properties props = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream input = loader.getResourceAsStream("application.properties");

    try {
      props.load(input);
    } catch (IOException e) {
      System.out.println("Fail loading props from application.properites file");
      e.printStackTrace();
      throw new UnavailableException(e.getMessage());
    }

    // Build connection string
    String url = "jdbc:postgresql://"
        + props.getProperty("hostname")
        + "/"
        + props.getProperty("dbname");
    String username = props.getProperty("username");
    String password = props.getProperty("password");

    // establish connection
    conn = DriverManager.getConnection(url, username, password);
    System.out.println("Connected to PostgreSQL server.");
  }
}
