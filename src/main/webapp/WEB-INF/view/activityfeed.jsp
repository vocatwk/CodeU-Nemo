<%@ page import="codeu.model.data.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%
List<Event> events = (List<Event>) request.getAttribute("events");
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
        for (int i = events.size() - 1; i >= 0; i--) {
          Event event = events.get(i);
          Date date = Date.from(event.getCreationTime());
          List<String> information = event.getInformation();
          String userName = information.get(0);
        %>
          <li>
            <b><%= date %></b>
          <%
          if (event.getType().equals("User")) {
          %>
            <a href="/profile/<%= userName %>"><%= userName %></a> joined!
          </li>
          <%
          }
          else if (event.getType().equals("About Me")) {
            String aboutMe = information.get(1);
          %>
            <a href="/profile/<%= userName %>"><%= userName%></a> updated their About Me:
            "<%= aboutMe %>"
          <%
          }
          else {
            String conversationTitle = information.get(1);
              if (event.getType().equals("Conversation")) {
              %>
                <a href="/profile/<%= userName %>"><%= userName %></a> created a new conversation: 
                <a href="/chat/<%= conversationTitle %>"><%= conversationTitle %></a>
          </li>
              <%
              }
              else if (event.getType().equals("Message")) {
                String messageContent = information.get(2);
              %>
                <a href="/profile/<%= userName %>"><%= userName %></a> sent a message in 
                <a href="/chat/<%= conversationTitle %>"><%= conversationTitle %></a>: "<%= messageContent %>"
          </li>
              <%
              }
            }
          }
          %>
      </ul> 
    </div>
    
  </div>
  
</body>
</html>