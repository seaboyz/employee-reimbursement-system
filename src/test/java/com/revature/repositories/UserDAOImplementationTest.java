package com.revature.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

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
public class UserDAOImplementationTest {
  static final String EXIST_USERNAME = "EXIST_USERNAME";
  static final String NONE_EXIST_USERNAME = "NONE_EXIST_USERNAME";

  UserDAOImplementation userDao;

  @Mock
  User mockUser;

  @Mock
  Connection mockConnection;

  @Mock
  Statement mockStatement;

  @Mock
  ResultSet mockResultSet;

  @Mock
  PreparedStatement mockPreparedStatement;

  @BeforeEach
  void init() {
    userDao = new UserDAOImplementation(mockConnection);
  }

  @Test
  void addshouldReturnUserWithUpdatedId() throws SQLException {
    User newUser = new User(1, "test", "123456", "test@test.com", "john", "doe",
        Role.EMPLOYEE);

    String query = "INSERT INTO ERS_USERS "
        + "(ERS_USER_NAME,ERS_PASSWORD,ERS_EMAIL,ERS_FIRST_NAME,ERS_LAST_NAME) "
        + "VALUES "
        + "(?, ?, ?, ? ?) ";

    when(mockConnection.prepareStatement(query)).thenReturn(mockPreparedStatement);

    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);

    when(mockResultSet.next()).thenReturn(true);

    when(mockResultSet.getInt(1)).thenReturn(anyInt());

    User updatedUser = userDao.add(newUser);

    assertNotEquals(999, userDao.add(updatedUser).getId());
  }

  @Nested
  @DisplayName("Test for return Optional")
  class TestGetByUsernameReturnOptional {
    User TEST_USER;

    @BeforeEach
    public void init() throws SQLException {

      when(mockConnection.createStatement()).thenReturn(mockStatement);

      when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

      TEST_USER = new User(1, "testUsername", "testPassword", "test@test.com", "testFirstname ", "testLastName",
          Role.EMPLOYEE);
    }

    @Test
    public void testGetByUsernameShouldReturnOptionalWithNoneExistUsername() throws Exception {
      when(mockResultSet.next()).thenReturn(false);
      Optional<User> noneExitUser = userDao.getByUsername(NONE_EXIST_USERNAME);
      assertEquals(noneExitUser.getClass(), Optional.class);
    }

    public void testGetByUsernameShouldReturnOptionalWithExistUsername() throws Exception {
      when(mockResultSet.next()).thenReturn(true);
      Optional<User> noneExitUser = userDao.getByUsername(EXIST_USERNAME);
      assertEquals(noneExitUser.getClass(), Optional.class);
      assertFalse(noneExitUser.isPresent());
    }

    @Test
    public void testGetByUsernameShouldReturnTheRightUser() throws Exception {
      when(mockResultSet.next()).thenReturn(true);
      when(mockResultSet.getInt("ers_user_id")).thenReturn(TEST_USER.getId());
      when(mockResultSet.getString("ers_user_name")).thenReturn(TEST_USER.getUsername());
      when(mockResultSet.getString("ers_password")).thenReturn(TEST_USER.getPassword());
      when(mockResultSet.getString("ers_email")).thenReturn(TEST_USER.getEmail());
      when(mockResultSet.getString("ers_first_name")).thenReturn(TEST_USER.getFirstname());
      when(mockResultSet.getString("ers_last_name")).thenReturn(TEST_USER.getLastname());
      when(mockResultSet.getInt("user_role_id")).thenReturn(1);

      Optional<User> optionalUser = userDao.getByUsername(TEST_USER.getUsername());
      User user = optionalUser.get();

      assertEquals(optionalUser.getClass(), Optional.class);

      assertEquals(user.getUsername(), TEST_USER.getUsername());
      assertEquals(user.getEmail(), TEST_USER.getEmail());
      assertEquals(user.getPassword(), TEST_USER.getPassword());
      assertEquals(user.getFirstname(), TEST_USER.getFirstname());
      assertEquals(user.getLastname(), TEST_USER.getLastname());
      assertEquals(user.getRole(), TEST_USER.getRole());
    }
  }

}
