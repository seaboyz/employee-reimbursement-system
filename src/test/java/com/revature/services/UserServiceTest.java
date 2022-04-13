package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAOImplementation;

import org.junit.Test;
import org.mockito.Mockito;;

public class UserServiceTest {

  @Test
  public void testGetByUsernamePassesWhenUsernameExists() throws Exception {

    UserDAOImplementation mockUserDao = Mockito.mock(UserDAOImplementation.class);
    UserService userService = new UserService(mockUserDao);

    User testUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockUserDao.getByUsername("test")).thenReturn(Optional.of(testUser));

    assertEquals(userService.getByUsername("test"), Optional.of(testUser));

  }

  @Test
  public void testGetByUsernamePassesWhenUsernameNotExists() throws Exception {

    UserDAOImplementation mockUserDao = Mockito.mock(UserDAOImplementation.class);
    UserService userService = new UserService(mockUserDao);

    String NOT_EXIST_USER_NAME = "notExistUserName";

    when(mockUserDao.getByUsername(NOT_EXIST_USER_NAME)).thenReturn(Optional.empty());

    assertEquals(userService.getByUsername(NOT_EXIST_USER_NAME), Optional.empty());
  }

  @Test
  public void testGetByUsernamePassesWhenUsernameShouldReturnTheRightUser() throws Exception {
    UserDAOImplementation mockUserDao = Mockito.mock(UserDAOImplementation.class);
    UserService userService = new UserService(mockUserDao);

    User testUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockUserDao.getByUsername("test")).thenReturn(Optional.of(testUser));

    assertEquals(testUser.getUsername(), userService.getByUsername("test").get().getUsername());
  }

}
