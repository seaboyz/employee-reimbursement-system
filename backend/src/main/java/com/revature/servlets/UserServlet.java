package com.revature.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revature.database.PostgreSQLDatabase;
import com.revature.models.User;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.util.Util;

public class UserServlet extends HttpServlet {
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
    res.setHeader("Access-Control-Allow-Headers", "Authorization");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
  }

  // * Preflight
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  // * GET @users/{id}
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // setup response
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // get token from req
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(401);
      return;
    }
    // get userId
    String[] path = req.getPathInfo().split("/");
    int userId = Integer.parseInt(path[1]);

    if (!authService.isTokenValid(token)) {
      res.setStatus(401);
      out.println("<h2>Please login</h2>");
    } else if (!authService.isSelf(userId, token)) {
      res.setStatus(401);
      out.println("<h2>No permission allowed</h2>");
    } else {
      try {
        Optional<User> optionalUser = userService.getByUserId(userId);
        if (!optionalUser.isPresent()) {
          res.setStatus(500);
          out.println("database error");
        } else {
          res.setStatus(200);
          User user = optionalUser.get();
          String jsonString = gson.toJson(user);
          out.println(jsonString);
        }
      } catch (SQLException e) {
        e.printStackTrace();
        res.setStatus(500);
      }

    }

    out.flush();

  }

  // * PUT @users/{id}
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // setup response
    res.setContentType("text/html");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // get token from req
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(401);
      return;
    }

    // get userId
    String[] path = req.getPathInfo().split("/");
    int userId = Integer.parseInt(path[1]);

    // verify token
    if (!authService.isTokenValid(token)) {
      res.setStatus(401);
      out.println("<h2>Please login</h2>");
      return;
    }

    // verify identity
    if (!authService.isSelf(userId, token)) {
      res.setStatus(401);
      out.println("<h2>No permission allowed</h2>");
      return;
    }

    // read json from request
    InputStream requestStream = req.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(requestStream);
    JsonObject jsonObject = JsonParser.parseReader(inputStreamReader).getAsJsonObject();

    // create new User object
    String username = jsonObject.get("username").getAsString();
    String email = jsonObject.get("email").getAsString();
    String password = jsonObject.get("password").getAsString();
    String encriptedPassword = Util.encriptPassword(password);

    User userToBeUpdated = new User();
    userToBeUpdated.setId(userId);
    userToBeUpdated.setEmail(email);
    userToBeUpdated.setPassword(encriptedPassword);
    userToBeUpdated.setUsername(username);

    try {
      userService.updateUser(userToBeUpdated);
      out.println("<h2>Successfully Updated you profile</h2>");
    } catch (SQLException e) {
      res.setStatus(500);
      out.println("<h2>Fail of updating your profile, try again later.</h2>");
    }

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
