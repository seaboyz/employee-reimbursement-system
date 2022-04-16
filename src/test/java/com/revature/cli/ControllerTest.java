package com.revature.cli;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class ControllerTest {
  User loggedInUser;

  @Mock
  AuthService mockAuthService;

  @Mock
  UserService mockUserService;

  @BeforeEach
  void init() {
    Controller controller = new Controller(mockAuthService, mockUserService);

  }

  @Test
  void upDateShouldUpdateUser(String username, String password, String email) {

    loggedInUser = new User(1, "test", "123456", "test@test.com", "john", "doe", Role.EMPLOYEE);

  }

}
