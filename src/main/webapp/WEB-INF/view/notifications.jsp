<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%
  String username = (String) request.getSession().getAttribute("user");
  List<Event> notifications = (List<Event>) request.getAttribute("eventsToShow");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Notification</title>
  <%@ include file="header.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%@ include file="navbar.jsp" %>
  <p> Hello! This is your notifcation page</p>
  <div id="notifications">
      <ul>
    <% for (Event notification : notifications) { %>
      <%
        Date date = Date.from(notification.getCreationTime());
        List<String> notifcationInfo = notification.getInformation();
        String Username = notifcationInfo.get(0);
        String type = notification.getType();
      %>
      <%if(type.equals("Conversation")){%>
        <li> <strong> <%= Date.from(notification.getCreationTime()) %>: </strong>
            <%= Username %> opened a conversation!
            Conversation title <%= notifcationInfo.get(1)%>
        </li>
      <% }else if(type.equals("Message")){ %>
            <li> <strong> <%= Date.from(notification.getCreationTime()) %>: </strong>
              <%= Username %> sent a message!
              Check conversation: <%= notifcationInfo.get(1)%>
            </li>
        <%}%>
      <%}%>
    </ul>
  </div>
</body>
</html>
