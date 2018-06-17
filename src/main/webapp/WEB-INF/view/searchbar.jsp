<!-- Checks if the search request is empty. -->
<script type="text/javascript">
  function isEmpty() {
    var searchBar = document.getElementById("searchBar");
    if(searchBar.value.length < 1) {
      alert("Please enter a valid search.");
      return false;
    }
    return true;
  }
</script>

<form action="/search" method="GET">
  <input type="text" name="searchRequest" id="searchBar"><button type="submit" onclick="return isEmpty();"><i class="fa fa-search"></i></button>
</form>