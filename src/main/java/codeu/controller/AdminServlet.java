package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.data.Conversation;
import codeu.model.store.basic.ConversationStore;
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

  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
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
  /**
   * This function fires when a user requests the /login URL. It simply forwards the request to
   * login.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);

    String username = request.getParameter("username");

    User user = userStore.getUser(username);
    if(user.getAdmin() == false){
      response.sendRedirect("/login");
      return;
    }else if (user.getAdmin() == true){

    int numOfUsers = userStore.getAllUsers().size();

    int numOfConvos = ConversationStore.getAllConversations().size();

    request.setAttribute("Users", numOfUsers);

    request.setAttribute("Conversations", numOfConvos);

    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);

    }

}


}
