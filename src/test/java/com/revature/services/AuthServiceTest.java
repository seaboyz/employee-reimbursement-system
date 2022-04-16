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
  User mockUser;

  @Mock
  UserService mockUserService;

  @BeforeEach
  public void init() {

    authService = new AuthService(mockUserService);

    mockUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);
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

      when(mockUserService.getByUsername("test")).thenReturn(Optional.of(mockUser));

      assertThrows(
          UserNamePasswordNotMatchException.class,
          () -> authService.login("test", "wrongPassword"));
    }

    @Test
    void loginShouldReturnUserWhenPassingRightUserNameAndPassword() throws Exception {

      when(mockUserService.getByUsername("test")).thenReturn(Optional.of(mockUser));

      assertEquals(authService.login("test", "123456"), mockUser);

    }
  }

  @Nested
  @DisplayName("Test register()")
  class TestRegister {

    @Test
    void shouldThrowUsernameNotUniqueException() throws SQLException {
      when(mockUserService.getByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

      assertThrows(
          UsernameNotUniqueException.class,
          () -> authService.register(mockUser));
    }

    // @Test
    // void shouldReturnUserWithUpdatedId() throws SQLException {
    // User newUser = new User()
    // updatedUser.setId(999);
    // when(mockUserService.addUser(mockUser)).thenReturn(updatedUser);
    // int id = mockUser.getId();
    // int updatedId = updatedUser.getId();
    // assertEquals(updatedId, 999);

    // }
  }
}
