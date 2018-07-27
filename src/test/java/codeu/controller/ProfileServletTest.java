package codeu.controller;

import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Event;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private HttpSession mockSession;
  private RequestDispatcher mockRequestDispatcher;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private EventStore mockEventStore;
  private User mockUser;

  @Before
  public void setup() {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockSession = Mockito.mock(HttpSession.class);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockMessageStore = Mockito.mock(MessageStore.class);
    profileServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);

    mockEventStore = Mockito.mock(EventStore.class);
    profileServlet.setEventStore(mockEventStore);

    mockUser = Mockito.mock(User.class);

  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/testSubject");
  
    UUID fakeUserId = UUID.randomUUID();
    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            fakeUserId,
            "test message",
            Instant.now()));

    Mockito.when(mockUserStore.getUser("testSubject")).thenReturn(mockUser);
    Mockito.when(mockUser.getId()).thenReturn(fakeUserId);
    Mockito.when(mockMessageStore.getMessagesFromAuthor(fakeUserId))
        .thenReturn(fakeMessageList);

    profileServlet.doGet(mockRequest, mockResponse);
    
    Mockito.verify(mockRequest).setAttribute("subject", "testSubject");
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_badUrl() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile");

    profileServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoGet_BadProfile() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/doesNotExist");
    Mockito.when(mockUserStore.getUser("doesNotExist")).thenReturn(null);

    profileServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoPut_SelfProfile() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/me");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("me");

    Mockito.when(mockUserStore.getUser("me")).thenReturn(mockUser);
    Mockito.when(mockUser.getName()).thenReturn("me");

    Mockito.when(mockRequest.getParameter("aboutMe")).thenReturn("someMessage");

    profileServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockUser).setAboutMe("someMessage");
    Mockito.verify(mockUserStore).updateUser(mockUser);

    ArgumentCaptor<User> userArgumentCaptor = 
        ArgumentCaptor.forClass(User.class);
    Mockito.verify(mockUserStore).updateUser(userArgumentCaptor.capture());
    Assert.assertEquals("me", userArgumentCaptor.getValue().getName());
    Assert.assertEquals(null, userArgumentCaptor.getValue().getAboutMe());

    ArgumentCaptor<Event> eventArgumentCaptor = 
        ArgumentCaptor.forClass(Event.class);
    Mockito.verify(mockEventStore).addEvent(eventArgumentCaptor.capture());
    Assert.assertEquals("About Me", eventArgumentCaptor.getValue().getType());
    List<String> testInformation = new ArrayList<>();
    testInformation.add("me");
    testInformation.add(null);
    Assert.assertEquals(testInformation, eventArgumentCaptor.getValue().getInformation());

    Mockito.verify(mockResponse).sendRedirect("/profile/me");
  }

  @Test
  public void testDoPut_OtherUserProfile() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/notMe");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("me");

    Mockito.when(mockUserStore.getUser("notMe")).thenReturn(mockUser);
    Mockito.when(mockUser.getName()).thenReturn("notMe");

    profileServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }
}
