package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.revature.exceptions.UserNotExistException;

import org.junit.jupiter.api.Test;

public class AuthServiceTest {
  @Test
  void loginShouldThrowUserNotExitExceptionWhenUserNotExist() throws Exception {
    UserService mockUserService = mock(UserService.class);
    AuthService authService = new AuthService(mockUserService);

    String NOT_EXIST_USER_NAME = "notExistUserName";
    when(mockUserService.getByUsername(NOT_EXIST_USER_NAME)).thenReturn(Optional.empty());

    assertThrowsExactly(
        UserNotExistException.class,
        () -> authService.login(NOT_EXIST_USER_NAME, anyString()));
  }
}
