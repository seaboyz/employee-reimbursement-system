package com.revature.cli;

import java.sql.SQLException;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class Controller {

  private AuthService authService;
  private UserService userService;

  public Controller(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public User update(User userTobeUpdated) throws SQLException {
    return userService.updateUser(userTobeUpdated);

  }

  public User register(
      String email,
      String password,
      String firstname,
      String lastname) throws SQLException {
    User newUser = new User(email, password, firstname, lastname);

    return authService.register(newUser);
  }

  public User login(String username, String password)
      throws UserNamePasswordNotMatchException, UserNotExistException, SQLException {
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

    return authService.login(username, password);
  }

  public User removeUser(User userTobeDeleted) throws SQLException {
    return userService.removeUser(userTobeDeleted);
  }

}
