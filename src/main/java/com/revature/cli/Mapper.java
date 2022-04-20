package com.revature.cli;

import com.revature.models.Role;
import com.revature.models.User;

public class Mapper {
  public Employee userToEmployee(User user) {
    return new Employee(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstname(),
        user.getLastname(), user.getRole().toString());
  }

  public User employeeToUser(Employee employee) {
    return new User(employee.getId(), employee.getUsername(), employee.getPassword(), employee.getEmail(),
        employee.getFirstname(), employee.getLastname(), Role.valueOf(employee.getRole()));
  }
}
