package com.revature.util;

import com.revature.models.User;

public class Util {
  public static User shallowCloneUser(User user) {
    return new User(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getFirstname(),
        user.getLastname(),
        user.getRole());
  }
}
