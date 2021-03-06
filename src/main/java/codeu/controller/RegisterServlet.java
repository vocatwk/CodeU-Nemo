package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.User;
import codeu.model.data.Event;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;

public class RegisterServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Events. */
  private EventStore eventStore;

  /**
   * Set up state for handling registration-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
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
  void setEventStore(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  /**
   * This function fires when a user requests the /register URL. It simply forwards the request to
   * index.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setAttribute("registrationForm", true);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");

    if (username.trim().length() < 1) {
      request.setAttribute("error", "Username must contain at least one letter or number.");
      request.setAttribute("registrationForm", true);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      return;
    }

    if (!username.matches("[\\w*\\s*]*")) {
      request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
      request.setAttribute("registrationForm", true);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      return;
    }

    if (userStore.isUserRegistered(username)) {
      request.setAttribute("error", "That username is already taken.");
      request.setAttribute("registrationForm", true);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      return;
    }

    String password = request.getParameter("password");

    if (password.length() < 6) {
      request.setAttribute("error", "Password must contain at least 6 characters.");
      request.setAttribute("registrationForm", true);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      return;
    }

    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    User user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now());
    userStore.addUser(user);

    List<String> userInformation = new ArrayList<>();
    userInformation.add(user.getName());
    Event userEvent = new Event(UUID.randomUUID(), "User", user.getCreationTime(), userInformation);
    eventStore.addEvent(userEvent);

    response.sendRedirect("/index.jsp");
  }

}
