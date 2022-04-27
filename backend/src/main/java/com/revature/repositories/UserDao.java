package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.exceptions.UserNotExistException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.Util;

public class UserDao implements Dao<User> {
  private final Connection connection;

  public UserDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public User add(User user) throws SQLException {
    String query = "INSERT INTO ERS_USERS "
        +
        "(ERS_USER_NAME,ERS_PASSWORD,ERS_EMAIL,ERS_FIRST_NAME,ERS_LAST_NAME)"
        + "VALUES "
        + "(?, ?, ?, ?, ?)";
    PreparedStatement ps = connection.prepareStatement(query,
        Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, user.getUsername());
    ps.setString(2, user.getPassword());
    ps.setString(3, user.getEmail());
    ps.setString(4, user.getFirstname());
    ps.setString(5, user.getLastname());

    ps.execute();
    // get auto generated key back
    ResultSet keys = ps.getGeneratedKeys();
    if (keys.next()) {
      int id = keys.getInt(1);
      user.setId(id);
      user.setRole(Role.EMPLOYEE);
      return user;
    } else {
      throw new SQLException("Fail save new user to database");
    }

  }

  public Optional<User> get(String username) throws SQLException {

    Statement statement = connection.createStatement();
    String query = "select * from ers_users where ers_user_name = '" + username + "'";
    ResultSet resultSet = statement.executeQuery(query);

    User user = new User();

    if (resultSet.next()) {
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
      } else if (roleId == 2) {
        user.setRole(Role.FINANCE_MANAGER);
      } else if (roleId == 3) {
        user.setRole(Role.NOT_CURRENT_EMPLOYEE);
      } else {
        return Optional.empty();
      }
      return Optional.of(user);
    }
    return Optional.empty();
  }

  public Optional<User> get(int id) throws SQLException {

    Statement statement = connection.createStatement();
    String query = "select * from ers_users where ers_user_id = '" + id + "'";
    ResultSet resultSet = statement.executeQuery(query);

    User user = new User();

    if (resultSet.next()) {
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
      } else if (roleId == 2) {
        user.setRole(Role.FINANCE_MANAGER);
      } else if (roleId == 3) {
        user.setRole(Role.NOT_CURRENT_EMPLOYEE);
      }

      return Optional.of(user);
    } else {
      throw new SQLException();
    }

  }

  @Override
  public List<User> getAll() throws SQLException {
    String query = "SELECT * FROM ers_users";

    List<User> users = new ArrayList<>();
    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      User user = new User();
      user.setId(rs.getInt("ers_user_id"));
      user.setUsername(rs.getString("ers_user_name"));
      user.setPassword(rs.getString("ers_password"));
      user.setEmail(rs.getString("ers_email"));
      user.setFirstname(
          rs.getString("ers_first_name"));
      user.setLastname(rs.getString("ers_last_name"));
      int roleId = rs.getInt("user_role_id");
      if (roleId == 1) {
        user.setRole(Role.EMPLOYEE);
      } else if (roleId == 2) {
        user.setRole(Role.FINANCE_MANAGER);
      } else if (roleId == 3) {
        user.setRole(Role.NOT_CURRENT_EMPLOYEE);
      }
      users.add(user);
    }
    return users;
  }

  @Override
  public User update(User user) throws SQLException {

    String sql = "UPDATE ERS_USERS SET ERS_USER_NAME = ?, ERS_EMAIL = ?, ERS_PASSWORD = ? WHERE ERS_USER_ID = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, user.getUsername());
    preparedStatement.setString(2, user.getEmail());
    preparedStatement.setString(3, user.getPassword());
    preparedStatement.setInt(4, user.getId());

    if (preparedStatement.executeUpdate() == 1) {
      return Util.shallowCloneUser(user);
    } else {
      System.out.println("update failed");
    }
    return null;
  }

  @Override
  public void delete(User user) throws SQLException {

  }

  @Override
  public void delete(int id) throws SQLException {
    String query = "DELETE FROM ERS_USERS WHERE ERS_USER_ID = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, id);
    if (ps.executeUpdate() == 0) {
      throw new UserNotExistException();
    }
  }

}
