package com.revature.database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

public class PostgreSQLDatabaseTest {
  @Test
  public void testGetConnectionDoReturnConnectionAndTheConnectionIsNotClosed() throws Exception {
    Connection connection = PostgreSQLDatabase.getConnection();
    assertNotNull(connection);
    assertFalse(connection.isClosed());
  }
}
