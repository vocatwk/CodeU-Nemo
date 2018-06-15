package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Event;
import codeu.model.store.basic.EventStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityFeedServletTest {

  private ActivityFeedServlet activityFeedServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private HttpSession mockSession;
  private RequestDispatcher mockRequestDispatcher;
  private EventStore mockEventStore;
  private UserStore mockUserStore;

  @Before
  public void setUp() {
    activityFeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockEventStore = Mockito.mock(EventStore.class);
    mockUserStore = Mockito.mock(UserStore.class);
    activityFeedServlet.setEventStore(mockEventStore);
    activityFeedServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("ExistingUsername");
    Mockito.when(mockUserStore.getUser("ExistingUsername")).thenReturn(
        new User(UUID.randomUUID(),"ExistingUserName", "randomPswdHash", Instant.now()));

    List<Event> fakeEventList = new ArrayList<>();

    UUID fakeUserId = UUID.randomUUID();
    User fakeUser = 
        new User(fakeUserId, "test_user", "test_password_hash", Instant.now());
    List<String> userInformation = new ArrayList<>();
    userInformation.add(fakeUser.getName());
    Event userEvent = new Event(
        UUID.randomUUID(), "User", fakeUser.getCreationTime(), userInformation);
    fakeEventList.add(userEvent);

    UUID fakeConversationId = UUID.randomUUID();
    Conversation fakeConversation = 
        new Conversation(fakeConversationId, fakeUserId, "test_conversation", Instant.now());
    List<String> conversationInformation = new ArrayList<>();
    conversationInformation.add(fakeUser.getName());
    conversationInformation.add(fakeConversation.getTitle());
    Event conversationEvent = new Event(
        UUID.randomUUID(), "Conversation", fakeConversation.getCreationTime(), conversationInformation);
    fakeEventList.add(conversationEvent);
    
    Message fakeMessage = new Message(
        UUID.randomUUID(), fakeConversationId, fakeUserId, "test_message", Instant.now());
    List<String> messageInformation = new ArrayList<>();
    messageInformation.add(fakeUser.getName());
    messageInformation.add(fakeConversation.getTitle());
    messageInformation.add(fakeMessage.getContent());
    Event messageEvent = new Event(
        UUID.randomUUID(), "Message", fakeMessage.getCreationTime(), messageInformation);
    fakeEventList.add(messageEvent);
    
    Mockito.when(mockEventStore.getAllEvents()).thenReturn(fakeEventList);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("events", fakeEventList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
    
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockEventStore, Mockito.never())
        .getAllEvents();
    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoGet_InvalidUser() throws IOException, ServletException {
    
    Mockito.when(mockSession.getAttribute("user")).thenReturn("nonExistingUsername");
    Mockito.when(mockUserStore.getUser("nonExistingUsername")).thenReturn(null);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockEventStore, Mockito.never())
        .getAllEvents();
    Mockito.verify(mockResponse).sendRedirect("/");
  }

}
