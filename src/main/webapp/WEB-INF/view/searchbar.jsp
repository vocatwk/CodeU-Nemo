<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.List" %>
<%
List<User> allUsers = UserStore.getInstance().getAllUsers();
%>

<script type="text/javascript">
  // Checks if the search request is empty.
  function isEmpty() {
    var searchBar = document.getElementById("searchBar");
    if(searchBar.value.length < 1) {
      alert("Please enter a valid search.");
      return false;
    }
    return true;
  }

  // Toggle drop down.
  function toggleDropDown() {
    var searchBar = document.getElementById("searchBar");
    var dropDiv = document.getElementById("dropDiv");
    if (searchBar.value == "") {
      dropDiv.style.display = "none";
    }
    else {
      dropDiv.style.display = "block"
    }
  }

  function keypressed() {
    fetch('/search?searchRequest=' + document.querySelector('#searchBar').value)
      .then(
        function(response) {
          if (response.status !== 200) {
            console.log("Looks like there was a problem. Status Code: " + 
              response.status);
            return;
          }

          response.json().then(function(data) {
            console.log(data);
          });
        }
      ) 
      .catch(function(err) {
        console.log("Fetch Error :-S", err);
      });
  }
</script>

<form action="/search" method="GET">
  <input onkeyup="toggleDropDown()" onkeypress="keypressed()" type="text" autocomplete="off" name="searchRequest" id="searchBar"><button type="submit" onclick="return isEmpty();"><i class="fa fa-search"></i></button>
  <div id="dropDiv" style="display: none; background-color: grey;">
    <%
    for (User u : allUsers) {
    String username = u.getName();
    %>
      <a href="/profile/<%= username %>"><%= username %></a>
      <br>
    <%
    }
    %>
  </div>
</form>