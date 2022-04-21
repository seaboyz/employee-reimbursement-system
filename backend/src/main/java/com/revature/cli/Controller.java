package com.revature.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revature.models.User;

public class Controller {

  public Employee updateUser(int userId, String username, String password, String email) {

    return null;
  }

  public Employee register(
      String email,
      String password,
      String firstname,
      String lastname) {

    try {
      // Making a JSON POST Request With HttpURLConnection
      // https://www.baeldung.com/httpurlconnection-post

      URL url = new URL("http://localhost:8080/api/register");

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("POST");

      connection.setRequestProperty("Accept", "application/json");

      // To send request content, let's enable the URLConnection object's doOutput
      // property to true.Otherwise, we won't be able to write content to the
      // connection output stream:
      connection.setDoOutput(true);

      // build a json object
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("email", email);
      jsonObject.addProperty("password", password);
      jsonObject.addProperty("firstname", firstname);
      jsonObject.addProperty("lastname", lastname);

      // convert jsonObject to jsonString
      String userJsonString = jsonObject.toString();

      // Create the Request Body
      OutputStream outputStream = connection.getOutputStream();
      byte[] input = userJsonString.getBytes(StandardCharsets.UTF_8);
      outputStream.write(input, 0, input.length);

      // get response
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

  public Employee login(String username, String password) {
    try {
      // Create a neat value object to hold the URL
      URL url = new URL("http://localhost:8080/api/login");

      // encode username and password
      String encoding = Base64.getEncoder().encodeToString(
          (username + ":" + password).getBytes(StandardCharsets.UTF_8));

      // Open a connection(?) on the URL(??) and cast the response(???)
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // set request type
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      // Now it's "open", we can set the request method, headers etc.
      // set auth header
      connection.setRequestProperty("Authorization", "Basic " + encoding);
      ;

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
