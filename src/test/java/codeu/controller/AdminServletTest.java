
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
import org.junit.Assert;

public class AdminServletTest {

  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private AdminServlet AdminServlet;
  private User mockUser;
  private Conversation mockConversation;

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

      mockUserStore = Mockito.mock(UserStore.class);
      AdminServlet.setUserStore(mockUserStore);

      mockConversationStore = Mockito.mock(ConversationStore.class);
      AdminServlet.setConversationStore(mockConversationStore);

      mockMessageStore = Mockito.mock(MessageStore.class);
      AdminServlet.setMessageStore(mockMessageStore);

      mockUser = Mockito.mock(User.class);
      mockConversation = Mockito.mock(Conversation.class);
    }

  @Test
  public void testDoGet() throws IOException, ServletException {

      List<User> fakeUserList = new ArrayList<>();
      User fakeUser = new User( UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
      UUID fakeUserId = UUID.randomUUID();
      fakeUserList.add(fakeUser);

      Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

      List<Conversation> fakeConversationList = new ArrayList<>();
      fakeConversationList.add(
          new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now()));
        Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

      List<Message> fakeMessageList = new ArrayList<>();
      fakeMessageList.add(
          new Message(UUID.randomUUID(),UUID.randomUUID(), UUID.randomUUID(),"test_message",Instant.now()));
        Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);


      Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
      Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);
      Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

    }


  @Test
  public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    AdminServlet.doGet(mockRequest, mockResponse);
    Mockito.verify(mockResponse).sendRedirect("/");
}

  @Test
  public void testDoGet_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

    AdminServlet.doGet(mockRequest, mockResponse);
    Mockito.verify(mockResponse).sendRedirect("/");
  }
  @Test
  public void testDoGet_isAdminFalse() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);
    AdminServlet.doGet(mockRequest, mockResponse);
    Mockito.when(mockUser.getIsAdmin()).thenReturn(false);
    Mockito.verify(mockResponse).sendRedirect("/");
  }

  public void testDoGet_getStats() throws IOException, ServletException{
    List<String> fakeAdminList = new ArrayList<>();
    fakeAdminList.add("test_username");

    List<User> fakeUserList = new ArrayList<>();
    User fakeUser = new User( UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
    UUID fakeUserId = UUID.randomUUID();
    fakeUserList.add(fakeUser);

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now()));
      Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(UUID.randomUUID(),UUID.randomUUID(), UUID.randomUUID(),"test_message",Instant.now()));
      Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);


    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);
    Mockito.when(mockUser.getIsAdmin()).thenReturn(true);

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.when(mockUserStore.getNumOfUsers()).thenReturn(fakeUserList.size());
    Mockito.when(mockConversationStore.getNumOfConversations()).thenReturn(fakeConversationList.size());
    Mockito.when(mockMessageStore.getNumOfMessages()).thenReturn(fakeMessageList.size());
    Mockito.verify(mockUserStore).getNumOfUsers();
    Mockito.verify(mockConversationStore).getNumOfConversations();
    Mockito.verify(mockMessageStore).getNumOfMessages();

    Mockito.verify(mockRequest).setAttribute("numOfUsers", fakeUserList.size());
    Mockito.verify(mockRequest).setAttribute("numOfConvos", fakeMessageList.size());
    Mockito.verify(mockRequest).setAttribute("numOfMessages", fakeConversationList.size());
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);
    Mockito.when(mockUser.getIsAdmin()).thenReturn(true);
    Mockito.when(mockUserStore.getNumOfUsers()).thenReturn(fakeUserList.size());

    AdminServlet.doGet(mockRequest, mockResponse);

    String fakeNewestUser = fakeUserList.get(fakeUserList.size()-1).getName();
    String fakeMostActiveUser = "";
    int fakeNumOfAdmins = 0;

    for(int i =0; i< fakeUserList.size(); i++){
      User fakeUserInfo = fakeUserList.get(i);
      UUID fakeUserId2 = fakeUserInfo.getId();
      int userMessagesListSize = mockMessageStore.getMessagesFromAuthor(fakeUserId2).size();
      int mostMessages=0;
      if (userMessagesListSize > mostMessages){
        mostMessages = mockMessageStore.getMessagesFromAuthor(fakeUserId2).size();
        fakeMostActiveUser = mockUserStore.getInstance().getUser(fakeUserId2).getName();
      }
    }


    Mockito.verify(mockRequest).setAttribute("newestUser", fakeNewestUser);
    Mockito.verify(mockRequest).setAttribute("numOfAdmins", fakeNumOfAdmins);
    Mockito.verify(mockRequest).setAttribute("mostActiveUser", fakeMostActiveUser);
  }

}
