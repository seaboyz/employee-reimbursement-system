package com.revature.repositories;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.User;

public interface UserDAO {
  public void add(User user) throws SQLException;

  public User getUserById(int id) throws SQLException;

  public User getUserByUsername(String username) throws SQLException;

  public List<User> all() throws SQLException;

  public void update(User user) throws SQLException;

  public void delete(int id) throws SQLException;

}
