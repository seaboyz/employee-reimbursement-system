package com.revature.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import com.revature.models.Role;
import com.revature.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDAOImplementationTest {
  UserDAOImplementation userDao;

  @BeforeEach
  public void init() {
    userDao = new UserDAOImplementation();
  }

  @Test
  public void testGetByUsernameShouldReturnOptional() throws Exception {

    Optional<User> optionalUser = userDao.getByUsername("test");

    assertEquals(optionalUser.getClass(), Optional.class);

    ;
  }

  @Test
  public void testGetByUsernameShouldReturnTheRightUser() throws Exception {
    Optional<User> optionalUser = userDao.getByUsername("test");
    User user = optionalUser.get();

    assertEquals(user.getUsername(), "test");
    assertEquals(user.getEmail(), "test@test.com");
    assertEquals(user.getPassword(), "123456");
    assertEquals(user.getFirstname(), "john");
    assertEquals(user.getLastname(), "doe");
    assertEquals(user.getRole(), Role.EMPLOYEE);
  }

  @Test
  public void testGetByUsernameshouldResturnOptionalEmpty() throws Exception {

    Optional<User> optionalNullUser = userDao.getByUsername("notExistUsername");
    assertEquals(optionalNullUser.getClass(), Optional.class);

    assertFalse(optionalNullUser.isPresent());
  }
}
