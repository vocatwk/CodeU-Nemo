package codeu.controller;

import codeu.model.data.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

public class NotificationServlet extends HttpServlet{
  private UserStore userStore;
  private EventStore eventStore;
  private Instant timeUserVistedPage;


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

  void setEventStore(EventStore eventStore){
    this.eventStore = eventStore;
  }

//TODO Decide if other notification types beyond messages are needed
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
      }

}
