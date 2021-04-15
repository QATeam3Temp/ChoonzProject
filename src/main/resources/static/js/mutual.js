const nav = document.querySelector('nav');

window.addEventListener('scroll', function () {
  const offset = window.pageYOffset;

  if (offset > 75) {
    nav.classList.add('scroll');
  } else {
    nav.classList.remove('scroll');
  }
});

function loggedIn() {
  let logout = document.createElement('a');
  let x = document.querySelector('a');
  let div = document.querySelector('#logarea');
  logout.onclick = function () {
    document.cookie = 'Key=; max-age=0; path=/;';
    showguest();
  };
  logout.setAttribute('class', 'navbar-link');

  if (document.cookie) {
    logout.innerHTML = 'Logout';
    div.innerHTML = '';
    div.appendChild(logout);
  } else {
    showguest();
  }
}

function showguest() {
  let div = document.querySelector('#logarea');
  div.innerHTML = `<a class="navbar-link" href="login.html">Log in</a> <a class="navbar-link" href="signup.html">Sign Up</a>`;
}
