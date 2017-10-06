$(document).ready(function(){
              
  $("#bk_select").on('change', function(){
    if ($(this).val()=="ew"){
      $("#checking_div").hide();
    }
    else {
      $("#checking_div").show();
    }
  });
  
});

function sent() {
    document.getElementById("myForm").action = $(location).attr('pathname') + "rest/edit";
    document.getElementById("myForm").submit();
}