package com.revature.database;

import com.revature.models.User;

import java.util.ArrayList;

public interface Database {
  ArrayList<User> findAllUsers();
  User getUserByUsername(String username);
}
