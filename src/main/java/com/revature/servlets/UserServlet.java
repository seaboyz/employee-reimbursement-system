package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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
public class UserServlet extends HttpServlet {
  private Gson gson;
  private AuthService authService;

  public UserServlet(AuthService authService) {
    this.authService = authService;
    gson = new Gson();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("username");
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
      }
    } catch (UserNamePasswordNotMatchException e) {
      response.setStatus(401);
      out.print("Username and Password not match");

    } catch (UserNotExistException e) {
      response.setStatus(404);
      out.print("Username not found, please go to registration");

    } catch (SQLException e) {
      e.printStackTrace();
      response.setStatus(500);
      out.print("Something wrong with database");
    } finally {
      out.flush();
    }

  }
}
