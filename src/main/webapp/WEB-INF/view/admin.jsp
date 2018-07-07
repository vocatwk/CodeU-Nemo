<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin Login</title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%-- getting statistics  --%>
	<br/>
	<br/>
  <div id="container">
    <h1>Admin Page</h1>
		<p> Here are some statistics of Nemo. </p>
		<p> Current number of Users: <%= request.getAttribute("numOfUsers") %></p>
		<p> Current number of Messages: <%= request.getAttribute("numOfMessages") %></p>
		<p> Current number of Conversations: <%= request.getAttribute("numOfConvos") %></p>
    <p> Most active user: <%= request.getAttribute("mostActiveUser") %></p>  
    <p> Newest User: <%= request.getAttribute("newestUser") %> </p>
    <p> Number of Admins: <%= request.getAttribute("numOfAdmins")%> </p>
    <p> User creation time: <%= request.getAttribute("timeCreated") %> </p>
  </div>
</body>
</html>
