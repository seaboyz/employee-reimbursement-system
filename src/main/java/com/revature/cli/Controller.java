package com.revature.cli;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.revature.models.User;

public class Controller {
  UserModel userModel;
  Mapper mapper;

  public Controller(UserModel userModel, Mapper mapper) {
    this.userModel = userModel;
    this.mapper = mapper;
  }

  public Employee updateUser(int userId, String username, String password, String email) {

    User userTobeUpdated = new User();
    userTobeUpdated.setId(userId);
    userTobeUpdated.setUsername(username);
    userTobeUpdated.setPassword(password);
    userTobeUpdated.setEmail(email);

    User updatedUser = userModel.update(userTobeUpdated);

    if (updatedUser != null) {
      return mapper.userToEmployee(updatedUser);
    }
    return null;
  }

  public Employee register(
      String email,
      String password,
      String firstname,
      String lastname) {

    User newUser = new User(email, password, firstname, lastname);

    User registedUser = userModel.add(newUser);

    if (registedUser != null) {
      return mapper.userToEmployee(registedUser);
    }
    return null;
  }

  public Employee login(String username, String password) {
    //// User authenticatedUser = userModel.auth(username, password);
    //// if (authenticatedUser != null) {
    //// return mapper.userToEmployee(authenticatedUser);
    //// }
    // // return null;
    try {
      // Create a neat value object to hold the URL
      URL url = new URL("http://localhost:8080/api/users");

      // Open a connection(?) on the URL(??) and cast the response(???)
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // set request type
      connection.setRequestMethod("GET");

      // set request header
      connection.setRequestProperty("username", username);
      connection.setRequestProperty("password", password);

      //// String basicAuth = Base64.getEncoder()
      //// .encodeToString((username + ":" +
      //// password).getBytes(StandardCharsets.UTF_8));

      //// Now it's "open", we can set the request method, headers etc.
      //// connection.setRequestProperty("Authorization", "Basic " + basicAuth);

      // This line makes the request
      InputStream responseStream = connection.getInputStream();
      // convert json stream to Emplyee object
      InputStreamReader inputStreamReader = new InputStreamReader(responseStream);
      Employee employee = new Gson().fromJson(inputStreamReader, Employee.class);

      return employee;

    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }

    return null;

  }

  public Employee removeUser(User userTobeDeleted) {
    User removedUser = userModel.remove(userTobeDeleted);

    if (removedUser != null) {
      return mapper.userToEmployee(removedUser);
    }

    return null;
  }

}
