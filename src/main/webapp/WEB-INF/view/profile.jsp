<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
List<Message> messages = (List<Message>) request.getAttribute("messages");
String subject = (String) request.getAttribute("subject");
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
    <% if(request.getSession().getAttribute("user") != null){ %>
	<a href="profile/<%= request.getSession().getAttribute("user") %>">
	   Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
	<a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <% if ((request.getSession().getAttribute("user") != null) &&
	(request.getSession().getAttribute("user").equals(subject))) { %>
		<h1> This is your profile page </h1>
  <% } else { %>
	<h1> This is <%= subject %>'s profile page </h1>
  <% } %>
  
</body>
</html>
