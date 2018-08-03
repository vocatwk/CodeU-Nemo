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
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
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
          <div class="alert alert-info alert-dismissible fade show" role="alert">
            <strong><%= Username %></strong> opened a conversation!
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
        </li>
      <% }else if(type.equals("Message")){ %>
          <li> <strong> <%= Date.from(notification.getCreationTime()) %>: </strong>
            <div class="alert alert-primary alert-dismissible fade show" role="alert">
              <strong><%= Username %></strong> sent a message!
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
          </li>
        <%}%>
      <%}%>
    </ul>
  </div>
</body>
</html>
