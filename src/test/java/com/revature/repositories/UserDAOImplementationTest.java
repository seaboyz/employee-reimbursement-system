package com.revature.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.revature.models.Role;
import com.revature.models.User;

import org.junit.Test;

public class UserDAOImplementationTest {

  @Test
  public void testGetByUsernameShouldReturnUser() throws Exception {
    UserDAOImplementation userDao = new UserDAOImplementation();
    User user = userDao.getByUsername("test");
    assertNotNull(user);
    assertEquals(user.getClass(), User.class);
  }

  @Test
  public void testGetByUsernameShouldReturnTheRightUser() throws Exception {
    UserDAOImplementation userDao = new UserDAOImplementation();
    User user = userDao.getByUsername("test");
    assertEquals(user.getUsername(), "test");
    assertEquals(user.getEmail(), "test@test.com");
    assertEquals(user.getPassword(), "123456");
    assertEquals(user.getFirstname(), "john");
    assertEquals(user.getLastname(), "doe");
    assertEquals(user.getRole(), Role.EMPLOYEE);

  }
}
