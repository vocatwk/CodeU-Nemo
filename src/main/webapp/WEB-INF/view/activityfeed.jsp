<%@ page import="codeu.model.data.Event" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%
List<Event> events = (List<Event>) request.getAttribute("events");
ConversationStore conversationStore = ConversationStore.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <div id="activityContainer">
    <h3 id="activityHeader">Activity Feed
      <a href="" style="float: right">&#8635;</a></h3>

    <p>Here's everything that's happened on the site so far!</p>

    <div id="activity">
      <ul class="list-group">
      <%
        for (int i = events.size() - 1; i >= 0; i--) {
          Event event = events.get(i);
          Date date = Date.from(event.getCreationTime());
          List<String> information = event.getInformation();
          String userName = information.get(0);

          if (event.getType().equals("User")) {
          %>
            <li class="list-group-item">
            <b><%= date %></b>
            <a href="/profile/<%= userName %>"><%= userName %></a> joined!
          </li>
          <%
          }
          else if (event.getType().equals("About Me")) {
            String aboutMe = information.get(1);
          %>
            <li class="list-group-item">
            <b><%= date %></b>
            <a href="/profile/<%= userName %>"><%= userName%></a> updated their About Me:
            "<%= aboutMe %>"
          <%
          }
          else {
            String conversationTitle = information.get(1);
            String conversationId = information.get(information.size() - 1);
            boolean isPrivate =
                conversationStore.getConversation(UUID.fromString(conversationId)).isPrivate();
            boolean inConversation =
                conversationStore.getConversation(UUID.fromString(conversationId))
                    .containsMember(navBarUsername);
              if (event.getType().equals("Conversation") && information.size() > 2) {
                if (!isPrivate && inConversation) {
              %>
                  <li class="list-group-item">
                    <b><%= date %></b>
                    <a href="/profile/<%= userName %>"><%= userName %></a> created a new conversation: 
                    <a href="/chat/<%= conversationId %>"><%= conversationTitle %></a>
                  </li>
              <%
                }
              }
              else if (event.getType().equals("Message") && information.size() > 3) {
                String messageContent = information.get(2);
                if (!isPrivate && inConversation) {
              %>
                  <li class="list-group-item">
                    <b><%= date %></b>
                    <a href="/profile/<%= userName %>"><%= userName %></a> sent a message in 
                    <a href="/chat/<%= conversationId %>"><%= conversationTitle %></a>: "<%= messageContent %>"
                  </li>
              <%
                }
              }
            }
          }
        %>
      </ul> 
    </div>

  </div>
  
</body>
</html>