<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%
Map<Instant, Object> sortedEventsMap = 
  (Map<Instant, Object>) request.getAttribute("sortedEventsMap");
List<User> users = (List<User>) request.getAttribute("users");
List<Conversation> conversations = 
  (List<Conversation>) request.getAttribute("conversations");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="navbar.jsp" %>

  <div id="container">
    <h1>Activity
      <a href="" style="float: right">&#8635;</a></h1>

    <p>Here's everything that's happened on the site so far!</p>

    <div id="activity">
      <ul>
        <%
        for(Map.Entry<Instant, Object> entry : sortedEventsMap.entrySet()) {
          Date date = Date.from(entry.getKey());
        %>
          <li>
            <b><%= date %>:</b>
          <%
          if(entry.getValue() instanceof User) {
            User user = (User) entry.getValue();
          %>
            <a href="/profile/<%= user.getName() %>"><%= user.getName() %></a> 
              joined!
          </li>
          <%
          }
          else if (entry.getValue() instanceof Conversation) {
            Conversation conversation = (Conversation) entry.getValue();
            User conversationOwner = null;
            for(User user : users) {
              if (user.getId().equals(conversation.getOwnerId())) {
                conversationOwner = user;
                break;
              } 
            }
          %>
            <a href="/profile/<%= conversationOwner.getName() %>">
              <%= conversationOwner.getName() %></a> 
              created a new conversation: 
            <a href="/chat/<%= conversation.getTitle() %>">
              <%= conversation.getTitle() %></a>
          </li>
          <%
          }
          else if (entry.getValue() instanceof Message) {
            Message message = (Message) entry.getValue();
            User messageAuthor = null;
            Conversation messageConversation = null;
            for(User user : users) {
              if (user.getId().equals(message.getAuthorId())) {
                messageAuthor = user;
                break;
              }
            }
            for(Conversation conversation : conversations) {
              if (conversation.getId().equals(message.getConversationId())) {
                messageConversation = conversation;
                break;
              }
            }
          %>
            <a href="/profile/<%= messageAuthor.getName() %>">
              <%= messageAuthor.getName() %></a>
            sent a message in
            <a href="/chat/<%= messageConversation.getTitle() %>">
              <%=messageConversation.getTitle()%></a>: 
              "<%= message.getContent() %>"
          </li>
          <%
          }
        }
        %>
      </ul>
    </div>

  </div>
  
</body>
</html>