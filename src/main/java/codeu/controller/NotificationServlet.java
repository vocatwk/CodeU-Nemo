package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import codeu.model.data.Event;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.util.UUID;
import java.time.Clock;

public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
  private EventStore eventStore;
  private Clock clock = Clock.systemUTC();


  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setEventStore(EventStore.getInstance());
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  /**
   * Sets the EventStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setEventStore(EventStore eventStore){
    this.eventStore = eventStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        Instant currentTime = clock.instant();
        long nanosTosubtract = currentTime.getNano();
        Instant lastEventTime = currentTime.minusNanos(nanosTosubtract);

        String username = (String)request.getSession().getAttribute("user");
        User user = userStore.getUser(username);
        Instant lastSeenTime = user.getLastSeenNotifications();
        List<Event> eventsToShow = eventStore.getEventsSince(lastSeenTime);


        request.setAttribute("eventsToShow",eventsToShow);
        user.setLastSeenNotifications(lastEventTime);

        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
      }

}
