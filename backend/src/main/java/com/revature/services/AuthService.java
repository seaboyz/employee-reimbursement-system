package com.revature.services;

import java.sql.SQLException;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.util.Util;

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
  public User authenticate(String username, String password)
      throws UserNotExistException, UserNamePasswordNotMatchException, SQLException {

    Optional<User> optionalUser = userService.getByUsername(username);

    if (!optionalUser.isPresent()) {
      throw new UserNotExistException();
    }

    User currentUser = optionalUser.get();

    String encriptedPassword = currentUser.getPassword();

    if (BCrypt.checkpw(password, encriptedPassword)) {
      String token = Util.getToken(currentUser);
      currentUser.setToken(token);
      return currentUser;
    } else {
      throw new UserNamePasswordNotMatchException();
    }

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
  public void register(User userToBeRegistered) throws SQLException, UsernameNotUniqueException {

    Optional<User> optionalUser = userService.getByUsername(userToBeRegistered.getUsername());

    if (optionalUser.isPresent()) {
      throw new UsernameNotUniqueException();
    }

    // encrypt password
    String encryptedPassword = Util.encriptPassword(userToBeRegistered.getPassword());

    // update user with encriptedpassword
    userToBeRegistered.setPassword(encryptedPassword);

    // save user
    userService.addUser(userToBeRegistered);
  }

  private DecodedJWT getVerifier(String token) {
    String secret = Util.getSecret();
    if (secret == null) {
      return null;
    }
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
      DecodedJWT jwt = verifier.verify(token);
      return jwt;
    } catch (JWTVerificationException e) {
      return null;
    }
  }

  public User getUserFromToken(String token) throws SQLException {
    DecodedJWT jwt = getVerifier(token);
    if (jwt == null) {
      return null;
    }
    String username = jwt.getClaim("username").asString();
    Optional<User> optionalUser = userService.getByUsername(username);
    if (!optionalUser.isPresent()) {
      return null;
    }
    User user = optionalUser.get();
    return new User(user.getId(), user.getUsername(), token);
  }

  public boolean isTokenValid(String token) {

    try {
      String secret = Util.getSecret();
      if (secret == null) {
        return false;
      }
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
      DecodedJWT jwt = verifier.verify(token);
      if (jwt == null) {
        return false;
      }
      return true;

    } catch (JWTVerificationException e) {
      return false;
    }
  }

  public boolean isSelf(int userId, String token) {
    DecodedJWT verifier = getVerifier(token);
    if (verifier != null) {
      int verifiedId = verifier.getClaim("id").asInt();
      return verifiedId == userId;
    }
    return false;
  }

  public boolean isAdmin(String token) {
    DecodedJWT verifier = getVerifier(token);
    if (verifier != null) {
      return verifier.getClaim("isAdmin").asBoolean();
    }
    return false;
  }

}
