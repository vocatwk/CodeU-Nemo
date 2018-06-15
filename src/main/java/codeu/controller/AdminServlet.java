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
  private ConversationStore ConversationStore;
  private MessageStore MessageStore;
  private List<String> adminList = new ArrayList<String>();
  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(userStore.getInstance());
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
        adminList.add("admin");
        adminList.add("admin1");
        adminList.add("admin2");
        adminList.add("admin3");

      String username = (String)request.getSession().getAttribute("user");
      if (username == null) {
        // user is not logged in, don't let them access the admin page
        response.sendRedirect("/");
        return;
      }
      User user = userStore.getUser(username);
<<<<<<< HEAD
      if (adminList.contains(username)){
        user.setIsAdmin(true);
      }

=======
>>>>>>> parent of afb1bd2... added admin tab to navbar. Admin tab should only show when user is an admin
      if (user == null) {
        // user was not found, don't let them access the admin page
        response.sendRedirect("/");
        return;
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

          for(int i =0; i< numOfUsers; i++){
            UUID userId = userList.get(i).getId();
            int userMessagesListSize = messageStore.getMessagesFromAuthor(userId).size();
            if (userMessagesListSize > mostMessages){
              mostMessages = messageStore.getMessagesFromAuthor(userId).size();
              mostActiveUser = userStore.getUser(userId).getName();
            }else if(mostActiveUser == null){
              mostActiveUser = "We need activity";
            }
            }

            request.setAttribute("numOfAdmins", numOfAdmins);
            request.setAttribute("newestUser", newestUser);
            request.setAttribute("mostActiveUser", mostActiveUser);
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }


}
