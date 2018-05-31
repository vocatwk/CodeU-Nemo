package codeu.controller;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AdminServletTest {

  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private AdminServlet AdminServlet;

    @Before
    public void setup() {
      AdminServlet = new AdminServlet();

      mockRequest = Mockito.mock(HttpServletRequest.class);
      mockSession = Mockito.mock(HttpSession.class);
      Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

      mockResponse = Mockito.mock(HttpServletResponse.class);
      mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
      Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
          .thenReturn(mockRequestDispatcher);

      mockConversationStore = Mockito.mock(ConversationStore.class);
      AdminServlet.setConversationStore(mockConversationStore);

      mockUserStore = Mockito.mock(UserStore.class);
      AdminServlet.setUserStore(mockUserStore);

      mockMessageStore = Mockito.mock(MessageStore.class);
      AdminServlet.setMessageStore(mockMessageStore);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
      List<User> fakeUserList = new ArrayList<>();

      User fakeUser = new User(UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
      fakeUser.setAdmin(true);
      fakeUserList.add(fakeUser);

      Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

      UUID fakeConversationId = UUID.randomUUID();
      List<Conversation> fakeConversationList = new ArrayList<>();
      fakeConversationList.add(
          new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now()));
      Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

      List<Message> fakeMessageList = new ArrayList<>();
      fakeMessageList.add(
          new Message(UUID.randomUUID(),fakeConversationId, UUID.randomUUID(),"test_message",Instant.now()));
      Mockito.when(mockMessageStore.getMessages()).thenReturn(fakeMessageList);


      AdminServlet.doGet(mockRequest, mockResponse);
      Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

      //Mockito.when(mockUserStore.getUser("test_username").getAdmin()).thenReturn(true);
      /*int fakenumOfUsers = mockUserStore.getAllUsers().size();
      int fakenumOfConvos = mockConversationStore.getAllConversations().size();
      int fakenumOfMessages = mockMessageStore.getMessages().size();

      Mockito.when(mockSession.getAttribute("numOfUsers")).thenReturn(fakenumOfUsers);
      Mockito.when(mockSession.getAttribute("numOfMessages")).thenReturn(fakenumOfMessages);
      Mockito.when(mockSession.getAttribute("numOfConvos")).thenReturn(fakenumOfConvos);

      Mockito.verify(mockRequest).setAttribute("numOfUsers", fakenumOfUsers);
      Mockito.verify(mockRequest).setAttribute("numOfMessages", fakenumOfMessages );
      Mockito.verify(mockRequest).setAttribute("numOfConvos", fakenumOfConvos );

      Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
*/
    }
  }


/*
  @Test
  public void testDoGet() throws IOException, ServletException {


  //  Mockito.when(mockRequest.getRequestURI()).thenReturn("/admin");
  //  Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/fakeUser");

    List<User> fakeUserList = new ArrayList<>();
    User fakeUser = new User(UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
    fakeUser.setAdmin(true);
    fakeUserList.add(fakeUser);

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

    UUID fakeConversationId = UUID.randomUUID();
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now()));
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(UUID.randomUUID(),fakeConversationId, UUID.randomUUID(),"test_message",Instant.now()));
    Mockito.when(mockMessageStore.getMessages()).thenReturn(fakeMessageList);
    AdminServlet.doGet(mockRequest, mockResponse);




    //Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

  }
  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    AdminServlet.doPost(mockRequest, mockResponse);
    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

    AdminServlet.doPost(mockRequest, mockResponse);
    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoPost_isAdmin() throws IOException, ServletException {
    //Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    //Mockito.when(mockUserStore.getUser("test_username").getAdmin()).thenReturn(true);
    int fakenumOfUsers = mockUserStore.getAllUsers().size();
    int fakenumOfConvos = mockConversationStore.getAllConversations().size();
    int fakenumOfMessages = mockMessageStore.getMessages().size();

    AdminServlet.doPost(mockRequest, mockResponse);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockSession.getAttribute("numOfUsers")).thenReturn(fakenumOfUsers);
    Mockito.when(mockSession.getAttribute("numOfMessages")).thenReturn(fakenumOfMessages);
    Mockito.when(mockSession.getAttribute("numOfConvos")).thenReturn(fakenumOfConvos);

    Mockito.verify(mockRequest).setAttribute("numOfUsers", fakenumOfUsers);
    Mockito.verify(mockRequest).setAttribute("numOfMessages", fakenumOfMessages );
    Mockito.verify(mockRequest).setAttribute("numOfConvos", fakenumOfConvos );

}
}
*/