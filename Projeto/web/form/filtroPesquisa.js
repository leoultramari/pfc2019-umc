$(document).ready(function(){
  $("#busca").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#lista tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});