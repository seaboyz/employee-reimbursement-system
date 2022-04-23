package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Optional;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
  AuthService authService;
  User testUser;

  @Mock
  UserService mockUserService;

  @BeforeEach
  public void init() {

    authService = new AuthService(mockUserService);

    testUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);
  }

  @Nested
  @DisplayName("Test login()")
  class TestLogin {

    @Test
    void loginShouldThrowUserNotExitExceptionWhenUserNotExist() throws Exception {
      String NOT_EXIST_USER_NAME = "notExistUserName";
      when(mockUserService.getByUsername(NOT_EXIST_USER_NAME)).thenReturn(Optional.empty());

      assertThrows(
          UserNotExistException.class,
          () -> authService.login(NOT_EXIST_USER_NAME, anyString()));
    }

    @Test
    void loginShouldThrowUserNamePassroedNotMatchExceptionWhenPassTheWrongPassword() throws Exception {

      when(mockUserService.getByUsername("test")).thenReturn(Optional.of(testUser));

      assertThrows(
          UserNamePasswordNotMatchException.class,
          () -> authService.login("test", "wrongPassword"));
    }

    @Test
    void loginShouldReturnUserWhenPassingRightUserNameAndPassword() throws Exception {

      when(mockUserService.getByUsername("test")).thenReturn(Optional.of(testUser));

      assertEquals(authService.login("test", "123456"), testUser);

    }
  }

  @Nested
  @DisplayName("Test register()")
  class TestRegister {

    @Test
    void shouldThrowUsernameNotUniqueException() throws SQLException {
      when(mockUserService.getByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

      assertThrows(
          UsernameNotUniqueException.class,
          () -> authService.register(testUser));
    }

  }
}
