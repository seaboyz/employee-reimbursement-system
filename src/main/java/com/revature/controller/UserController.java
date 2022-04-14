package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.User;
import com.revature.services.AuthService;

@WebServlet("/user")
public class UserController extends HttpServlet {
  private Gson gson;
  private AuthService authService;

  public UserController(AuthService authService) {
    this.authService = authService;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("email").split("@")[0];
    String password = request.getParameter("password");
    PrintWriter out = response.getWriter();

    // use AuthService to do user authentication
    try {
      User user = authService.login(username, password);
      if (user != null) {
        String userJsonString = gson.toJson(user);

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(userJsonString);
        out.flush();
      }
    } catch (UserNamePasswordNotMatchException e) {
      response.setStatus(401);
      out.print("Username and Password not match");
      out.flush();
    } catch (UserNotExistException e) {
      response.setStatus(404);
      out.print("Username not found, please go to registration");
      out.flush();
    }

  }
}
