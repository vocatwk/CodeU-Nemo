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
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Iterator" %>

<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
String user = (String) request.getSession().getAttribute("user");
String privacySettingButtonValue = (Boolean) request.getAttribute("isPrivate")? "make public":"make private";
String membersOfConversation = (String) request.getAttribute("membersOfConversation");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>

  <script>

    var membersOfConversation = new Set(JSON.parse('<%= membersOfConversation %>'));
    var membersAfterEditing = new Set(membersOfConversation);

    function AreSetsEqual(set1, set2){

      if(set1.size !== set2.size){
        return false;
      }
      for (let value of set1){
        if(!set2.has(value)){
          return false;
        }
      }
      return true;
    }

    $(window).ready(function(){
      updateMembersList();
    })

    function updateMembersList(){
      var it = membersOfConversation.values();

      if(membersOfConversation.size >= 3){
        $(".memberList").html("People: " + it.next().value + ", " + it.next().value + ", and " 
          + (membersOfConversation.size - 2) + " others.");
      }else if(membersOfConversation.size == 2) {
        $(".memberList").html("People: " + it.next().value + " and " + it.next().value);
      }else{
        $(".memberList").html("People: " + it.next().value);
      }
    }

    function AddUserAsTag(userName){
      var selectedUserList = document.getElementById('selectedUserList');
        var selectedUser = document.createElement("button");
        selectedUser.setAttribute("class", "btn btn-primary");
        selectedUser.classList.add('selectedUser');
        selectedUser.innerHTML = userName;

        var closeIcon = document.createElement("button");
        closeIcon.setAttribute("type", "button");
        closeIcon.setAttribute("class", "close remove-user-button");
        closeIcon.setAttribute("aria-label", "Close");
        closeIcon.setAttribute("username", userName);

        var span = document.createElement("span");
        span.setAttribute("aria-hidden", "true");
        span.innerHTML = "&times;";

        closeIcon.appendChild(span);
        closeIcon.addEventListener("click", function(){
          var userName = this.getAttribute("username");
          $(this).parent().remove();

          var addButtonForUser = $(".add-user-button[username='" + userName + "']");
          addButtonForUser.attr("href", "#");
          addButtonForUser.removeClass("isDisabled");

          membersAfterEditing.delete(userName);

          if(!AreSetsEqual(membersOfConversation, membersAfterEditing) && membersAfterEditing.size >= 1){
            $('#saveChangesButton').removeAttr('disabled');
          }else{
            $('#saveChangesButton').attr('disabled', 'true');
          }

        });

        selectedUser.appendChild(closeIcon);
        selectedUserList.appendChild(selectedUser);
    }

    //prepopulate members of conversation in setUsers modal
    $(document).ready(function(){
      $("#EditMembersButton").click(function(){

        //clean up modal
        $('#userResult').empty();
        $('#userSearchBar').val("");
        $('#selectedUserList').empty();
        $('#saveChangesButton').attr('disabled', 'true');

        // get updated list of members
        fetch('/chat/<%= conversation.getId() %>', {
          method: "GET",
          headers: {
            "purpose" : "Get members"
          },
          credentials: "same-origin"
        }).then(function(response) {
          if(response.status !== 200) {
            console.log("An error occured. Status code: " + response.status);
            return;
          }

          response.json().then(function(data){
            membersOfConversation = new Set(data);
            membersAfterEditing = new Set(membersOfConversation);
          });

        }, function(error) {
          console.log("An error occured. " + error.message);
        })

        //populate
        membersOfConversation.forEach(function(userName){
          AddUserAsTag(userName);
        });
      });
    });

    // for make private/make public button
    var newChatPrivacyValue = "<%= privacySettingButtonValue %>";
    $(document).ready(function() {
      $("#privacySettingButton").click(function() {
        fetch('/chat/<%= conversation.getId() %>', {
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

    // Add selected user
    $(document).on('click', '.add-user-button', function(e) {
      e.preventDefault();
      if(!this.classList.contains('isDisabled')){

        var userName = this.getAttribute("username");
        membersAfterEditing.add(userName);

        if(!AreSetsEqual(membersOfConversation, membersAfterEditing)){
          $('#saveChangesButton').removeAttr('disabled');
        }else{
          $('#saveChangesButton').attr('disabled', 'true');
        }

        this.removeAttribute("href");
        this.classList.add('isDisabled');
        AddUserAsTag(userName);
      }
    });

    // save changes to the list of members
    $(document).on('click', '#saveChangesButton', function() {

      $('#setUsersModal').modal('hide');

      fetch('/chat/<%= conversation.getId() %>', {
          method: "PUT",
          headers: {
            "Content-Type": "application/json; charset=utf-8",
            "purpose" : "Setting users"
          },
          body: JSON.stringify(Array.from(membersAfterEditing)),
          credentials: "same-origin"
        }).then(function(response) {
          if(response.status !== 200) {
            console.log("An error occured. Status code: " + response.status);
            return;
          }
          membersOfConversation = new Set(membersAfterEditing);
          updateMembersList();

          if(!membersOfConversation.has("<%= user %>")){
            window.location.replace("/conversations");
          }
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

  <div id="chatContainer">

    <div id="centeredChatContainer">

      <div class="headerContainer flex">

        <div class="titleAndSettings flex">

          <!-- Setting button and content -->
          <div class="dropdown">
            <h4 class="dropdown-toggle text-primary" id="settings-dropdown-trigger" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="fa fa-cog"> </i>
            </h4>
            <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
              <button id="privacySettingButton" class="dropdown-item" type="button"><%= privacySettingButtonValue %></button>
              <button id="EditMembersButton" class="dropdown-item btn btn-primary" type="button" data-toggle="modal" data-target="#setUsersModal">Edit Members</button>
            </div>
          </div>

          <!-- Conversation title -->
          <h4> <%= conversation.getTitle() %> </h4>
        </div>

        <%@ include file="addUserBox.jsp" %>

        <div class="flex text-primary"> 
          <strong class="memberList"> </strong>
          <a href="" ><h2> &#8635; </h2></a>
        </div>
      </div>

      <div id="chatBox" class="rounded">
        <div id="messagesContainer">
          <%
            for (Message message : messages) {
              String author = UserStore.getInstance().getUser(message.getAuthorId()).getName();
              if(user.equals(author)){
          %>
                <div class="messageContainer right">
                  <div class="box triangle rightBubble blue">
                    <%= message.getContent() %>
                  </div>
                  <img class="dot" src="/default-user.png">
                </div>
          <%
              }else{
          %>
                <div class="messageContainer">
                  <img class="dot" src="/default-user.png">
                  <div class="box triangle leftBubble">
                    <a class="author" href="/profile/<%=author%>"> <%=author%> </a> 
                    <br>
                    <%= message.getContent() %>
                  </div>
                </div>
          <%
              }
            } 
          %>
        </div>
      </div>

      <hr/>

      <form action="/chat/<%= conversation.getId() %>" method="POST">
        <div class="flex" id="messageForm">
          <div class="form-group" id="messageInput">
            <input type="text" class="form-control" name="message" placeholder="Type your message here ... ">
          </div>
          <button type="submit" class="btn btn-primary">Send</button>
        </div>
      </form>

      <hr/>
    </div>
  </div>

</body>
</html>
