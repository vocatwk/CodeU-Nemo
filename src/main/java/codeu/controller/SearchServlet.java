package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/** Servlet class responsible for the search feature. */
public class SearchServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Set up state for handling search requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common 
   * setup method for use by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a 
   * common setup method for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the EventStore used by this servlet. This function provides a common 
   * setup method for use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * TODO: Documentation
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    System.out.println("In doPost()");
    String searchRequest = (String)request.getParameter("searchRequest");
    request.setAttribute("searchRequest", searchRequest);

    List<User> users = userStore.getUsers(searchRequest);
    request.setAttribute("users", users);

    String json = new Gson().toJson(users);
    System.out.println("Response Type: " + response.getContentType());
    response.setContentType("application/json");
    System.out.println("Response Type: " + response.getContentType());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

    // List<Conversation> conversations = conversationStore.getAllConversations();
    // request.setAttribute("conversations", conversations);

    // List<Message> messages = messageStore.getAllMessages();
    // request.setAttribute("messages", messages);

    request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
  }

  /**
  * TODO: Determine if this is needed.
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    // TODO
  }

}