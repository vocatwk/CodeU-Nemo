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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

/** Filter class responsible for user access. */
public class SessionFilter implements Filter {
  
  /** used to pass information to this filter during initialization. */
  private FilterConfig filterConfig;
  
  /** Store class that gives access to users. */
  private UserStore userStore;

  /** Set up state for handling user access. */
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
    setUserStore(UserStore.getInstance());

  }

  /** Sets the userStore used by this filer. */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /** not yet used but has to be implemented because this class implements Filter interface */
  public void destroy() {

  }

  /** This function fires when a user navigates to any page. It checks if a user is logged in or
   * not and redirects as necessary.
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain
      filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    
    String requestUrl = request.getRequestURI();

    String username = (String) request.getSession().getAttribute("user");
    
    if(username == null || userStore.getUser(username) == null) {
      // user not logged in or not found
      if(!(requestUrl.equals("/") || requestUrl.equals("/login") || requestUrl.equals("/register"))) {
        //user does not have access, redirect to index.
        request.getRequestDispatcher("/").forward(request,response);
        return;
      }
    }
    else if (requestUrl.equals("/login") || requestUrl.equals("/register")) {
      //user logged in, restrict access to login/register page until logout
      request.getRequestDispatcher("/").forward(request,response);
      return;
    }

    filterChain.doFilter(servletRequest,servletResponse);
    
  }
}


