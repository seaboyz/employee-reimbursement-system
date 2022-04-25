package com.revature.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.UnavailableException;

import com.revature.database.PostgreSQLDatabase;
import com.revature.models.Role;
import com.revature.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {
  static final String EXIST_USERNAME = "EXIST_USERNAME";
  static final String NONE_EXIST_USERNAME = "NONE_EXIST_USERNAME";

  UserDao userDao;

  Connection connection;

  // @Mock
  // User mockUser;

  // @Mock
  // Connection mockConnection;

  // @Mock
  // Statement mockStatement;

  // @Mock
  // ResultSet mockResultSet;

  // @Mock
  // PreparedStatement mockPreparedStatement;

  @BeforeEach
  void init() throws SQLException, UnavailableException {
    connection = PostgreSQLDatabase.getConnection();
    userDao = new UserDao(connection);
  }

  @Nested
  class TestGetById {

    @Test
    void getShouldReturnUserWithRoleWhenPassingId() throws SQLException {

      User user = userDao.get(35).get();

      assertEquals(User.class, user.getClass());
      assertEquals(35, user.getId());
      assertEquals(Role.FINANCE_MANAGER, user.getRole());
    }
  }

}
