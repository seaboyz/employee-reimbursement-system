package com.revature.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.revature.models.User;

public interface Dao<T> {
  T add(User user) throws SQLException;

  Optional<T> get(int id) throws SQLException;

  Optional<T> get(String username) throws SQLException;

  List<T> getAll() throws SQLException;

  User update(T t) throws SQLException;

  void delete(T t) throws SQLException;

  void delete(int id) throws SQLException;
}
