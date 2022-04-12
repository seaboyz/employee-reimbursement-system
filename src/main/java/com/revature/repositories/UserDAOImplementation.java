package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.revature.database.PostgreSQLDatabase;
import com.revature.models.User;

public class UserDAOImplementation implements UserDAO {
  static Connection conn = PostgreSQLDatabase.getConnection();

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
  public Optional<User> getByUsername(String username) {

    return Optional.empty();
  }

  @Override
  public User getUserById(int id) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User getUserByUsername(String username) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<User> all() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(User user) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(int id) throws SQLException {
    // TODO Auto-generated method stub

  }

}
