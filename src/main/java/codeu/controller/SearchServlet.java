package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/** Servlet class responsible for the search feature. */
public class SearchServlet extends HttpServlet {




  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling search requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common 
   * setup method for use by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function gets the Users that contains the searchRequest (username) 
   * and sets up the JSON object to be parsed. 
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    String searchRequest = (String)request.getParameter("searchRequest");

    String json = new Gson().toJson(userStore.getUsers(searchRequest));
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

    // TODO Determine if we want to include Conversations and Messages
  }
}
