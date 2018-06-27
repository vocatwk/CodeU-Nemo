package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
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



/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {

  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private List<String> adminList = Arrays.asList("admin","admin1","admin2","admin3");


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

  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

      String username = (String)request.getSession().getAttribute("user");
      User user = userStore.getUser(username);
      if (adminList.contains(username)){
        user.setIsAdmin(true);
      }
      if (user.getIsAdmin() == false) {
        /* rediects user to homepage if not admin*/
        response.sendRedirect("/");
        return;
      }

      /* an attempt to grab information from the stores to display on the page
      if the user is admin*/
      List<User> userList = userStore.getAllUsers();
      int numOfUsers = userStore.getNumOfUsers();
      int numOfConvos = conversationStore.getNumOfConversations();
      int numOfMessages = messageStore.getNumOfMessages();

      request.setAttribute("numOfUsers", numOfUsers);
      request.setAttribute("numOfMessages", numOfMessages);
      request.setAttribute("numOfConvos", numOfConvos);

      int mostMessages=0;
      String newestUser = userList.get(userList.size()-1).getName();
      String mostActiveUser = "";
      int numOfAdmins = adminList.size();

      for (User userIsActive : userStore.getAllUsers()) {
        UUID userId = userIsActive.getId();
        int userMessagesListSize = messageStore.getMessagesFromAuthor(userId).size();
        if (userMessagesListSize > mostMessages){
            mostMessages = userMessagesListSize;
            mostActiveUser = userIsActive.getName();
          }
      }

      request.setAttribute("numOfAdmins", numOfAdmins);
      request.setAttribute("newestUser", newestUser);
      request.setAttribute("mostActiveUser", mostActiveUser);
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
    }


}
