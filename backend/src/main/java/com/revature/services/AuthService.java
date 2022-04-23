package com.revature.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * The AuthService should handle login and registration for the ERS application.
 * <p>
 * {@code login} and {@code register} are the minimum methods required; however,
 * additional methods can be added.
 * <p>
 * Examples:
 * <ul>
 * <li>Retrieve Currently Logged-in User</li>
 * <li>Change Password</li>
 * <li>Logout</li>
 * </ul>
 */
public class AuthService {
  private UserService userService;

  public AuthService(UserService userService) {
    this.userService = userService;
  }

  /**
   * <ul>
   * <li>Needs to check for existing users with username/email provided.</li>
   * <li>Must throw exception if user does not exist.</li>
   * <li>Must compare password provided and stored password for that user.</li>
   * <li>Should throw exception if the passwords do not match.</li>
   * <li>Must return user object if the user logs in successfully.</li>
   * </ul>
   */
  public User login(String username, String password)
      throws UserNotExistException, UserNamePasswordNotMatchException, SQLException {

    Optional<User> optionalUser = userService.getByUsername(username);

    if (!optionalUser.isPresent()) {
      throw new UserNotExistException();
    }
    if (!optionalUser.get().getPassword().equals(password)) {
      throw new UserNamePasswordNotMatchException();
    }

    return optionalUser.get();

  }

  /**
   * <ul>
   * <li>Should ensure that the username/email provided is unique.</li>
   * <li>Must throw exception if the username/email is not unique.</li>
   * <li>Should persist the user object upon successful registration.</li>
   * <li>Must throw exception if registration is unsuccessful.</li>
   * <li>Must return user object if the user registers successfully.</li>
   * <li>Must throw exception if provided user has a non-zero ID</li>
   * </ul>
   * <p>
   * Note: userToBeRegistered will have an id=0, additional fields may be null.
   * After registration, the id will be a positive integer.
   */
  public User register(User userToBeRegistered) throws SQLException, UsernameNotUniqueException, IOException {

    Optional<User> optionalUser = userService.getByUsername(userToBeRegistered.getUsername());

    if (optionalUser.isPresent()) {
      throw new UsernameNotUniqueException();
    }

    // encrypt password
    String salt = BCrypt.gensalt(10);
    String encryptedPassword = BCrypt.hashpw(userToBeRegistered.getPassword(), salt);
    userToBeRegistered.setPassword(encryptedPassword);

    // save user
    User registedUser = userService.addUser(userToBeRegistered);

    String token = getToken();

    if (registedUser != null && token != null) {
      return new User(registedUser.getUsername(), token);
    }
    return null;
  }

  private String getToken() throws JWTCreationException {
    // get secrect string from application.properties file
    Properties props = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream input = loader.getResourceAsStream("application.properties");
    try {
      props.load(input);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.print("Fail loading props from application.properites file");
      return null;
    }

    // get
    String SECRECT = props.getProperty("ACCESS_TOKEN_SECRET");

    // get jwt token
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRECT);
      String token = JWT.create().withIssuer("auth0").sign(algorithm);
      return token;
    } catch (JWTCreationException e) {
      e.printStackTrace();
      System.out.println("Fail generating JWT token");
      return null;
    }
  }

}
