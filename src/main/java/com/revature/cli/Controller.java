package com.revature.cli;

import java.sql.SQLException;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class Controller {

  public User loggedInUser = null;

  private AuthService authService;
  private UserService userService;

  public Controller(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public void update(String username, String password, String email) {
    username = username.equals("") ? loggedInUser.getUsername() : username;
    password = password.equals("") ? loggedInUser.getPassword() : password;
    email = email.equals("") ? loggedInUser.getEmail() : email;

    User userToBeUpdated = new User(
        loggedInUser.getId(),
        username,
        password,
        email,
        loggedInUser.getFirstname(),
        loggedInUser.getLastname(),
        loggedInUser.getRole());

    // TODO
    // userService.update(userToBeUpdated);
  }

  public User register(
      String email,
      String password,
      String firstname,
      String lastname) throws SQLException {
    User newUser = new User(email, password, firstname, lastname);

    return authService.register(newUser);
  }

  public void loginWithUsernameAndPassword(String username, String password) {
    // todo for REST API
    // HttpClient POST http://localhost:8080/login
    // https://hc.apache.org/httpcomponents-client-5.1.x/index.html

    // CloseableHttpClient client = HttpClients.createDefault();
    // HttpPost httpPost = new HttpPost("http://localhost:8080/api/user");
    // List<NameValuePair> params = new ArrayList<>();
    // params.add((new BasicNameValuePair("username", username)));
    // params.add((new BasicNameValuePair("password", password)));
    // httpPost.setEntity(new UrlEncodedFormEntity(params));
    // CloseableHttpResponse response = client.execute(httpPost);

    // if (response.getStatusLine().getStatusCode() == 200) {
    // System.out.println("_______________________________");
    // System.out.println("| Welcome |");
    // System.out.println("| User Menu |");
    // System.out.println("|_____________________________|");
    // } else if (response.getStatusLine().getStatusCode() == 403) {
    // System.out.println("The username and password was wrong, please try again.");
    // System.out.println("Press 0 to go back to the main manu");
    // }

    try {
      User user = authService.login(username, password);
      loggedInUser = user;
    } catch (UserNotExistException e) {
      System.out.println("User with the username " + username + " does not exist, please try again.");
    } catch (UserNamePasswordNotMatchException e) {
      System.out.println("The password was wrong, please try again.");
    } catch (SQLException e) {
      System.out.println("Something wrong with database");
      e.printStackTrace();
    }
  }

  public void logout() {
    loggedInUser = null;
  }

}
