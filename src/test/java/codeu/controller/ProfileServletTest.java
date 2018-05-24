package codeu.controller;

import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockMessageStore = Mockito.mock(MessageStore.class);
    profileServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);
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

    User mockUser = Mockito.mock(User.class);
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
}
