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
import codeu.model.data.NemoBot;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.EventStore;
import java.io.IOException;
import java.io.BufferedReader;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashSet;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.gson.Gson;

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

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setEventStore(EventStore.getInstance());
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

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String requestUrl = request.getRequestURI();
    String conversationIdAsString = requestUrl.substring("/chat/".length());
    UUID conversationId = getIdFromString(conversationIdAsString);

    Conversation conversation = conversationStore.getConversation(conversationId);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationIdAsString);
      response.sendRedirect("/conversations");
      return;
    }

    String purpose = request.getHeader("purpose");
    if(purpose != null && purpose.equals("Get members")){
      String json = new Gson().toJson(conversation.getMembers());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(json);
      return;
    }

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    String membersOfConversation = new Gson().toJson(conversation.getMembers());

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.setAttribute("isPrivate", conversation.isPrivate());
    request.setAttribute("membersOfConversation", membersOfConversation);
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
    String conversationIdAsString = requestUrl.substring("/chat/".length());
    UUID conversationId = getIdFromString(conversationIdAsString);

    Conversation conversation = conversationStore.getConversation(conversationId);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationIdAsString);
      response.sendRedirect("/conversations");
      return;
    }

    String messageContent = request.getParameter("message");
    if(messageContent != null) {
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

      List<String> messageInformation = new ArrayList<String>();
      messageInformation.add(user.getName());
      messageInformation.add(conversation.getTitle());
      messageInformation.add(cleanedMessageContent);
      messageInformation.add(conversationId.toString());
      Event messageEvent =
          new Event(
              UUID.randomUUID(),
              "Message",
              message.getCreationTime(),
              messageInformation);
      eventStore.addEvent(messageEvent);

      // Scan the message for "@NemoBot"
      if (containsWholeWord(cleanedMessageContent, "@NemoBot")) {
        NemoBot nemoBot = new NemoBot();
        String botResponse = nemoBot.answerMessage(cleanedMessageContent);
        Message botMessage =
            new Message(
                UUID.randomUUID(),
                conversation.getId(),
                nemoBot.getId(),
                botResponse,
                Instant.now());

        messageStore.addMessage(botMessage);

        List<String> botMessageInformation = new ArrayList<String>();
        botMessageInformation.add("NemoBot");
        botMessageInformation.add(conversation.getTitle());
        botMessageInformation.add(botResponse);
        Event botMessageEvent =
            new Event(
                UUID.randomUUID(),
                "Message",
                botMessage.getCreationTime(),
                botMessageInformation);
        eventStore.addEvent(botMessageEvent);
      }
    }

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationId);
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");

    String requestUrl = request.getRequestURI();
    String conversationIdAsString = requestUrl.substring("/chat/".length());
    UUID conversationId = getIdFromString(conversationIdAsString);

    Conversation conversation = conversationStore.getConversation(conversationId);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationIdAsString);
      response.sendRedirect("/conversations");
      return;
    }

    String purpose = request.getHeader("purpose");
    if(purpose == null){
      // wrong form of PUT request, do nothing
      return;
    }

    if(purpose.equals("Changing chat privacy")){
      String privacyCommand = request.getReader().readLine();

      if(privacyCommand.equals("make private")){
        conversation.makePrivate();
      }
      else if(privacyCommand.equals("make public")){
        conversation.makePublic();
      }
      conversationStore.updateConversation(conversation);
    }
    else if(purpose.equals("Setting users")){

      String jsonString = request.getReader().readLine();
      String cleanedJsonString = Jsoup.clean(jsonString, Whitelist.none());
      String[] userNameArray = null;

      try{
        userNameArray = new Gson().fromJson(cleanedJsonString, String[].class);
      }
      catch(Exception e){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      if(userNameArray == null || userNameArray.length == 0){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Conversation must have at least one member.");
        return;
      }

      for(String user : userNameArray){
        if(userStore.getUser(user) == null){
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          return;
        }
      }

      HashSet<String> membersList = new HashSet<>(Arrays.asList(userNameArray));
      conversation.setMembers(membersList);
      conversationStore.updateConversation(conversation);
    }

  Object answer = request.getAttribute("subbed");
  boolean subbed = ((Boolean) answer).booleanValue();
  if(subbed == true){
    User user = userStore.getUser(username);
    user.addSubscription(conversationId);
  }

  }

  /*
  * This function splits source by whitespace and checks if substring
  * is contained as a standalone word, ignore case.
  */
  private boolean containsWholeWord(String source, String substring) {
    for (String word : source.split("[[\\p{Punct}&&[^@]]\\s]+")) {
      if (word.equalsIgnoreCase(substring)) {
        return true;
      }
    }
    return false;
  }

  /*
  * This function converts from string to UUID.
  * Returns null if string is not a proper representation of UUID.
  */
  private UUID getIdFromString(String input) {
    UUID conversationId = null;

    try{
      conversationId = UUID.fromString(input);
    }
    catch(Exception e){
      return null;
    }

    return conversationId;
  }
}
