package com.revature.servlets;

import java.io.IOException;
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
import com.revature.database.PostgreSQLDatabase;
import com.revature.models.User;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;

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
    res.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
    res.setHeader("Access-Control-Allow-Headers", "Authorization");
    res.setHeader("Access-Control-Allow-Methods", "GET");
  }

  // for Prefligh
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  // GET @users/{id}
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // setup response
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // recieving data from header
    String token = req.getHeader("Authorization").split(" ")[1];

    // ger pathInfo
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
