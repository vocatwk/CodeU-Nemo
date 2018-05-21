<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="codeu.model.data.Message" %>
<%
List<Message> messages = (List<Message>) request.getAttribute("messages");
String subject = (String) request.getAttribute("subject");
String user = (String) request.getSession().getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= subject %></title>
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

  <nav>
    <a id="navTitle" href="/">CodeU Chat App - Nemo</a>
    <a href="/conversations">Conversations</a>
    <% if(user != null){ %>
	<a href="/profile/<%= user %>">
	   Hello <%= user %>!</a>
    <% } else{ %>
	<a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <% if ((user != null) && (user.equals(subject))) { %>
      <h1> This is your profile page </h1>
  <% } else { %>
      <h1> This is <%= subject %>'s profile page </h1>
  <% } %>

  <div id="container">

   <h1> <%= subject %>'s messages <a href="" style="float: right">&#8635;</a></h1>

   <div id="messages">

     <ul>
       <% for (Message message : messages) { %>
       <li> <strong> <%= Date.from(message.getCreationTime()) %>: 
            </strong> <%= message.getContent() %> </li>
       <% } %>
     </ul>

   </div>

  </div>

</body>
</html>
