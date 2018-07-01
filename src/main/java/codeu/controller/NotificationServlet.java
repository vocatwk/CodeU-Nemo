package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.data.Event;
import codeu.model.store.basic.EventStore;
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


public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
  private EventStore eventStore;

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

//TODO Decide if other notification types beyond messages are needed
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        String username = (String)request.getSession().getAttribute("user");
        User user = userStore.getUser(username);
        Instant userLookedAtPage = Instant.now();
        Instant lastSeenTime = user.getLastSeenNotifications();
        UUID lastSeenId = null;
        List<Event> events = eventStore.getAllEvents();
        for(Event event : events){
          if(lastSeenTime.equals(event.getCreationTime())){
            lastSeenId = event.getId();
          }
        }
        Event lastSeen = eventStore.getEvent(lastSeenId);
        List<Event> eventsToShow = eventStore.getEventsSince(lastSeen);
        user.setLastSeenNotifications(userLookedAtPage);

        request.setAttribute("eventsToShow",eventsToShow);

        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
      }

}
