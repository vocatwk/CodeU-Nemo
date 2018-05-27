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
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/profile/<%=request.getSession().getAttribute("user") %>">
         Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>
    <%-- logging in before accessing the page --%>
	<form action="/admin" method="POST">
    <label for="username">Username: </label>
    <br/>
    <input type="text" name="username" id="username">
    <br/>
			<br/><br/>
			<button type="submit">Login</button>
		</form>
  <%-- getting statistics --%>
			<br/>
		<br/>
  <div id="container">
    <h1>Admin Page</h1>
		<p> Here are some statistics of Nemo. </p>
		<p> Current number of Users: <%= request.getAttribute("Users") %></p>
		<p> Current number of Messages: <%= request.getAttribute("Messages") %></p>
		<p> Current number of Conversations: <%= request.getAttribute("Conversations") %></p>

  </div>
</body>
</html>
