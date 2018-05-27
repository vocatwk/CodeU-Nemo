package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.data.Conversation;
import codeu.model.store.basic.ConversationStore;
import codeu.model.data.Message;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {

  private UserStore userStore;
  private ConversationStore ConversationStore;
  private MessageStore MessageStore;

  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  void setConversationStore(ConversationStore ConversationStore) {
    this.ConversationStore = ConversationStore;
  }

  void setMessageStore(MessageStore MessageStore) {
    this.MessageStore = MessageStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
     String username = request.getParameter("username");

/* checks to see if user is admin if false redrects them to the login page*/
    User user = userStore.getUser(username);
    if(user.getAdmin() == false){
      response.sendRedirect("/login");
      return;
    }else if (user.getAdmin() == true){
      /* an attempt to grab information from the stores to display on the page */

    int numOfUsers = userStore.getAllUsers().size();

    int numOfConvos = ConversationStore.getAllConversations().size();

    int Messages = MessageStore.getMessages().size();

    request.setAttribute("Users", numOfUsers);

    request.setAttribute("Conversations", numOfConvos);

    request.setAttribute("Messages", Messages);

    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);



}


}
}
