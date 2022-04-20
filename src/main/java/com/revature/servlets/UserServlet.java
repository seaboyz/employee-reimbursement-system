package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.util.Util;

//// @WebServlet(urlPatterns = "/users")
public class UserServlet extends HttpServlet {
  // private Gson gson;
  // private AuthService authService;

  // @Override
  // public void init(ServletConfig config) throws ServletException {
  // this.authService = new authService();
  // gson = new Gson();
  // }

  // for testing GET @route /api/users
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h2>Hello from the UserServlet</h2>");

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    out.println("<h2>" + username + "</h2>");
    out.println("<h2>" + password + "</h2>");
    out.flush();
  }

  // Login with username and password
  // @route POST /api/users/login
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String body = Util.getBody(request);

    PrintWriter out = response.getWriter();

    out.print(body);
  }

  // @Override
  // protected void doPost(HttpServletRequest request, HttpServletResponse
  // response) throws IOException {
  // String username = request.getParameter("username");
  // String password = request.getParameter("password");
  // PrintWriter out = response.getWriter();

  // // use AuthService to do user authentication
  // try {
  // User user = authService.login(username, password);
  // if (user != null) {
  // String userJsonString = gson.toJson(user);
  // response.setStatus(200);
  // response.setContentType("application/json");
  // response.setCharacterEncoding("UTF-8");
  // out.print(userJsonString);
  // }
  // } catch (UserNamePasswordNotMatchException e) {
  // response.setStatus(401);
  // out.print("Username and Password not match");

  // } catch (UserNotExistException e) {
  // response.setStatus(404);
  // out.print("Username not found, please go to registration");

  // } catch (SQLException e) {
  // e.printStackTrace();
  // response.setStatus(500);
  // out.print("Something wrong with database");
  // } finally {
  // out.flush();
  // }

}
