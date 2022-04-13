package com.revature.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.revature.models.User;

public interface UserDAO {
  public void add(User user) throws SQLException;

  public Optional<User> getUserById(int id) throws SQLException;

  public Optional<User> getByUsername(String username) throws SQLException;

  public List<Optional<User>> all() throws SQLException;

  public void update(User user) throws SQLException;

  public void delete(int id) throws SQLException;

}
