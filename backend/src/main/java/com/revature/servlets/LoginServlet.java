package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.revature.database.PostgreSQLDatabase;
import com.revature.models.User;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;

// POST @route /api/login
// base authentication through Auth Basic
// credentials are encoded with Basic64 in the header
public class LoginServlet extends HttpServlet {
  private Gson gson;
  private AuthService authService;
  private UserService userService;
  private UserDao userDao;
  private Connection connection;

  @Override
  public void init() {
    try {
      connection = PostgreSQLDatabase.getConnection();
      userDao = new UserDao(connection);
      userService = new UserService(userDao);
      this.authService = new AuthService(userService);
      gson = new Gson();
    } catch (SQLException e) {
      System.out.println("Database connection error");
      e.printStackTrace();
    } catch (UnavailableException e) {
      System.out.println("Server error");
      e.printStackTrace();
    }
  }

  private void setAccessControlHeaders(HttpServletResponse res) {
    res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
    // res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Headers", "Authorization");
    res.setHeader("Access-Control-Allow-Methods", "POST");
  }

  // for Prefligh
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  // POST @users/
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // recieving data from header
    String auth = req.getHeader("Authorization");
    String base64Credentials = auth.substring("Basic".length()).trim();
    byte[] credentialDecoded = Base64.getDecoder().decode(base64Credentials);
    String credentials = new String(credentialDecoded, StandardCharsets.UTF_8);
    String username = credentials.split(":")[0];
    String password = credentials.split(":")[1];

    // setup response
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // user userService login return User object
    try {
      User loggedInUser = authService.login(username, password);
      // convert User object to json
      String userJson = gson.toJson(loggedInUser);
      out.print(userJson);
    } catch (SQLException e) {
      res.setStatus(500);
      String errorJson = gson.toJson(e);
      out.print(errorJson);
    }

    out.flush();
  }

  @Override
  public void destroy() {
    try {
      PostgreSQLDatabase.disconnect();
      if (connection.isClosed()) {
        System.out.println("Databse connection closed");
      } else {
        System.out.println("database disconnect failed");
      }
    } catch (SQLException e) {
      System.out.println("database disconnect failed");
      e.printStackTrace();
    }
  }

}
