package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Notificationl
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.NotificationStore;
import codeu.model.store.basic.EventStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;
import java.util.Arrays;

public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private EventStore eventStore;
  private NotificationStore notificationStore;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setEventStore(EventStore.getInstance());
    setNotificationStore(NotifcationStore.getInstance());
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the NotificationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setNotificationStore(NotifcationStore notifcationStore){
    this.notificationStore = notificationStore;
  }

//TODO Decide if other notification types beyond messages are needed
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        String username = request.getAttribute("username");
        List<User> allUsers = UserStore.getInstance().getAllUsers();
        User userIsReciever = allUsers.getUser(username);
        UUID userId = userIsReciever.getID();
        List<Notification> allNotifications = NotifcationStore.getInstance().getAllNotification();
        List<Notification> userNotifications = NotificationStore.NotificationsForUser(userId);
        request.setAttribute("userNotifications", userNotifications);
      }

}
