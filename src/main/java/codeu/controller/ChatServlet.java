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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Event;
import codeu.model.data.Notification;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import codeu.model.store.basic.NotificationStore;
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

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Events. */
  private EventStore eventStore;

  private NotificationStore notificationStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setEventStore(EventStore.getInstance());
    setNotificationStore(NotificationStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
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

  void setNotificationStore(NotificationStore notificationStore) {
    this.notificationStore = notificationStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.setAttribute("isPrivate", conversation.isPrivate());
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }

    String type = request.getParameter("type");
    String messageContent = request.getParameter("message");

    if(type != null){
      if(type.equals("make private")){
        conversation.makePrivate();
      }
      else{
        conversation.makePublic();
      }
      conversationStore.updateConversation(conversation);
    }
    else if(messageContent != null) {

      // this removes any HTML from the message content
      String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

      Message message =
          new Message(
              UUID.randomUUID(),
              conversation.getId(),
              user.getId(),
              cleanedMessageContent,
              Instant.now());

      messageStore.addMessage(message);

      List<String> messageInformation = new ArrayList<>();
      messageInformation.add(user.getName());
      messageInformation.add(conversationTitle);
      messageInformation.add(cleanedMessageContent);
      Event messageEvent = new Event(UUID.randomUUID(), "Message", message.getCreationTime(), messageInformation);
      eventStore.addEvent(messageEvent);
      Notification messageNotification = new Notification (
        UUID.randomUUID(), conversation.getOwnerId(), message.getAuthorId(), messageEvent);
    }

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }
}
