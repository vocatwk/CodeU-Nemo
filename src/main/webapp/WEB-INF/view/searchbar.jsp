<script type="text/javascript">
  // Iterates through fetched results
  function fetchResults(putResultsIn, forValue) {
    var searchBarValue = document.querySelector('#' + forValue).value;
    if (searchBarValue.length > 0) {
      fetch('/search?searchRequest=' + searchBarValue, {credentials: "same-origin"})
        .then(
          function(response) {
            if (response.status !== 200) {
              console.log("Looks like there was a problem. Status Code: " + response.status);
              return;
            }
            response.json().then(function(data) {
              // Start with no resultItem divs
              document.querySelector('#' + putResultsIn).innerHTML = '';
              if(putResultsIn === 'userResult'){
                for (var user in data) {
                  var userName = data[user].name;
                  var li = document.createElement("li");
                  li.setAttribute("class", "list-group-item");
                  a1 = document.createElement("a");
                  a1.href = "/profile/" + userName;
                  a1.innerHTML = userName;
                  li.appendChild(a1);

                  if(!membersOfConversation.has(userName)){
                    a2 = document.createElement("a");
                    a2.innerHTML = "add";
                    a2.setAttribute("class", "add-user-button");
                    a2.setAttribute("username", userName);

                    if(ToBeAddedToConversation.has(userName)){
                      a2.classList.add("isDisabled");
                    }else{
                      a2.href = "#";
                    }
                    li.appendChild(a2);
                  }

                  document.getElementById(putResultsIn).appendChild(li);
                }
              } else {
                for (var user in data) {
                  var userName = data[user].name;
                  var div = document.createElement("div");
                  div.setAttribute("class", "resultItem");
                  a = document.createElement("a");
                  a.href = "/profile/" + userName;
                  a.innerHTML = userName;
                  div.appendChild(a);
                  document.getElementById(putResultsIn).appendChild(div);
                }
              }
            });
          }
        )
        .catch(err => console.log("Fetch Error :-S", err));
    }
    // Search bar is empty
    else {
      // Clear the resultItem divs
      document.querySelector('#' + putResultsIn).innerHTML = '';
    }
  }
</script>

<div id ="searchDiv">
  <input onkeyup="fetchResults('result','searchBar')" type="text" autocomplete="off" placeholder="Search for Users. . ." name="searchRequest" id="searchBar">
  <div id="result">
    <!-- resultItem divs will go here -->
  </div>
</div>
