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


/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {

  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private List<String> adminList = new ArrayList<>();
  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(userStore.getInstance());
    setConversationStore(conversationStore.getInstance());
    setMessageStore(messageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
      String username = (String)request.getSession().getAttribute("user");
      if (username == null) {
        // user is not logged in, don't let them access the admin page
        response.sendRedirect("/");
        return;
      }
      User user = userStore.getUser(username);
      if (user == null) {
        // user was not found, don't let them access the admin page
        response.sendRedirect("/");
        return;
      }
      //This is a tester to see if it prints = adminList.add("admin");
      if (user.getAdminTrue() == true) {
        if (!adminList.contains(user.getName())){
          adminList.add(user.getName());
        }
      }

      request.setAttribute("adminList", adminList);
      if (adminList.contains(user.getName())) {
      /* an attempt to grab information from the stores to display on the page
      if the user is admin*/
          List<User> userList = UserStore.getInstance().getAllUsers();
          int numOfUsers = userList.size();
          int numOfConvos = conversationStore.getInstance().getAllConversations().size();
          int numOfMessages = messageStore.getInstance().getAllMessages().size();
          String newestUser = userList.get(userList.size()-1).getName();
          String mostActiveUser = "";
          for(int i =0; i< numOfUsers; i++){
            UUID userId = userList.get(i).getId();
              int mostMessages=0;
            if (messageStore.getMessagesFromAuthor(userId).size() > mostMessages){
              mostMessages = messageStore.getMessagesFromAuthor(userId).size();
              mostActiveUser = userStore.getInstance().getUser(userId).getName();
            }
          }

          request.setAttribute("numOfUsers", numOfUsers);
          request.setAttribute("numOfMessages", numOfMessages);
          request.setAttribute("numOfConvos", numOfConvos);
          request.setAttribute("newestUser", newestUser);
          request.setAttribute("mostActiveUser", mostActiveUser);
          request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);

        } else if (!adminList.contains(user.getName())) {
            /* rediects user to homepage if not admin*/
          response.sendRedirect("/");
          return;
  }
}}
