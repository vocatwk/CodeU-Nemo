package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
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


public class SubscriptionServlet extends HttpServlet{
  private UserStore userStore;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        String username = (String)request.getSession().getAttribute("user");
        User user = userStore.getUser(username);
        List<String> subscriptions = user.getSubscription();

        request.setAttribute("subscriptions",subscriptions);

        request.getRequestDispatcher("/WEB-INF/view/subscriptions.jsp").forward(request, response);
      }

}
