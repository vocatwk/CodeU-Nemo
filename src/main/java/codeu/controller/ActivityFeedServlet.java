package codeu.controller;

import codeu.model.data.Event;
import codeu.model.store.basic.EventStore;
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

  /** Set up state for handling activty feed requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setEventStore(EventStore.getInstance());
  }

  /**
   * Sets the EventStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setEventStore(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  /**
   * This function fires when a user navigates to the activity feed page. It 
   * simply forwards to activityfeed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
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
