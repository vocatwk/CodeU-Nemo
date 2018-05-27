package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityFeedServletTest {

  private ActivityFeedServlet activityFeedServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;

  @Before
  public void setUp() {
    activityFeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    activityFeedServlet.setUserStore(mockUserStore);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    activityFeedServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    activityFeedServlet.setMessageStore(mockMessageStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    UUID fakeUserId = UUID.randomUUID();
    User fakeUser = 
        new User(fakeUserId, "test_user", "test_password_hash", Instant.now());
    List<User> fakeUserList = new ArrayList<>();
    fakeUserList.add(fakeUser);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

    UUID fakeConversationId = UUID.randomUUID();
    Conversation fakeConversation = 
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(fakeConversation);
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
          new Message(
            UUID.randomUUID(), 
            fakeConversationId,
            fakeUserId,
            "test_message",
            Instant.now()));
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("users", fakeUserList);
    Mockito.verify(mockRequest).setAttribute("conversations", fakeConversationList);
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}