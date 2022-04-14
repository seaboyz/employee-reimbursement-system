package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.Role;
import com.revature.models.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  UserService mockUserService;

  @Test
  void loginShouldThrowUserNotExitExceptionWhenUserNotExist() throws Exception {

    AuthService authService = new AuthService(mockUserService);

    String NOT_EXIST_USER_NAME = "notExistUserName";
    when(mockUserService.getByUsername(NOT_EXIST_USER_NAME)).thenReturn(Optional.empty());

    assertThrowsExactly(
        UserNotExistException.class,
        () -> authService.login(NOT_EXIST_USER_NAME, anyString()));
  }

  @Test
  void loginShouldThrowUserNamePassroedNotMatchExceptionWhenPassTheWrongPassword() throws Exception {

    AuthService authService = new AuthService(mockUserService);

    User mockUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);
    when(mockUserService.getByUsername("test")).thenReturn(Optional.of(mockUser));

    assertThrowsExactly(
        UserNamePasswordNotMatchException.class,
        () -> authService.login("test", "wrongPassword"));
  }

  @Test
  void loginShouldReturnUserWhenPassingRightUserNameAndPassword() throws Exception {

    AuthService authService = new AuthService(mockUserService);

    User mockUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockUserService.getByUsername("test")).thenReturn(Optional.of(mockUser));

    assertEquals(authService.login("test", "123456"), mockUser);

  }

}
