package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.Dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/* 
  @ExtendWith is junit.jupiter
  make sure @Test is also junit.jupiter
*/
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  UserService userService;

  @Mock
  Dao mockUserDao;

  @BeforeEach
  public void init() {
    userService = new UserService(mockUserDao);
  }

  @Test
  public void testGetByUsernamePassesWhenUsernameExists() throws Exception {

    User testUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockUserDao.get("test")).thenReturn(Optional.of(testUser));

    assertEquals(userService.getByUsername("test"), Optional.of(testUser));

  }

  @Test
  public void testGetByUsernamePassesWhenUsernameNotExists() throws Exception {

    String NOT_EXIST_USER_NAME = "notExistUserName";

    when(mockUserDao.get(NOT_EXIST_USER_NAME)).thenReturn(Optional.empty());

    assertEquals(userService.getByUsername(NOT_EXIST_USER_NAME), Optional.empty());
  }

  @Test
  public void testGetByUsernamePassesWhenUsernameShouldReturnTheRightUser() throws Exception {

    User testUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

    when(mockUserDao.get("test")).thenReturn(Optional.of(testUser));

    assertEquals(testUser.getUsername(), userService.getByUsername("test").get().getUsername());
  }

}
