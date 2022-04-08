package com.revature.services;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

  private static UserService userService;
  private static UserDAO userDAO;

  private User GENERIC_EMPLOYEE_1;


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    userDAO = mock(UserDAO.class);
    userService = new UserService(userDAO);
  }

  @Before
  public void setUp() throws Exception {
    GENERIC_EMPLOYEE_1 = new User(1, "genericEmployee1", "genericPassword", Role.EMPLOYEE);
  }

  @Test
  public void testGetByUsernamePassesWhenUsernameExists() {
    when(userService.getByUsername("genericEmployee1")).thenReturn(Optional.of(GENERIC_EMPLOYEE_1));

    Assert.assertEquals(Optional.of(GENERIC_EMPLOYEE_1), userService.getByUsername(GENERIC_EMPLOYEE_1.getUsername()));

    verify(userDAO).getByUsername(GENERIC_EMPLOYEE_1.getUsername());
  }
}
