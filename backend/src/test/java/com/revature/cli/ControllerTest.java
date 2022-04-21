package com.revature.cli;

import java.sql.SQLException;

import com.revature.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO Integration testing
@ExtendWith(MockitoExtension.class)
public class ControllerTest {
  Controller controller;
  Employee employee;

  @BeforeEach
  void init() {
    employee = new Employee(1, "test", "123456", "test@test.com", "john", "doe", "employee");
  }

  @Nested
  class TestUpdate {

    @Test
    void ShouldUpdatedEmployee() throws SQLException {

    }

    @Nested
    class TestRegister {

      @Test
      void registerShouldReturnAUserWithIdAndRole() throws SQLException {

      }

    }

    @Nested
    class TestLogin {

      @Test
      void ShouldReturnEmployee() throws SQLException {

      }

      @Test
      void shouldThrowUserNotExitException() throws SQLException {
      }

      @Test
      void shouldThrowUserNamePasswordNotMatchException() throws SQLException {
      }
    }

  }

  @Nested
  class TestRemoveUser {
    User currentEmployee;
    User notCurrentEmployee;

    @Test
    void ShouldReturnAUserWithRoleNOT_CURRENT_EMPLOYEE() throws SQLException {

    }

  }

}