package com.revature.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.revature.database.PostgreSQLDatabase;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.util.Util;

public class ReimbursementServlet extends HttpServlet {
  private Gson gson;
  private AuthService authService;
  private UserService userService;
  private UserDao userDao;
  private Connection connection;
  private ReimbursementService reimbursementService;

  @Override
  public void init() {
    try {
      connection = PostgreSQLDatabase.getConnection();
      userDao = new UserDao(connection);
      userService = new UserService(userDao);
      this.authService = new AuthService(userService);
      gson = new Gson();
      reimbursementService = new ReimbursementService(connection);
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
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res) {
    setAccessControlHeaders(res);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  // GET @/reimbursements/:id
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    setAccessControlHeaders(res);

    // Get the token from the request
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // validate token
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // get user from token
    User user;
    try {
      user = authService.getUserFromToken(token);
      if (user == null) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // get reimbursement id
    String[] path = req.getPathInfo().split("/");
    int reimbursementId = Integer.parseInt(path[1]);

    // get reimbursement
    Reimbursement reimbursement;
    try {
      reimbursement = reimbursementService.get(reimbursementId);
    } catch (SQLException e) {
      e.printStackTrace();
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(gson.toJson(reimbursement));

  }

  // POST @/reimbursements
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    setAccessControlHeaders(res);

    // Get the token from the request
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // validate token
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // get user from token
    User user;
    try {
      user = authService.getUserFromToken(token);
      if (user == null) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    String body = req.getReader().lines().reduce("", (acc, line) -> acc + line);

    Reimbursement reimbursement = gson.fromJson(body, Reimbursement.class);
    reimbursement.setAuthorId(user.getId());

    try {
      Reimbursement savedReimbursement = reimbursementService.add(reimbursement);
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write(gson.toJson(savedReimbursement));
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }
  }

  // PUT @/reimbursements/:id
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    setAccessControlHeaders(res);

    // Get the token from the request
    String token = Util.getToken(req);
    if (token == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // validate token
    if (!authService.isTokenValid(token)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // get user from token
    User user;
    try {
      user = authService.getUserFromToken(token);
      if (user == null) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // get reimbursement id
    String[] path = req.getPathInfo().split("/");
    int reimbursementId = Integer.parseInt(path[1]);

    // get reimbursement
    Reimbursement reimbursement;
    try {
      reimbursement = reimbursementService.get(reimbursementId);
    } catch (SQLException e) {
      e.printStackTrace();
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // check if reimbursement belongs to user
    if (reimbursement.getAuthorId() != user.getId()) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String body = req.getReader().lines().reduce("", (acc, line) -> acc + line);

    Reimbursement reimbursementTobeUpdated = gson.fromJson(body, Reimbursement.class);
    reimbursementTobeUpdated.setId(reimbursementId);

    try {
      Reimbursement updatedReimbursement = reimbursementService.update(reimbursementTobeUpdated);
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write(gson.toJson(updatedReimbursement));
    } catch (SQLException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }
  }


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
