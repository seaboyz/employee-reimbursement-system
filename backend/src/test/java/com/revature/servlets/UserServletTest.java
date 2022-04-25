package com.revature.servlets;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServletTest {

  UserServlet userServlet;

  @Mock
  HttpServletRequest req;

  @Mock
  HttpServletResponse res;

  @Mock
  PrintWriter out;

  @BeforeEach
  void init() throws IOException {

    userServlet = new UserServlet();
    userServlet.init();

    when(res.getWriter()).thenReturn(out);

  }

}
