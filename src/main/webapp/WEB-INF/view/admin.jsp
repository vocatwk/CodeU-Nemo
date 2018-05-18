
<!DOCTYPE html>
<html>
	<head>
		<title>Admin Page</title>
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
    
		<h1>Admin Page</h1>
		This is the protype of the admin page

	</body>
</html>
