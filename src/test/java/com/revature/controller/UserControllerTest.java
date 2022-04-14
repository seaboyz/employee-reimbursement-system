package com.revature.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  String NOT_EXIST_USERNAME = "NOT_EXIST_USERNAME";
  String NOT_EXIST_PASSWORD = "NOT_EXIST_PASSWORD";
  String EXIST_USERNAME = "EXIST_USERNAME";
  String WRONG_PASSWORD = "WRONG_PASSWORD";

  @Mock
  AuthService mockAuthService;

  @Mock
  HttpServletRequest mockRequest;

  @Mock
  HttpServletResponse mockResponse;

  @Mock
  PrintWriter mockOut;

  @Test
  void doPostShouldSent404ResponseWhenReqeustUserNotExist() throws IOException {

    when(mockRequest.getParameter("username")).thenReturn(NOT_EXIST_USERNAME);
    when(mockRequest.getParameter("password")).thenReturn(NOT_EXIST_PASSWORD);

    when(mockResponse.getWriter()).thenReturn(mockOut);

    when(mockAuthService.login(NOT_EXIST_USERNAME, NOT_EXIST_PASSWORD)).thenThrow(new UserNotExistException());

    UserController userController = new UserController(mockAuthService);
    userController.doPost(mockRequest, mockResponse);

    verify(mockResponse).setStatus(404);

    verify(mockOut).print("Username not found, please go to registration");

    verify(mockOut).flush();
  }

  @Test
  void doPostShouldSent401ResponseWhenRequestUserPasswordNotMatch() throws IOException {

    when(mockRequest.getParameter("username")).thenReturn(EXIST_USERNAME);
    when(mockRequest.getParameter("password")).thenReturn(WRONG_PASSWORD);

    when(mockResponse.getWriter()).thenReturn(mockOut);

    when(mockAuthService.login(EXIST_USERNAME, WRONG_PASSWORD)).thenThrow(new UserNamePasswordNotMatchException());

    UserController userController = new UserController(mockAuthService);
    userController.doPost(mockRequest, mockResponse);

    verify(mockResponse).setStatus(401);

    verify(mockOut).print("Username and Password not match");

    verify(mockOut).flush();
  }

  @Test
  void doPostShouldSend200RestponseWhenRequestUserPasswordNotMatch() throws IOException {

    User user = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockRequest.getParameter("username")).thenReturn(user.getUsername());
    when(mockRequest.getParameter("password")).thenReturn(user.getPassword());

    when(mockResponse.getWriter()).thenReturn(mockOut);

    when(mockAuthService.login(user.getUsername(), user.getPassword())).thenReturn(user);

    UserController userController = new UserController(mockAuthService);
    userController.doPost(mockRequest, mockResponse);

    verify(mockResponse).setStatus(200);

    verify(mockResponse).setContentType("application/json");

    verify(mockResponse).setCharacterEncoding("UTF-8");

    verify(mockOut).print(anyString());

    verify(mockOut).flush();
  }

  @Nested
  @DisplayName("Test for Contoller login")
  class Controller {
    UserController userController;

    @BeforeEach
    void init() {
      userController = new UserController(mockAuthService);
    }

    @Test
    void loginShouldThrowUserNotExitException() {
      when(mockAuthService.login(NOT_EXIST_USERNAME, NOT_EXIST_PASSWORD)).thenThrow(UserNotExistException.class);

      assertThrows(
          UserNotExistException.class,
          () -> userController.login(NOT_EXIST_USERNAME, NOT_EXIST_PASSWORD));
    }

    @Test
    void loginShouldUserNamePasswordNotMatchException() {
      when(mockAuthService.login(EXIST_USERNAME, WRONG_PASSWORD)).thenThrow(UserNamePasswordNotMatchException.class);

      assertThrows(
          UserNamePasswordNotMatchException.class,
          () -> userController.login(EXIST_USERNAME, WRONG_PASSWORD));
    }

    @Test
    void loginShouldReturnUser() {
      User user = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

      when(mockAuthService.login(user.getUsername(), user.getPassword())).thenReturn(user);

      assertEquals(user, userController.login(user.getUsername(), user.getPassword()));
    }
  }
}
