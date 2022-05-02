package com.revature.servlets;

import java.io.IOException;
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
import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
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
    res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
  }

  // preflight request
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  // GET @users
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    setAccessControlHeaders(res);

    // setup response
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();

    // get token from req
    String token = Util.getToken(req);

    // get userId from path
    int userId = Util.getUserId(req);

    // login
    // /users
    // token is empty
    // TODO use RequestDispatcher to redirect to login servlet
    if (userId == -1 && token == null) {
      String[] credentialsArray = Util.getCredentails(req);
      String username = credentialsArray[0];
      String password = credentialsArray[1];
      try {
        User user = authService.authenticate(username, password);
        res.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(user));
        return;
      } catch (SQLException e) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      } catch (UserNotExistException e) {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      } catch (UserNamePasswordNotMatchException e) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    // verify token
    if (token == null) {
      res.setStatus(401);
      return;
    }
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // TODO use RequestDispatcher to redirect to user servlet
    // /users
    // as admin
    boolean isAdmin = authService.isAdmin(token);
    ;
    if (isAdmin && userId == -1) {
      // get all users as admin
      try {
        res.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(userService.getAllUsers()));
        return;
      } catch (SQLException e) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
    }

    // TODO use RequestDispatcher to redirect to admin servlet
    // users/:id
    if (isAdmin && userId != -1) {
      // get any user by id as admin including self
      try {
        res.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(userService.getUserById(userId)));
        return;
      } catch (NumberFormatException e) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      } catch (SQLException e) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      } catch (UserNotExistException e) {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    }

    // TODO use RequestDispatcher to redirect to user servlet
    // as user
    // @users/:id
    // user can only get their own user info
    if (!authService.isSelf(userId, token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    try {
      User user = userService.getByUserId(userId);
      res.setStatus(HttpServletResponse.SC_OK);
      out.print(gson.toJson(user));
      return;
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    } catch (UserNotExistException e) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

  }

  // POST @users
  // register
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    setAccessControlHeaders(res);

    // read body from request
    JsonObject body = JsonParser.parseReader(new InputStreamReader(req.getInputStream())).getAsJsonObject();

    // create new User object
    String username = body.get("username").getAsString();
    String email = body.get("email").getAsString();
    String password = body.get("password").getAsString();
    String firstname = body.get("firstname").getAsString();
    String lastname = body.get("lastname").getAsString();

    User newUser = new User(username, email, password, firstname, lastname);

    try {
      authService.register(newUser);
      res.setStatus(HttpServletResponse.SC_CREATED);
      return;
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    } catch (UsernameNotUniqueException e) {
      res.setStatus(HttpServletResponse.SC_CONFLICT);
      return;
    }

  }

  // PUT @users/{id}
  // update user info
  // only user can update their own info
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // setup response
    res.setContentType("text/html");
    res.setCharacterEncoding("UTF-8");

    // get token from req
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // verify token
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // get userId
    int userId = Util.getUserId(req);

    // verify identity
    if (!authService.isSelf(userId, token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // read json from request
    JsonObject body = Util.getJson(req);

    // create new User object
    String username = body.get("username").getAsString();
    String email = body.get("email").getAsString();
    String password = body.get("password").getAsString();
    String encriptedPassword = Util.encriptPassword(password);

    User userToBeUpdated = new User();
    userToBeUpdated.setId(userId);
    userToBeUpdated.setEmail(email);
    userToBeUpdated.setPassword(encriptedPassword);
    userToBeUpdated.setUsername(username);

    try {
      userService.updateUser(userToBeUpdated);
      res.setStatus(HttpServletResponse.SC_OK);
      return;
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

  }

  // DELETE @users/{id}
  // delete user
  // only admin can delete user
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // get token from req
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // verify token
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // verify identity
    // is admin
    if (!authService.isAdmin(token)) {
      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    // get userId
    int userId = Util.getUserId(req);

    // only user can be deleted by admin
    // admin can only delete user that is not self
    if (authService.isSelf(userId, token)) {
      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    try {
      userService.deleteUserById(userId);
      res.setStatus(HttpServletResponse.SC_OK);
      return;
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    } catch (UserNotExistException e) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
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
