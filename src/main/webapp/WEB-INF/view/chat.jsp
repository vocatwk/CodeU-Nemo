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
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>
  
  <script>

    var ToBeAddedToConversation = new Array();
    // for make private/make public button
    var newChatPrivacyValue = "<%= privacySettingButtonValue %>";
    $(document).ready(function() {
      $("#privacySettingButton").click(function() {
        fetch('/chat/<%= conversation.getTitle() %>', {
          method: "PUT",
          headers: {
            "purpose" : "Changing chat privacy"
          },
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

    // select user to add
    $(document).on('click', '.add-user-button', function(e) {             
      e.preventDefault();
      if(!this.classList.contains('isDisabled')){

        this.removeAttribute("href");
        this.classList.add('isDisabled');
        var selectedUserList = document.getElementById('selectedUserList');
        var selectedUser = document.createElement("button");
        selectedUser.setAttribute("class", "btn btn-primary");
        selectedUser.classList.add('selectedUser');
        selectedUser.innerHTML = this.getAttribute("username");

        selectedUserList.appendChild(selectedUser);
        ToBeAddedToConversation.push(this.getAttribute("username"));
        $('#addSelectedUsersButton').removeAttr('disabled');
      }
    });

    // add selected users
    $(document).on('click', '#addSelectedUsersButton', function() {             
     
      $('#selectedUserList').empty();
      $('#userResult').empty();
      $('#userSearchBar').val("");
      $('#addUsersModal').modal('hide');
      this.setAttribute('disabled', true);

      fetch('/chat/<%= conversation.getTitle() %>', {
          method: "PUT",
          headers: {
            "Content-Type": "application/json; charset=utf-8",
            "purpose" : "Adding users"
          },
          body: JSON.stringify(ToBeAddedToConversation),
          credentials: "same-origin"
        }).then(function(response) {
          if(!response.ok) {
            console.log("An error occured. Status code: " + response.status);
            return;
          }
          ToBeAddedToConversation = new Array();
        }, function(error) {
          console.log("An error occured. " + error.message);
        })
    });

    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };

  </script>
</head>
<body onload="scrollChat()">

  <div id="container">

    <div class="headerContainer">

      <div class="titleAndSettings">
        <!-- Conversation title -->
        <h1> <%= conversation.getTitle() %> </h1>
          
        <!-- Setting button and content -->
        <div class="dropdown">
          <button class="btn btn-secondary dropdown-toggle" type="button" id="settingsDropdown"
                  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 
            <i class="fa fa-cog"> </i>
          </button>
          <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <button id = "privacySettingButton" class="dropdown-item" type="button"><%= privacySettingButtonValue %></button>
            <button class="dropdown-item btn btn-primary" type="button" data-toggle="modal" data-target="#addUsersModal">Add Users</button>
          </div>
        </div>
      </div>

      <%@ include file="addUserBox.jsp" %>

      <h1> <a href="" >&#8635;</a> </h1>
    </div>
    
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
