// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SessionFilterTest {

  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private FilterChain mockFilterChain;
  private ServletRequest mockServletRequest;
  private ServletResponse mockServletResponse;
  private SessionFilter sessionFilter;
  private FilterConfig mockFilterConfig;

  @Before
  public void setup() throws ServletException {
    sessionFilter = new SessionFilter();

    mockServletRequest = Mockito.mock(HttpServletRequest.class);
    mockRequest = (HttpServletRequest) mockServletRequest;

    mockServletResponse = Mockito.mock(HttpServletResponse.class);
    mockResponse = (HttpServletResponse) mockServletResponse;

    mockFilterChain = Mockito.mock(FilterChain.class);
    mockFilterConfig = Mockito.mock(FilterConfig.class);

    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    sessionFilter.init(mockFilterConfig);
    sessionFilter.setUserStore(mockUserStore);
  }

  @Test
  public void testDoFilter_loggedInHasAccess() throws IOException, ServletException {

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/test_conversation");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User mockUser = Mockito.mock(User.class);
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);

    sessionFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
    
    Mockito.verify(mockFilterChain).doFilter(mockServletRequest, mockServletResponse);
  }

  @Test
  public void testDoFilter_loggedOutHasAccess() throws IOException, ServletException {

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/login");
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    sessionFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
    
    Mockito.verify(mockFilterChain).doFilter(mockServletRequest, mockServletResponse);
  }

  @Test
  public void testDoFilter_loggedOutNoAccess() throws IOException, ServletException {

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/conversations");
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    sessionFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
    
    Mockito.verify(mockRequestDispatcher).forward(mockServletRequest, mockServletResponse);
  }

  @Test
  public void testDoFilter_loggedInNoAccess() throws IOException, ServletException {

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/login");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User mockUser = Mockito.mock(User.class);
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);

    sessionFilter.doFilter(mockServletRequest, mockServletResponse, mockFilterChain);
    
    Mockito.verify(mockRequestDispatcher).forward(mockServletRequest, mockServletResponse);
  }
}
