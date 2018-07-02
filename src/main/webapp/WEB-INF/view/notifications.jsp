<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>

<%String username = (String) request.getSession().getAttribute("user");%>

<!DOCTYPE html>
<html>
<head>
  <title>Notification</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%@ include file="navbar.jsp" %>
  <% List<Event> notifications = (List<Event>) request.getAttribute("eventsToShow"); %>
  <div id="notifications">
    <h2> Hello <%= username %>! This is your notifcation page </h2>

    <ul>
      <% for (Event notification : notifications) { %>
      <% List<String> notificationInfo = notification.getInformation(); %>
      <% String type = notification.getType(); %>
        <li> <strong> <%= Date.from(notification.getCreationTime()) %>: </strong>
            <%= type %>
            <%= notificationInfo %>

        </li>
      <% } %>
    </ul>
  </div>

</body>
</html>
