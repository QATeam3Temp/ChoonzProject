"use strict";

const getUserName = document.querySelector("#username");
const getPassword = document.querySelector("#password");
const logInBtn = document.querySelector("#submit");
var status;

function sendHttpRequest(method, url, data) {
  console.log(data);
  return fetch(url, {
    method: method,
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      status = response.status;
      if (response.status >= 200 && response.status < 300) {
        return response.text();
      } else {
        if (response.status == 401) {
          alert("Username or password is incorrect");
          console.log("Username or password is incorrect");
        }
        return response.json().then((errData) => {
          console.log(errData);
          throw new Error("Something went wrong - server-side");
        });
      }
    })
    .catch((error) => {
      console.log(error);
      throw new Error("Something went wrong");
    });
}

async function logInPost(userName, password) {
  const logInData = {
    username: userName,
    password: password,
  };

  document.cookie =
    "Key =" +
    (await sendHttpRequest(
      "POST",
      `http://localhost:8082/users/login`,
      logInData
    ));
  let myCookie = document.cookie;
  console.log(myCookie);
  

  if (status == 200) {
    window.location.href = "http://localhost:8082/home";
  }
}

logInBtn.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredUserName = getUserName.value;
  const enteredPassword = getPassword.value;

  logInPost(enteredUserName, enteredPassword);
});
