package codeu.controller;

import codeu.model.data.Event;
import codeu.model.data.User;
import codeu.model.store.basic.EventStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the activty feed page. */
public class ActivityFeedServlet extends HttpServlet {

  /** Store class that gives access to Events. */
  private EventStore eventStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling activty feed requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setEventStore(EventStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the EventStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setEventStore(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  /**
   * Sets the UserStore used by this servlet.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the activity feed page. It 
   * simply forwards to activityfeed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them access activity page
      response.sendRedirect("/");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them create access activity page
      response.sendRedirect("/");
      return;
    }

    List<Event> events = eventStore.getAllEvents();
    request.setAttribute("events", events);

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
