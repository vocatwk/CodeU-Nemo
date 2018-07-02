package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
<<<<<<< HEAD
import codeu.model.data.Event;
import codeu.model.store.basic.EventStore;
=======
>>>>>>> 96487ee0cecaf6be8757454013aace6008fcd33f
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.util.UUID;


public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
  private EventStore eventStore;

=======
import java.time.Instant;

public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
>>>>>>> 96487ee0cecaf6be8757454013aace6008fcd33f
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
<<<<<<< HEAD
    setEventStore(EventStore.getInstance());
=======
>>>>>>> 96487ee0cecaf6be8757454013aace6008fcd33f
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
<<<<<<< HEAD
  /**
   * Sets the EventStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setEventStore(EventStore eventStore){
    this.eventStore = eventStore;
  }
=======
>>>>>>> 96487ee0cecaf6be8757454013aace6008fcd33f

//TODO Decide if other notification types beyond messages are needed
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
<<<<<<< HEAD
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

=======
        String username = request.getParameter("username");
        User user = userStore.getUser(username);
        Instant userLookedAtPage = Instant.now();

        System.out.println(user.getLastSeenNotificationsTimestamp());
        user.setLastSeenNotificationTimestamp(userLookedAtPage);
>>>>>>> 96487ee0cecaf6be8757454013aace6008fcd33f
        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
      }

}
