package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.revature.database.PostgreSQLDatabase;
import com.revature.models.Role;
import com.revature.models.User;

public class UserDAOImplementation implements UserDAO {
  static Connection conn = PostgreSQLDatabase.getConnection();

  public static void main(String[] args) {
    UserDAOImplementation userDAOImplementation = new UserDAOImplementation();
    try {
      User user = userDAOImplementation.getByUsername("test");
      System.out.println(user);
    } catch (Exception e) {
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

    Statement statement = conn.createStatement();
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
