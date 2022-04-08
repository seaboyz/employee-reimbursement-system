package com.revature.controller;

import com.revature.repositories.UserRepository;
import com.revature.services.AuthService;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class UserController extends HttpServlet {
  private UserService userService;
  private AuthService authService;

  public UserController(UserRepository userRepository) {
    this.userService = new UserService(userRepository);
    this.authService = new AuthService(userService);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String username = request.getParameter("email").split("@")[0];
    String password = request.getParameter("password");
    // TODO
    // use AuthService to do user authentication
    // if user exist return user info to frontend 200
    // if not throw exception?? or return 404
    AuthService authService = new AuthService();

    authService.login(username, password);
  }
}
