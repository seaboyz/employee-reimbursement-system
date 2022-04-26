package com.revature.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    // get path
    String path = req.getPathInfo();

    // get token from req
    String token = Util.getToken(req);

    // login
    // @/users
    // login does not need to be authenticated(no token)
    if (path == null && token == null) {
      String auth = req.getHeader("Authorization");
      String base64Credentials = auth.substring("Basic".length()).trim();
      byte[] credentialDecoded = Base64.getDecoder().decode(base64Credentials);
      String credentials = new String(credentialDecoded, StandardCharsets.UTF_8);
      String[] credentialsArray = credentials.split(":");
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

    // both admin and user can access this with token
    // verify token
    if (token == null) {
      res.setStatus(401);
      return;
    }
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // as admin
    boolean isAdmin = authService.isAdmin(token);
    ;
    if (isAdmin && path == null) {
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
    if (isAdmin && path != null) {
      // get any user by id as admin including self
      try {
        int id = Integer.parseInt(path.substring(1));
        res.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(userService.getUserById(id)));
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

    // as user
    // @users/:id
    // get userId
    int userId = Integer.parseInt(path.substring(1));

    if (!authService.isSelf(userId, token)) {
      // user can only get their own user info
      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    } else {
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

  // DELETE @users/{id}
  // delete user
  // only admin can delete user
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    setAccessControlHeaders(res);

    // get path
    String path = req.getPathInfo();
    if (path == null) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

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
    int userId = Integer.parseInt(path.substring(1));

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
