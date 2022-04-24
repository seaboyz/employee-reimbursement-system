package com.revature.servlets;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

  @Nested
  class TestDoGet {

    @Test
    void shouldSetStatus200() throws IOException, ServletException {

      when(req.getHeader("Authorization")).thenReturn(
          "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlkIjozNCwiaXNBZG1pbiI6ZmFsc2UsInVzZXJuYW1lIjoidGVzdCJ9.Mgk6_NtdcoqRtqPxPE5prpVZJ0c55Kxv-gSdXmtwFf8");

      when(req.getPathInfo()).thenReturn("users/34");

      userServlet.doGet(req, res);

      verify(res, times(1)).setStatus(200);
    }

    @Test
    void shouldSetStatus401() throws IOException, ServletException {

      when(req.getHeader("Authorization")).thenReturn(
          null);

      userServlet.doGet(req, res);

      verify(res, times(1)).setStatus(401);
    }

    @Test
    void shouldSetStatus401TryToGetOtherUserInfo() throws IOException, ServletException {

      when(req.getHeader("Authorization")).thenReturn(
          "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlkIjozNCwiaXNBZG1pbiI6ZmFsc2UsInVzZXJuYW1lIjoidGVzdCJ9.Mgk6_NtdcoqRtqPxPE5prpVZJ0c55Kxv-gSdXmtwFf8");

      when(req.getPathInfo()).thenReturn("users/24");

      userServlet.doGet(req, res);

      verify(res, times(1)).setStatus(401);
    }
  }

}
