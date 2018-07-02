
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
      UUID fakeUserId = UUID.randomUUID();
      User fakeUser = new User(fakeUserId, "test_username", "fakePasswordHash", Instant.now());
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
      Mockito.when(mockUserStore.getNumOfUsers()).thenReturn(fakeUserList.size());
      Mockito.when(mockConversationStore.getNumOfConversations()).thenReturn(fakeConversationList.size());
      Mockito.when(mockMessageStore.getNumOfMessages()).thenReturn(fakeMessageList.size());



      Mockito.when(mockMessageStore.getMessagesFromAuthor(fakeUserId)).thenReturn(fakeMessageList);
      Mockito.when(mockUserStore.getUser(fakeUserId)).thenReturn(mockUser);
      Mockito.when(mockUser.getName()).thenReturn("test_username");

      String fakeNewestUser = "test_username";
      String fakeMostActiveUser = "test_username";

      AdminServlet.doGet(mockRequest, mockResponse);

      Mockito.verify(mockRequest).setAttribute("numOfUsers", fakeUserList.size());
      Mockito.verify(mockRequest).setAttribute("numOfConvos", fakeMessageList.size());
      Mockito.verify(mockRequest).setAttribute("numOfMessages", fakeConversationList.size());
      Mockito.verify(mockRequest).setAttribute("numOfAdmins", 4);
      Mockito.verify(mockRequest).setAttribute("newestUser", fakeNewestUser);
      Mockito.verify(mockRequest).setAttribute("mostActiveUser", fakeMostActiveUser);
      Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);


    }
  
  @Test
  public void testDoGet_isAdminFalse() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(mockUser);
    AdminServlet.doGet(mockRequest, mockResponse);
    Mockito.when(mockUser.getIsAdmin()).thenReturn(false);
    Mockito.verify(mockResponse).sendRedirect("/");
  }
}
