$(function() {
  $('#ChangeToggle').click(function() {
    $('#navbar-hamburger').toggleClass('hidden');
    $('#navbar-close').toggleClass('hidden');  
  });
});


function setUser() {
  let user1 = document.querySelector("#navbar-right");
  if (user1 != null) {
    user1.innerHTML=""

  }
}




function load() {
  let logout = document.createElement("a");
let x = document.querySelector('a');
let div = document.querySelector("#navbar-right");
logout.innerHTML="Replaced"
div.innerHTML=""

  if (document.cookie) {
    showloggedin() 
  } else {
    showguest()
  }
}


function showloggedin() {

}