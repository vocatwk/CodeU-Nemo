package codeu.controller;

import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the profile page. */
public class ProfileServlet extends HttpServlet {
   
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling profile requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the profile page. It gets the profile name from
   * the URL, finds the corresponding profile subject, and fetches the messages sent by that subject.
   * It then forwards to profile.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();

    if(requestUrl.length() <= "/profile/".length()){
      // if user navigates to "/profile/" without a specific user
      // TODO respond with 404
      response.sendRedirect("/");
      return;
    }
    String subjectName = requestUrl.substring("/profile/".length());
	
    
    User subject = userStore.getUser(subjectName);

    if(subject == null) {
      // couldn't file profile, redirect to index.jsp
      // TODO respond with 404
      response.sendRedirect("/");
      return;
    }

    request.setAttribute("subject", subjectName);
    UUID subjectId = subject.getId();

    List<Message> messages = messageStore.getMessagesFromAuthor(subjectId);
    
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the profile page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
  	
	// TO-DO

  }
}
