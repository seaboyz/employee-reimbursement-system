package com.revature.database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

public class PostgreSQLDatabaseTest {
  @Test
  public void testGetConnectionDoReturnConnection() {
    Connection connection = PostgreSQLDatabase.getConnection();
    assertNotNull(connection);
  }
}
