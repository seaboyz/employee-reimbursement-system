package com.revature.cli;

import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
public class ControllerTest {
  User loggedInUser;
  Controller controller;
  Mapper mapper;

  @Mock
  UserModel mockUserModel;

  @BeforeEach
  void init() {
    controller = new Controller(mockUserModel, mapper);
  }

  @Test
  void updateShouldUpdateUser() throws SQLException {
    User testUser = new User(1, "test", "123456", "test@test.com", "john", "doe",
        Role.EMPLOYEE);

    User userTobeUpdated = new User();
    userTobeUpdated.setId(testUser.getId());
    userTobeUpdated.setUsername(testUser.getUsername());
    userTobeUpdated.setPassword(testUser.getPassword());
    userTobeUpdated.setEmail(testUser.getEmail());

    when(mockUserModel.update(userTobeUpdated)).thenReturn(Util.shallowCloneUser(testUser));

    assertNotSame(
        controller.updateUser(
            testUser.getId(),
            testUser.getUsername(),
            testUser.getPassword(),
            testUser.getEmail()),
        testUser);

    assertEquals(
        controller.updateUser(
            testUser.getId(),
            testUser.getUsername(),
            testUser.getPassword(),
            testUser.getEmail()),
        testUser);

  }

  @Test
  void registerShouldReturnAUserWithIdAndRole() throws SQLException {
    User testUser = new User("test@test.com", "123456", "john", "doe");

    User registeredUser = Util.shallowCloneUser(testUser);
    registeredUser.setId(1);
    registeredUser.setRole(Role.EMPLOYEE);

    when(mockUserModel.add(testUser)).thenReturn(registeredUser);

    assertEquals(
        controller.register(
            testUser.getEmail(),
            testUser.getPassword(),
            testUser.getFirstname(),
            testUser.getLastname()),
        registeredUser);
  }

  @Nested
  class TestLogin {

    User testUser;

    @BeforeEach
    void init() {
      testUser = new User(1, "test", "123456", "test@test.com", "john", "doe",
          Role.EMPLOYEE);
    }

    @Test
    void loginShouldReturnAUserRegisterdUser() throws SQLException {

      when(mockUserModel.auth(testUser.getUsername(), testUser.getPassword()))
          .thenReturn(testUser);

      assertEquals(
          testUser,
          controller.login(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    void shouldThrowUserNotExitException() throws SQLException {

      when(mockUserModel.auth(anyString(),
          anyString())).thenThrow(UserNotExistException.class);

      assertThrows(
          UserNotExistException.class,
          () -> controller.login(anyString(), anyString()));
    }

    @Test
    void shouldThrowUserNamePasswordNotMatchException() throws SQLException {

      when(mockUserModel.auth(testUser.getUsername(), testUser.getPassword()))
          .thenThrow(UserNamePasswordNotMatchException.class);

      assertThrows(UserNamePasswordNotMatchException.class,
          () -> controller.login(testUser.getUsername(), testUser.getPassword()));
    }
  }

  @Nested
  class TestRemoveUser {
    User currentEmployee;
    User notCurrentEmployee;

    @BeforeEach
    void init() {
      currentEmployee = new User(1, "test", "123456", "test@test.com", "john",
          "doe", Role.EMPLOYEE);

      notCurrentEmployee = Util.shallowCloneUser(currentEmployee);
      notCurrentEmployee.setRole(Role.NOT_CURRENT_EMPLOYEE);
    }

    @Test
    void ShouldReturnAUserWithRoleNOT_CURRENT_EMPLOYEE() throws SQLException {

      when(mockUserModel.remove(currentEmployee)).thenReturn(notCurrentEmployee);

      assertEquals(Role.NOT_CURRENT_EMPLOYEE,
          controller.removeUser(currentEmployee).getRole());
    }

  }

}