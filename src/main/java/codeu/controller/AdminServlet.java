package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {

  private UserStore userStore;

  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
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

  /**
   * This function fires when a user requests the /login URL. It simply forwards the request to
   * login.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the login form. It gets the username and password from
   * the submitted form data, checks for validity and if correct adds the username to the session so
   * we know the user is logged in.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    User user = userStore.getUser(username);

    if (!userStore.isUserRegistered(username)) {
        request.setAttribute("error", "That username is not an admin.");
        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        return;
    } else if (userStore.isUserRegistered(username)){
        if(user.getAdmin() == false){
      request.setAttribute("error", "That username is not an admin.");
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
      return;
      }
    }

    if (!BCrypt.checkpw(password, user.getPasswordHash())) {
      request.setAttribute("error", "Please enter a correct password.");
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
      return;
    }
    request.getSession().setAttribute("user", username);
      response.sendRedirect("/admin");

  }

}
