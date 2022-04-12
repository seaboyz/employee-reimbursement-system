package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.revature.database.PostgreSQLDatabase;
import com.revature.models.User;

public class UserDAOImplementation implements UserDAO {
  static Connection conn = PostgreSQLDatabase.getConnection();

  public static void main(String[] args) {
    UserDAOImplementation uDaoImpl = new UserDAOImplementation();
    try {
      User user = uDaoImpl.getByUsername("test");
      System.out.println(user);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * <ul>
   * <li>Should Insert a new User record into the DB with the provided
   * information.</li>
   * <li>Should throw an exception if the creation is unsuccessful.</li>
   * <li>Should return a User object with an updated ID.</li>
   * </ul>
   */
  @Override
  public void add(User newUser) throws SQLException {
    String query = "INSERT INTO ERS_USERS "
        + "(ERS_USER_NAME,ERS_PASSWORD,ERS_EMAIL,ERS_FIRST_NAME,ERS_LAST_NAME) "
        + "VALUES "
        + "(?, ?, ?, ? ?) ";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, newUser.getUsername());
    ps.setString(2, newUser.getPassword());
    ps.setString(3, newUser.getEmail());
    ps.setString(4, newUser.getFirstname());
    ps.setString(5, newUser.getLastname());
    ps.executeUpdate();
  }

  /**
   * Should retrieve a User from the DB with the corresponding username or an
   * empty optional if there is no match.
   */
  public User getByUsername(String username) throws SQLException {
    User user = new User();
    String query = "SELECT * FROM ERS_USERS "
        + "WHERE ERS_USER_NAME = "
        + "?";
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(query);

    while (resultSet.next()) {
      user.setId(resultSet.getInt("id"));
      user.setUsername(resultSet.getString("ERS_USER_NAME"));
      user.setPassword(resultSet.getString("ERS_PASSWORD"));
      user.setPassword(resultSet.getString("ERS_EMAIL"));
      user.setPassword(resultSet.getString("ERS_FIRST_NAME"));
      user.setPassword(resultSet.getString("ERS_LAST_NAME"));
    }
    return user;

  }

  @Override
  public User getUserById(int id) throws SQLException {

    return null;
  }

  @Override
  public List<User> all() throws SQLException {

    return null;
  }

  @Override
  public void update(User user) throws SQLException {

  }

  @Override
  public void delete(int id) throws SQLException {

  }

}
