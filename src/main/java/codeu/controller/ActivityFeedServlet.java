package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the activty feed page. */
public class ActivityFeedServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Set up state for handling activty feed requests. */
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
   * Sets the MessageStore used by this servlet. This function provides a common 
   * setup method for use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * This function fires when a user navigates to the activity feed page. It 
   * simply forwards to activityfeed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    // Get and set Users
    List<User> users = userStore.getAllUsers();
    request.setAttribute("users", users);

    // Get and set Conversations
    List<Conversation> conversations = conversationStore.getAllConversations();
    request.setAttribute("conversations", conversations);

    // Get and set Messages
    List<Message> messages = messageStore.getAllMessages();
    request.setAttribute("messages", messages);

    // Make, sort, and set the event map
    Map<Instant, Object> sortedEventsMap = 
      new TreeMap<Instant, Object>(new Comparator<Instant>() {
        @Override
        public int compare(Instant o1, Instant o2) {
          return o2.compareTo(o1);
        }
      });
    for(User user : users)
      sortedEventsMap.put(user.getCreationTime(), user);
    for(Conversation conversation : conversations)
      sortedEventsMap.put(conversation.getCreationTime(), conversation);
    for(Message message : messages)
      sortedEventsMap.put(message.getCreationTime(), message);
    request.setAttribute("sortedEventsMap", sortedEventsMap);

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

  /**
  * TODO: Describe functionality
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    // TODO
  }
}
