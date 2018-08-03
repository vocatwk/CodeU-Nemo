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
        List<UUID> conversationSubbedTo = user.getSubscriptions();
        List<Event> eventsLastSeen = eventStore.getEventsSince(lastSeenTime);

        List<Event> eventsToShow = new ArrayList<>();

        for(Event subscribedEvent:eventsLastSeen){
          String eventType = subscribedEvent.getType();
          List<String> information = subscribedEvent.getInformation();
          if(eventType.equals("Message") && information.size() > 3){
            String convoId = information.get(3);
            UUID conversationId = UUID.fromString(convoId);
            if (conversationSubbedTo.contains(conversationId)){eventsToShow.add(subscribedEvent);}
          } else if(eventType.equals("Conversation") && information.size() > 2){
            String convoId = information.get(2);
            UUID conversationId = UUID.fromString(convoId);
            if (conversationSubbedTo.contains(conversationId)){eventsToShow.add(subscribedEvent);}
          }

        }

        request.setAttribute("eventsToShow",eventsToShow);
        user.setLastSeenNotifications(lastEventTime);

        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
      }

}
