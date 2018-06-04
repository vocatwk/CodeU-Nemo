<%@ page import="java.util.List" %>
<% List<String> adminList = (List<String>) request.getAttribute("adminList");
  String user = request.getSession().getAttribute("user");
  %>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Login</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(user != null){ %>
      <a href="/profile/<%=user%>">
         Hello <%= user %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <% if(user != null){ %>
      <% if(adminList.size()>0 || adminList.contains(user)){ %>
        <a href="/admin">Admin</a>
        <% } %>
    <% } %>
  </nav>
  <%-- getting statistics  --%>
	<br/>
	<br/>
  <div id="container">
    <h1>Admin Page</h1>
		<p> Here are some statistics of Nemo. </p>
		<p> Current number of Users: <%= request.getAttribute("numOfUsers") %></p>
		<p> Current number of Messages: <%= request.getAttribute("numOfMessages") %></p>
		<p> Current number of Conversations: <%= request.getAttribute("numOfConvos") %></p>

  </div>
</body>
</html>
