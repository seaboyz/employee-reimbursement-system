package com.revature.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.revature.models.User;

public class Controller {
  UserModel userModel;

  public Employee updateUser(int userId, String username, String password, String email) {

    return null;
  }

  public Employee register(
      String email,
      String password,
      String firstname,
      String lastname) {

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

    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }

  public Employee removeUser(User userTobeDeleted) {

    return null;
  }

}
