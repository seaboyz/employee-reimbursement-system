package com.revature.cli;

import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Optional;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.util.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserModelTest {
  UserModel userModel;
  User employee;
  User newUser;

  @Mock
  AuthService mockAuthService;

  @Mock
  UserService mockUserService;

  @BeforeEach
  void init() {
    userModel = new UserModel(mockAuthService, mockUserService);
    newUser = new User("test@test.com", "123456", "john", "doe");
    employee = new User(
        1,
        "test",
        "123456",
        "test@test.com",
        "john",
        "doe",
        Role.EMPLOYEE);
  }

  @Nested
  class TestAdd {

    @Test
    void shouldReturnEmployee() throws SQLException, UsernameNotUniqueException {

      when(mockAuthService.register(newUser)).thenReturn(employee);

      assertEquals(employee, userModel.add(newUser));
    }

    @Test()
    void shouldCatchUsernameNotUniqueException() throws SQLException {

      when(mockAuthService.register(newUser)).thenThrow(UsernameNotUniqueException.class);

      try {
        userModel.add(newUser);
      } catch (UsernameNotUniqueException e) {
        assertEquals("User name is already been taken, try another one", e.getMessage());
      }

      try {
        userModel.add(newUser);
      } catch (Exception e) {
        assertEquals(UsernameNotUniqueException.class, e.getClass());
      }

    }

    @Test()
    void shouldReturnNull() throws SQLException {
      when(mockAuthService.register(newUser)).thenThrow(UsernameNotUniqueException.class);

      assertNull(userModel.add(newUser));
    }
  }

  @Nested
  class TestAuth {

    @Test
    void shouldReturnUserWithIdAndRole() throws SQLException {
      when(mockAuthService.login(employee.getUsername(), employee.getPassword())).thenReturn(employee);

      assertEquals(employee, userModel.auth(employee.getUsername(), employee.getPassword()));
    }

    @Test
    void shouldCatchUserNamePasswordNotMatchException() throws SQLException {
      when(mockAuthService.login(newUser.getUsername(), newUser.getPassword()))
          .thenThrow(UserNamePasswordNotMatchException.class);

      try {
        userModel.auth(newUser.getUsername(), newUser.getPassword());
      } catch (Exception e) {
        assertEquals(UserNamePasswordNotMatchException.class, e.getClass());
      }
    }
  }

  @Nested
  class testGetUserByUsername {

    @Test
    void shouldReturnEmployee() throws SQLException {
      when(mockUserService.getByUsername(employee.getUsername())).thenReturn(Optional.of(employee));

      assertEquals(userModel.getUserByUsername(employee.getUsername()), employee);
    }

    @Test
    void shouldReturnNullWhenUserWithTheUsernameNotExist() throws SQLException {

      when(mockUserService.getByUsername(newUser.getUsername())).thenReturn(Optional.empty());

      assertNull(userModel.getUserByUsername(newUser.getUsername()));
    }
  }

  @Test
  void testGetAllUsers() {

  }

  @Nested
  class TestUpdate {

    @Test
    void shouldReturnUpdatedUser() throws SQLException {

      User updatedUser = Util.shallowCloneUser(employee);

      when(mockUserService.updateUser(employee)).thenReturn(updatedUser);

      assertEquals(employee, userModel.update(employee));
      assertNotSame(employee, userModel.update(employee));

    }
  }

  @Nested
  class TestRemove {

    @Test
    void shouldReturnRemovedUser() throws SQLException {
      when(mockUserService.removeUser(employee)).thenReturn(employee);
      assertEquals(userModel.remove(employee), employee);
    }

    @Test
    void shouldReturnNullWhenUserExist() throws SQLException {
      when(mockUserService.removeUser(newUser)).thenReturn(null);

      assertNull(userModel.remove(newUser));
    }
  }

}
