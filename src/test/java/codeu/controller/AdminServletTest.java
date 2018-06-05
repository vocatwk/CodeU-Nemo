
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

      mockUserStore = Mockito.mock(UserStore.class);
      AdminServlet.setUserStore(mockUserStore);

      mockConversationStore = Mockito.mock(ConversationStore.class);
      AdminServlet.setConversationStore(mockConversationStore);

      mockMessageStore = Mockito.mock(MessageStore.class);
      AdminServlet.setMessageStore(mockMessageStore);
    }

  @Test
  public void testDoGet() throws IOException, ServletException {

    List<User> fakeUserList = new ArrayList<>();
    User fakeUser = new User(UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
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
  public void testDoGet_AdminStatsPage() throws IOException, ServletException {
    List<User> fakeUserList = new ArrayList<>();
    User fakeUser = new User(UUID.randomUUID(), "test_username", "fakePasswordHash", Instant.now());
    fakeUser.setAdmin(true);
    fakeUserList.add(fakeUser);
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);
    List<String> fakeAdminList = new ArrayList<String>();

      if(fakeUser.getAdmin() ==true ){
        if(fakeAdminList.contains(fakeUser.getName())){
        fakeAdminList.add(fakeUser.getName());
        }
      }
    if(fakeAdminList.contains(fakeUser.getName())){
      int fakenumOfUsers = mockUserStore.getAllUsers().size();
      int fakenumOfConvos = mockConversationStore.getAllConversations().size();
      int fakenumOfMessages = mockMessageStore.getAllMessages().size();
      String FakeMostActiveUser = "";

      for(int i =0; i< fakenumOfUsers; i++){
        UUID fakeUserId = fakeUserList.get(i).getId();
          int fakeMostMessages=0;
        if (mockMessageStore.getMessagesFromAuthor(fakeUserId).size() > fakeMostMessages){
          fakeMostMessages = mockMessageStore.getMessagesFromAuthor(fakeUserId).size();
          FakeMostActiveUser = mockUserStore.getInstance().getUser(fakeUserId).getName();
        }
      }
      AdminServlet.doGet(mockRequest, mockResponse);

      Mockito.when(mockSession.getAttribute("numOfUsers")).thenReturn(fakenumOfUsers);
      Mockito.when(mockSession.getAttribute("numOfMessages")).thenReturn(fakenumOfMessages);
      Mockito.when(mockSession.getAttribute("numOfConvos")).thenReturn(fakenumOfConvos);
      Mockito.when(mockSession.getAttribute("mostActiveUser")).thenReturn(FakeMostActiveUser);
      Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

    }
  }

}
