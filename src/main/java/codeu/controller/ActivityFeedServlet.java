package codeu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the activty feed page. */
public class ActivityFeedServlet extends HttpServlet {

  /** Set up state for handling activty feed requests. */
  @Override
  public void init() throws ServletException {
  	super.init();
  }

  /**
   * This function fires when a user navigates to the activity feed page. It 
   * simply forwards to activityfeed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

  /**
  * TODO: Describe functionality
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    // TODO
  }
}