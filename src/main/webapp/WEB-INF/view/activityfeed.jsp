<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%
Map<Instant, Object> sortedEventsMap = 
  (Map<Instant, Object>) request.getAttribute("sortedEventsMap");
List<User> users = (List<User>) request.getAttribute("users");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App - Nemo</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/profile/<%=request.getSession().getAttribute("user") %>">
         Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <h1>Activity</h1>

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
            for (User user : users) {
              if (user.getId().equals(conversation.getOwnerId()))
                conversationOwner = user;
                break; 
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
          %>
        <%
        }
        %>
      </ul>
    </div>

  </div>
  
</body>
</html>