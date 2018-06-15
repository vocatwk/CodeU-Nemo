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
      if (adminList.contains(username)){
        user.setIsAdmin(true);
      }
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
          User newestUser = userList.get(userStore.getNumOfUsers()-1);//.getName();
          String mostActiveUser = "";
          int numOfAdmins = adminList.size();

          for(int i =0; i< numOfUsers; i++){
            UUID userId = userList.get(i).getId();
            int userMessagesListSize = messageStore.getMessagesFromAuthor(userId).size();
            if( userList.get(i) <= newestUser.getCreationTime()){
              int userTime = userList.get(i).getCreationTime();
            }
            if (userMessagesListSize > mostMessages){
              mostMessages = userMessagesListSize;
              mostActiveUser = userStore.getUser(userId).getName();
            }else if(mostActiveUser == null){
              mostActiveUser = "We need activity";
            }
            }

            request.setAttribute("timeCreated",userTime )

            request.setAttribute("numOfAdmins", numOfAdmins);
            //request.setAttribute("newestUser", newestUser);
            request.setAttribute("mostActiveUser", mostActiveUser);
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }


}
