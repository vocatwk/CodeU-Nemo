<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin Login</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%@ include file="navbar.jsp" %>
  <%-- getting statistics  --%>
	<br/>
	<br/>
  <div id="container">
    <h1>Admin Page</h1>
		<p> Here are some statistics of Nemo. </p>
		<p> Current number of Users: <%= request.getAttribute("numOfUsers") %></p>
		<p> Current number of Messages: <%= request.getAttribute("numOfMessages") %></p>
		<p> Current number of Conversations: <%= request.getAttribute("numOfConvos") %></p>
<<<<<<< HEAD
    <p> Most active user: <%= request.getAttribute("mostActiveUser") %></p>
=======
    <p> Most active user: <%= request.getAttribute("mostActiveUser") %></p>  
>>>>>>> 7651b41ea8e53fc65a1eebafb410d4e3a21acdbc
    <p> Newest User: <%= request.getAttribute("newestUser") %> </p>
    <p> Number of Admins: <%= request.getAttribute("numOfAdmins")%> </p>
    <p> User creation time: <%= request.getAttribute("timeCreated") %> </p>
  </div>
</body>
</html>
