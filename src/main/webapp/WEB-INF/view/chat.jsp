<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
String user = (String) request.getSession().getAttribute("user");
String privacySettingButtonValue = (Boolean) request.getAttribute("isPrivate")? "make public":"make private";
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

  <script>

    //open add user box
    $(document).ready(function(){
      $("#addUsersModalTrigger").click(function() {
        $("#addUserBox").show();
      });
    });

    // close add user box when user clicks on x or anywhere outside of the box
    $(document).ready(function() {
      $(".close").click(function() {
        $("#addUserBox").hide();
      });
    });

    // show contents of drop down settings menu
    $(document).ready(function(){
      $("#conversationSettings").click(function() {
        $(".dropdown-content").toggle();
      });
    });

    // for make private/make public button
    var newChatPrivacyValue = "<%= privacySettingButtonValue %>";
    $(document).ready(function() {
      $("#privacySettingButton").click(function() {
        fetch('/chat/<%= conversation.getTitle() %>', {
          method: "POST",
          body : newChatPrivacyValue,
          credentials: "same-origin"
        }).then(function(response) {
          if(!response.ok) {
            console.log("An error occured. Status code: " + response.status);
            return;
          }
          newChatPrivacyValue = newChatPrivacyValue === "make private" ? "make public":"make private";
          $("#privacySettingButton").html(newChatPrivacyValue);
        }, function(error) {
          console.log("An error occured. " + error.message);
        })
      });
    });

    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };

  </script>
</head>
<body onload="scrollChat()">

  <%@ include file="navbar.jsp" %>

  <div id="container">

    <h1>
      <!-- Conversation title -->
      <%= conversation.getTitle() %>
        
      <!-- Setting button and content -->
      <div id="dropdown-settings">
        <i id="conversationSettings" class="fa fa-cog"> </i>
        <div class="dropdown-content" style="display:none">
          <li> <button id="privacySettingButton"> <%=privacySettingButtonValue%> </button> </li>
          <li> <button id="addUsersModalTrigger"> Add User </button> </li>
        </div>
      </div>

      <%@ include file="addUserBox.jsp" %>

      <!-- refresh button -->
      <a href="" style="float: right">&#8635;</a>
    </h1>

    
    <hr/>

    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
    %>
      <li><strong> <a href="/profile/<%=author %>"><%= author %></a>:
          </strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message">
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
