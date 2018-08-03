<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%
List<Message> messages = (List<Message>) request.getAttribute("messages");
String subject = (String) request.getAttribute("subject");
String user = (String) request.getSession().getAttribute("user");
String aboutMe = (String) request.getAttribute("aboutMe");
ConversationStore conversationStore = ConversationStore.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= subject %></title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">

  <script>
    // scroll the messages div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };

    function readImage() {
  
      if (this.files && this.files[0]) {
        
        var FR = new FileReader();
        
        FR.addEventListener("load", function(e) {
          document.getElementById("image").src       = e.target.result;
          document.getElementById("imageName").innerHTML = e.target.result;
          alert("upload done");
        }); 
        
        FR.readAsDataURL( this.files[0] );
      }
  
    }

    document.getElementById("profilePicture").addEventListener("change", readImage);
  </script>
</head>
<body onload="scrollChat()">

  <div id="container">

    <h1> <%= subject %>'s profile page </h1>
    <hr/>

    <h2> About <%= subject %> </h2>
    <% if (!aboutMe.equals("")) { %>
      <p> <%= aboutMe %> </p>
    <% } else { %>
      <p> <%= subject %> has not provided any information.</p>
    <% } %>

    <% if ((user != null) && (user.equals(subject))) { %>
      <h2> Edit your About Me (only you can see this) </h2>
      <form action="/profile/<%= subject %>" method="POST">
        <input type="text" name="aboutMe">
        <br/>
        <button type="submit">Submit</button>
      </form>
    <% } %>

    <input id="profilePicture" type='file'>
    <p id="imageName"></p>
    <img id="image" height="150">

    <hr/>

    <h2> <%= subject %>'s sent messages <a href="" style="float: right">&#8635;</a></h2>

    <div id="messages">

      <ul>
        <% for (Message message : messages) { 
            if (!conversationStore.getConversation(message.getConversationId()).isPrivate()) {
        %>
              <li> <strong> <%= Date.from(message.getCreationTime()) %>:
                 </strong> <%= message.getContent() %> </li>
        <%  }
          } %>
      </ul>

    </div>

  </div>

</body>
</html>
