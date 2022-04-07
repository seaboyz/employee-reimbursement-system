package com.revature.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/user")
public class UserController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("Email: " + request.getParameter("email"));
    System.out.println("Password: " + request.getParameter("password"));
    // TODO
    // use AuthService to do user authentication
    // if user exist return user info to frontend 200
    // if not throw exception?? or return 404
  }
}
