package com.revature.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.Role;
import com.revature.models.User;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Util {
  public static User shallowCloneUser(User user) {
    return new User(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getFirstname(),
        user.getLastname(),
        user.getRole());
  }

  public static String getParamsFromPost(HttpServletRequest request) throws IOException {
    BufferedReader reader = request.getReader();
    StringBuilder sb = new StringBuilder();
    String line = reader.readLine();
    while (line != null) {
      sb.append(line + "\n");
      line = reader.readLine();
    }
    reader.close();
    String params = sb.toString();
    String[] _params = params.split("&");
    for (String param : _params) {
      System.out.println("params(POST)-->" + param);
    }
    return params;
  }

  public static String[] getCredentails(HttpServletRequest req) {
    String auth = req.getHeader("Authorization");
    String base64Credentials = auth.substring("Basic".length()).trim();
    byte[] credentialDecoded = Base64.getDecoder().decode(base64Credentials);
    String credentials = new String(credentialDecoded, StandardCharsets.UTF_8);
    String[] credentialsArray = credentials.split(":");
    return credentialsArray;
  }

  public static JsonObject getJson(HttpServletRequest req) throws IOException {
    InputStream requestStream = req.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(requestStream);
    return JsonParser.parseReader(inputStreamReader).getAsJsonObject();
  }

  public static String getBody(HttpServletRequest request) throws IOException {
    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      InputStream inputStream = request.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      } else {
        stringBuilder.append("");
      }
    } catch (IOException e) {
      throw e;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          throw e;
        }
      }
    }
    body = stringBuilder.toString();
    return body;
  }

  public static int getUserId(HttpServletRequest req) {
    return req.getPathInfo() == null ? -1 : Integer.parseInt(req.getPathInfo().substring(1));
  }

  public static String getToken(HttpServletRequest req) {
    try {
      String auth = req.getHeader("Authorization");
      if (auth.split(" ")[0].equals("Bearer")) {
        return auth.split(" ")[1];
      }
      return null;
    } catch (Exception e) {
      return null;
    }

  }

  public static String getToken(User user) throws JWTCreationException {
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
    Algorithm algorithm = Algorithm.HMAC256(SECRECT);
    Builder builder = JWT.create().withIssuer("auth0").withClaim("username", user.getUsername()).withClaim("id",
        user.getId());

    if (user.getRole().equals(Role.FINANCE_MANAGER)) {
      builder.withClaim("isAdmin", true);
    } else if (user.getRole().equals(Role.EMPLOYEE)) {
      builder.withClaim("isAdmin", false);
    } else if (user.getRole().equals(Role.NOT_CURRENT_EMPLOYEE)) {
      throw new UserNotExistException();
    }

    return builder.sign(algorithm);
  }

  public static String getSecret() {
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

    return SECRECT;
  }

  public static String encriptPassword(String password) {
    String salt = BCrypt.gensalt(10);
    String encryptedPassword = BCrypt.hashpw(password, salt);
    return encryptedPassword;
  }
}
