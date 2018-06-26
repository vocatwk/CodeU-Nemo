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

  // This is to remove the resultItem divs
  function removeElementsByClass(className){
    var elements = document.getElementsByClassName(className);
    while(elements.length > 0){
      elements[0].parentNode.removeChild(elements[0]);
    }
  }

  // Iterates through results
  function fetchResults() {
    var searchBarValue = document.querySelector('#searchBar').value;
    if (searchBarValue.length > 0) {
      fetch('/search?searchRequest=' + searchBarValue, 
        {credentials: "same-origin"})
      .then(
        function(response) {
          if (response.status !== 200) {
            console.log("Looks like there was a problem. Status Code: " + response.status);
            return;
          }
          response.json().then(function(data) {
            if (data != null) {
              // Start with no resultItem divs
              removeElementsByClass("resultItem");
              for (var user in data) {
                var userName = data[user].name;
                var div = document.createElement("div");
                div.setAttribute("class", "resultItem");
                div.setAttribute("id", userName);
                a = document.createElement("a");
                a.href = "/profile/" + userName;
                a.innerHTML = userName;
                div.appendChild(a);
                document.getElementById("result").appendChild(div);
              }
            }
            // Once data is null, remove resultItem divs
            else {
              removeElementsByClass("resultItem");
            }
          });
        }
      ) 
      .catch(function(err) {
        console.log("Fetch Error :-S", err);
      });
    }
    // Clear the results when the search bar is empty
    else {
      removeElementsByClass("resultItem");
    }
  }
</script>

<form action="/search" method="GET">
  <input onkeyup="fetchResults()" type="text" autocomplete="off" name="searchRequest" id="searchBar"><button type="submit" onclick="return isEmpty();"><i class="fa fa-search"></i></button>
  <div id="result">
    <!-- resultItem divs will go here -->
  </div>
</form>