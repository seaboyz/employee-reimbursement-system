package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import com.revature.models.Role;
import com.revature.models.User;

public class UserDAOImplementation implements UserDAO {
  private Connection connection;

  // public static void main(String[] args) {
  // UserDAOImplementation userDAOImplementation = new UserDAOImplementation();
  // try {
  // Optional<User> user = userDAOImplementation.getByUsername("test");
  // System.out.println(user);
  // } catch (Exception e) {
  // e.printStackTrace();
  // }

  // }

  public UserDAOImplementation(Connection connection) {
    this.connection = connection;
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
  public User add(User newUser) throws SQLException {
    User updatedUser = newUser;
    String query = "INSERT INTO ERS_USERS "
        + "(ERS_USER_NAME,ERS_PASSWORD,ERS_EMAIL,ERS_FIRST_NAME,ERS_LAST_NAME) "
        + "VALUES "
        + "(?, ?, ?, ? ?) ";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, newUser.getUsername());
    ps.setString(2, newUser.getPassword());
    ps.setString(3, newUser.getEmail());
    ps.setString(4, newUser.getFirstname());
    ps.setString(5, newUser.getLastname());
    if (ps.executeUpdate() == 1) {
      // get auto generated key back
      // https://www.baeldung.com/jdbc-returning-generated-keys
      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        int id = keys.getInt(1);
        updatedUser.setId(id);
      }
    } else {
      throw new SQLException("Could not get auto generated key from database");
    }

    return updatedUser;

  }

  /**
   * Should retrieve a User from the DB with the corresponding username or an
   * empty optional if there is no match.
   */
  public Optional<User> getByUsername(String username) throws SQLException {

    Statement statement = connection.createStatement();
    String query = "select * from ers_users where ers_user_name = '" + username + "'";
    ResultSet resultSet = statement.executeQuery(query);

    User user = new User();

    while (resultSet.next()) {
      user.setId(resultSet.getInt("ers_user_id"));
      user.setUsername(resultSet.getString("ers_user_name"));
      user.setPassword(resultSet.getString("ers_password"));
      user.setEmail(resultSet.getString("ers_email"));
      user.setFirstname(
          resultSet.getString("ers_first_name"));
      user.setLastname(resultSet.getString("ers_last_name"));
      int roleId = resultSet.getInt("user_role_id");
      if (roleId == 1) {
        user.setRole(Role.EMPLOYEE);
      } else {
        user.setRole(Role.FINANCE_MANAGER);
      }
      return Optional.of(user);
    }

    return Optional.empty();

  }

  @Override
  public Optional<User> getUserById(int id) throws SQLException {

    return Optional.empty();
  }

  @Override
  public List<Optional<User>> all() throws SQLException {

    return null;
  }

  @Override
  public void update(User user) throws SQLException {

  }

  @Override
  public void delete(int id) throws SQLException {

  }

}
