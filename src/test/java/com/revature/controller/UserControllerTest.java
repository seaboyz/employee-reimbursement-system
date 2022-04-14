package com.revature.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.exceptions.UserNotExistException;
import com.revature.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  UserController userController;
  AuthService authService;
  String NOT_EXIST_EMAIL = "NOT_EXIST@EMAIL.COM";
  String NOT_EXIST_PASSWORD = "NOT_EXIST_PASSWORD";

  @Mock
  AuthService mockAuthService;

  @Mock
  HttpServletRequest mockRequest;

  @Mock
  HttpServletResponse mockResponse;

  @BeforeEach
  void init() {

    userController = new UserController(mockAuthService);

  }

  @Test
  void doPostShouldThrowUserNotExistExceptionWhenReqeustUserNotExist() throws IOException {
    when(mockRequest.getParameter("email")).thenReturn(NOT_EXIST_EMAIL);
    when(mockRequest.getParameter("password")).thenReturn(NOT_EXIST_PASSWORD);

    StringWriter sw = new StringWriter();
    PrintWriter out = new PrintWriter(sw);
    when(mockResponse.getWriter()).thenReturn(out);

    when(mockAuthService.login(NOT_EXIST_EMAIL, NOT_EXIST_PASSWORD)).thenThrow(UserNotExistException.class);

    assertThrows(
        UserNotExistException.class,
        () -> userController.doPost(mockRequest, mockResponse));
    // https://www.javadoc.io/static/org.mockito/mockito-core/2.6.1/org/mockito/exceptions/misusing/PotentialStubbingProblem.html
  }
}
