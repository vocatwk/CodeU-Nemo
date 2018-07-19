// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.controller.BotController;
import codeu.model.data.Bot;
import codeu.model.data.NemoBot;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Event;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashSet;
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

public class ChatServletTest {

  private ChatServlet chatServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private BotController mockBotController;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private Conversation mockConversation;
  private EventStore mockEventStore;
  private BufferedReader mockReader;
  private PrintWriter mockWriter;

  @Before
  public void setup() throws IOException {
    chatServlet = new ChatServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/chat.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockBotController = Mockito.mock(BotController.class);
    chatServlet.setBotController(mockBotController);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    chatServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    chatServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    chatServlet.setUserStore(mockUserStore);

    mockConversation = Mockito.mock(Conversation.class);
    mockEventStore = Mockito.mock(EventStore.class);
    chatServlet.setEventStore(mockEventStore);

    mockReader = Mockito.mock(BufferedReader.class);
    Mockito.when(mockRequest.getReader()).thenReturn(mockReader);

    mockWriter = Mockito.mock(PrintWriter.class);
    Mockito.when(mockResponse.getWriter()).thenReturn(mockWriter);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_user1");
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    
    fakeConversation.addMember("test_user1");
    fakeConversation.addMember("test_user2");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            fakeConversationId,
            UUID.randomUUID(),
            "test message",
            Instant.now()));
    Mockito.when(mockMessageStore.getMessagesInConversation(fakeConversationId))
        .thenReturn(fakeMessageList);

    chatServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("conversation", fakeConversation);
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
    Mockito.verify(mockRequest).setAttribute("isPrivate",false);
    Mockito.verify(mockRequest)
        .setAttribute("membersOfConversation","[\"test_user2\",\"test_user1\"]");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_PurposeIsToGetMembers() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_user1");
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    
    fakeConversation.addMember("test_user1");
    fakeConversation.addMember("test_user2");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    Mockito.when(mockRequest.getHeader("purpose")).thenReturn("Get members");

    chatServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).setContentType("application/json");
    Mockito.verify(mockResponse).setCharacterEncoding("UTF-8");
    Mockito.verify(mockWriter).write("[\"test_user2\",\"test_user1\"]");
    Mockito.verify(mockRequestDispatcher, Mockito.never()).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_badConversation() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(null);

    chatServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoGet_badUrl() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/does_not_resemble_UUID");

    chatServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }


  
  @Test
  public void testDoPost_ConversationNotFound() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(null);

    chatServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockMessageStore, Mockito.never()).addMessage(Mockito.any(Message.class));
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoGet_UserHasNoAccess() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockSession.getAttribute("user")).thenReturn("random_user");
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    
    fakeConversation.addMember("test_user1");
    fakeConversation.addMember("test_user2");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    chatServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Mockito.verify(mockWriter).write("You don't have access to this page");
    Mockito.verify(mockRequest, Mockito.never()).getHeader("purpose");
    Mockito.verify(mockConversationStore, Mockito.never()).updateConversation(Mockito.any(Conversation.class));
  }

  @Test
  public void testDoPost_MessageIsNull() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    fakeConversation.addMember("test_username");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    Mockito.when(mockRequest.getParameter("message")).thenReturn(null);

    chatServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockMessageStore, Mockito.never()).addMessage(Mockito.any(Message.class));
    Mockito.verify(mockEventStore, Mockito.never()).addEvent(Mockito.any(Event.class));
    Mockito.verify(mockResponse).sendRedirect("/chat/" + fakeConversationId);
  }

  @Test
  public void testDoPost_StoresMessage_NoBot() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    fakeConversation.addMember("test_username");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    Mockito.when(mockRequest.getParameter("message")).thenReturn("Test message.");

    chatServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    Mockito.verify(mockMessageStore).addMessage(messageArgumentCaptor.capture());
    Assert.assertEquals("Test message.", messageArgumentCaptor.getValue().getContent());

    ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
    Mockito.verify(mockEventStore).addEvent(eventArgumentCaptor.capture());
    Assert.assertEquals("Message", eventArgumentCaptor.getValue().getType());
    List<String> testInformation = new ArrayList<>();
    testInformation.add("test_username");
    testInformation.add("test_conversation");
    testInformation.add("Test message.");
    testInformation.add(fakeConversationId.toString());
    Assert.assertEquals(testInformation, eventArgumentCaptor.getValue().getInformation());

    // This simulates the private method getMentionKey() to some extent.
    Mockito.when(mockBotController.getBot("@NoBot")).thenReturn(null);

    Mockito.verify(mockResponse).sendRedirect("/chat/" + fakeConversationId);
  }

  @Test
  public void testDoPost_CleansHtmlContent_NoBot() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$eDhncK/4cNH2KE.Y51AWpeL8/5znNBQLuAFlyJpSYNODR/SJQ/Fg6",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    fakeConversation.addMember("test_username");
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    Mockito.when(mockRequest.getParameter("message"))
        .thenReturn("Contains <b>html</b> and <script>JavaScript</script> content.");

    chatServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    Mockito.verify(mockMessageStore).addMessage(messageArgumentCaptor.capture());
    Assert.assertEquals(
        "Contains html and  content.", messageArgumentCaptor.getValue().getContent());

    ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
    Mockito.verify(mockEventStore).addEvent(eventArgumentCaptor.capture());
    Assert.assertEquals("Message", eventArgumentCaptor.getValue().getType());
    List<String> testInformation = new ArrayList<>();
    testInformation.add("test_username");
    testInformation.add("test_conversation");
    testInformation.add("Contains html and  content.");
    testInformation.add(fakeConversationId.toString());
    Assert.assertEquals(testInformation, eventArgumentCaptor.getValue().getInformation());

    // This simulates the private method getMentionKey() to some extent.
    Mockito.when(mockBotController.getBot("@NoBot")).thenReturn(null);

    Mockito.verify(mockResponse).sendRedirect("/chat/" + fakeConversationId);
  }

  @Test
  public void testDoPost_StoresMessage_WithBot() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    fakeConversation.addMember("test_username");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    Mockito.when(mockRequest.getParameter("message")).thenReturn("Test message @NemoBot.");

    chatServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    Mockito.verify(mockMessageStore).addMessage(messageArgumentCaptor.capture());
    Assert.assertEquals("Test message @NemoBot.", messageArgumentCaptor.getValue().getContent());

    ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
    Mockito.verify(mockEventStore).addEvent(eventArgumentCaptor.capture());
    Assert.assertEquals("Message", eventArgumentCaptor.getValue().getType());
    List<String> testInformation = new ArrayList<>();
    testInformation.add("test_username");
    testInformation.add("test_conversation");
    testInformation.add("Test message @NemoBot.");
    // Assert.assertEquals(testInformation, eventArgumentCaptor.getValue().getInformation());

    NemoBot nemoBot = Mockito.mock(NemoBot.class);
    Mockito.when(mockBotController.getBot("@NemoBot")).thenReturn(nemoBot);

    Mockito.verify(mockResponse).sendRedirect("/chat/" + fakeConversationId);
  }

  @Test
  public void testDoPost_UserHasNoAccess() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    fakeConversation.addMember("test_username2");

    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(fakeConversation);

    chatServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Mockito.verify(mockWriter).write("You don't have access to this page");
    Mockito.verify(mockRequest, Mockito.never()).getParameter("message");
    Mockito.verify(mockResponse, Mockito.never()).sendRedirect("/chat/" + fakeConversationId);
  }

  @Test
  public void testDoPut_makePublicPrivate() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(mockConversation);
    Mockito.when(mockConversation.containsMember("test_username")).thenReturn(true);

    Mockito.when(mockRequest.getHeader("purpose")).thenReturn("Changing chat privacy");

    Mockito.when(mockReader.readLine())
        .thenReturn("make private");
    Mockito.when(mockConversation.isPrivate()).thenReturn(false);
    
    chatServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockConversation).makePrivate();
    Mockito.verify(mockConversationStore).updateConversation(mockConversation);
  }

  @Test
  public void testDoPut_makePrivatePublic() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(mockConversation);
    Mockito.when(mockConversation.containsMember("test_username")).thenReturn(true);

    Mockito.when(mockRequest.getHeader("purpose")).thenReturn("Changing chat privacy");

    Mockito.when(mockReader.readLine())
        .thenReturn("make public");
    Mockito.when(mockConversation.isPrivate()).thenReturn(true);
    
    chatServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockConversation).makePublic();
    Mockito.verify(mockConversationStore).updateConversation(mockConversation);
  }

  @Test
  public void testDoPut_SettingUsers() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(mockConversation);
    Mockito.when(mockConversation.containsMember("test_username")).thenReturn(true);

    Mockito.when(mockRequest.getHeader("purpose")).thenReturn("Setting users");

    Mockito.when(mockReader.readLine())
        .thenReturn("[\"test_user1\",\"test_user2\"]");
    
    User fakeUser1 =
        new User(
            UUID.randomUUID(),
            "test_user1",
            "$2a$10$eDhncK/4cNH2KE.Y51AWpeL8/5znNBQLuAFlyJpSYNODR/SJQ/Fg6",
            Instant.now());

    User fakeUser2 =
        new User(
            UUID.randomUUID(),
            "test_user2",
            "$2a$10$eDhncK/4cNH2KE.Y51AWpeL8/5znNBQLuAFlyJpSYNODR/SJQ/Fg6",
            Instant.now());

    Mockito.when(mockUserStore.getUser("test_user1")).thenReturn(fakeUser1);
    Mockito.when(mockUserStore.getUser("test_user2")).thenReturn(fakeUser2);

    HashSet<String> fakeUsersToBeAdded = new HashSet<>();
    fakeUsersToBeAdded.add("test_user1");
    fakeUsersToBeAdded.add("test_user2");
    
    Mockito.when(mockConversation.getMembers()).thenReturn(fakeUsersToBeAdded);

    chatServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockConversation).setMembers(fakeUsersToBeAdded);
    Mockito.verify(mockConversationStore).updateConversation(mockConversation);
  }

  @Test
  public void testDoPut_UserHasNoAccess() throws IOException, ServletException {
    UUID fakeConversationId = UUID.randomUUID();
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/chat/" + fakeConversationId);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    
    Mockito.when(mockConversationStore.getConversation(fakeConversationId))
        .thenReturn(mockConversation);
    Mockito.when(mockConversation.containsMember("test_username")).thenReturn(false);

    chatServlet.doPut(mockRequest, mockResponse);

    Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Mockito.verify(mockWriter).write("You don't have access to this page");
    Mockito.verify(mockRequest, Mockito.never()).getParameter("message");
    Mockito.verify(mockConversation, Mockito.never()).setMembers(Mockito.any(HashSet.class));
    Mockito.verify(mockConversationStore, Mockito.never()).updateConversation(Mockito.any(Conversation.class));
  }
}
