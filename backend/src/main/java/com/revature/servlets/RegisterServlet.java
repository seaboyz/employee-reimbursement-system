package com.revature.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

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

// POST @route /api/register
// base authentication through Auth Basic
// credentials are encoded with Basic64 in the header
public class RegisterServlet extends HttpServlet {
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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    // setup response
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // read json from request
    InputStream requestStream = req.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(requestStream);
    JsonObject jsonObject = JsonParser.parseReader(inputStreamReader).getAsJsonObject();

    // create new User object
    String email = jsonObject.get("email").getAsString();
    String password = jsonObject.get("password").getAsString();
    String firstname = jsonObject.get("firstname").getAsString();
    String lastname = jsonObject.get("lastname").getAsString();

    User newUser = new User(email, password, firstname, lastname);

    // use userService add user to the database
    try {
      User registerdUser = userService.addUser(newUser);
      // send user object back as json
      String userJson = gson.toJson(registerdUser);
      out.print(userJson);

    } catch (SQLException e) {
      res.setStatus(500);
      String errorJson = gson.toJson(e);
      out.print(errorJson);
    }
    // The flush() method of OutputStream class is used to flush the content of the
    // buffer to the output stream.
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
