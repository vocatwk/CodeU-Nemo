<%@ page import="java.util.List" %>
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

</head>
<body>

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
  
</body>
</html>
