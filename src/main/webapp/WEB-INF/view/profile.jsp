<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>

<%
List<Message> messages = (List<Message>) request.getAttribute("messages");
String subject = (String) request.getAttribute("subject");
String user = (String) request.getSession().getAttribute("user");
String aboutMe = (String) request.getAttribute("aboutMe");
String requestUrl = (String) request.getRequestURI();
String UrlStripped = (String) requestUrl.substring();
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= subject %></title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">

  <script>
    // scroll the messages div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <div id="container">

    <h1> <%= subject %>'s profile page </h1>
    <hr/>

    <h2> About <%= subject %> </h2>
    <% if (!aboutMe.equals("")) { %>
      <p> <%= aboutMe %> </p>
    <% } else { %>
      <p> <%= subject %> has not provided any information.</p>
    <% } %>

    <% if ((user != null) && (user.equals(subject))) { %>
      <h2> Edit your About Me (only you can see this) </h2>
      <form action="/profile/<%= subject %>" method="POST">
        <input type="text" name="aboutMe">
        <br/>
        <button type="submit">Submit</button>
      </form>
    <% } %>
    <hr/>

    <h2> <%= subject %>'s sent messages <a href="" style="float: right">&#8635;</a></h2>

    <div id="messages">

      <ul>
        <% for (Message message : messages) { %>
          <li> <strong> <%= Date.from(message.getCreationTime()) %>:
               </strong> <%= message.getContent() %> </li>
        <% } %>
      </ul>

    </div>
    <h1> <%= subject %>'s subscriptions </h1>
    <% List<UUID> subscriptionsID = request.getAttribute("subscriptionsID"); %>
    <% List<String> conversationNames = request.getAttribute("conversationNames"); %>
    <% for (int i = 0; i< subscriptionsID.size(); i++) { %>
    <a class="btn btn-primary" href="/chat/<%=subscriptionsID.get(i)%>" role="button"> <%=conversationNames.get(i)%> </a>
    <% } %>
  </div>

</body>
</html>
