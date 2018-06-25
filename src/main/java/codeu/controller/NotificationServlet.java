package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Notification;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.NotificationStore;
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
  private NotificationStore notificationStore;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setNotificationStore(NotificationStore.getInstance());
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
  void setNotificationStore(NotificationStore notificationStore){
    this.notificationStore = notificationStore;
  }

//TODO Decide if other notification types beyond messages are needed
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        String username = (String)request.getSession().getAttribute("user");
        User userIsReciever = userStore.getUser(username);
        UUID userId = userIsReciever.getId();
        List<Notification> allNotifications = notificationStore.getAllNotification();
        List<Notification> userNotifications = notificationStore.NotificationsForUser(userId);
        request.setAttribute("userNotifications", userNotifications);
        request.getRequestDispatcher("/WEB-INF/view/notification.jsp").forward(request, response);
      }

}
