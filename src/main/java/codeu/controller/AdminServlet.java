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
import codeu.model.store.persistence.PersistentStorageAgent;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentDataStore;

/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {
//  private PersistentStorageAgent PersistentStorageAgent;
  private UserStore userStore;

  public void init() throws ServletException{
    super.init();
    setUserStore(UserStore.getInstance());
  }

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
/* checks to see if user is admin if false redrects them to the login page*/
    String username = request.getParameter("username");
    User user = userStore.getUser(username);
    if(user.getAdmin() == false){
      response.sendRedirect("/login");
      return;
    }else if (user.getAdmin() == true){
      /*setting the attribute*/
      int[] c = getData();

      request.setAttribute("Users", c[0]);

      request.setAttribute("Conversations", c[1]);

      request.setAttribute("Messages", c[2]);

      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
    }
  }

  public int[] getData() throws PersistentDataStoreException{
      /*getting information about the chat up from the persistentDataStore*/
    PersistentStorageAgent data = PersistentStorageAgent.getInstance();
    int[] v = new int[3];
    int numOfUsers = data.loadUsers().size();
    v[0] = numOfUsers;
    int numOfConvos = data.loadConversations().size();
    v[1] = numOfConvos;
    int Messages = data.loadMessages().size();
    v[2] = Messages;
    return v;
  }
}
