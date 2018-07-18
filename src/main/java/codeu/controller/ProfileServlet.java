package codeu.controller;

import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Event;
import codeu.model.data.Conversation;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import codeu.model.store.basic.ConversationStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the profile page. */
public class ProfileServlet extends HttpServlet {

  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Events. */
  private EventStore eventStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Set up state for handling profile requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setEventStore(EventStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
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
   * Sets the EventStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setEventStore(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
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

    request.setAttribute("aboutMe", subject.getAboutMe());
    request.setAttribute("messages", messages);
    List<UUID> subscriptionsID = subject.getSubscription();
    List<String> conversationNames = new ArrayList<>();
    for (UUID subID : subscriptionsID) {
        Conversation convo = conversationStore.getConversation(subID);
        String convoName = convo.getTitle();
        conversationNames.add(convoName);
    }
    request.setAttribute("subscriptionsID",subscriptionsID);
    request.setAttribute("conversationNames", conversationNames);

    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the profile page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String requestUrl = request.getRequestURI();
    String username = (String) request.getSession().getAttribute("user");
    User subject = userStore.getUser(requestUrl.substring("/profile/".length()));

    if(!username.equals(subject.getName())){
      // user is trying to edit another user's profile
      // TODO respond with access denied
      response.sendRedirect("/login");
      return;
    }

    String aboutMe = request.getParameter("aboutMe");

    // remove any HTML from the About me message
    String cleanedAboutMe = Jsoup.clean(aboutMe, Whitelist.none());

    //set user's about me and update it in dataStore
    subject.setAboutMe(cleanedAboutMe);
    userStore.updateUser(subject);

    List<String> aboutMeInformation = new ArrayList<>();
    aboutMeInformation.add(subject.getName());
    aboutMeInformation.add(subject.getAboutMe());
    Event aboutMeEvent = new Event(
        UUID.randomUUID(), "About Me", Instant.now(), aboutMeInformation);
    eventStore.addEvent(aboutMeEvent);

    response.sendRedirect("/profile/" + subject.getName());
  }
}
