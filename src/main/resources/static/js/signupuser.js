"use strict";

const getUserName = document.querySelector("#username");
const getPassword = document.querySelector("#password");
const getConfirmationPassword = document.querySelector("#confpassword");
const createAccountBtn = document.querySelector("#submit");
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
        return response.json();
      } else {
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

async function createPost(userName, password) {
  const post = {
    username: userName,
    password: password,
  };
  await sendHttpRequest("POST", `http://localhost:8082/users/signup`, post);
  console.log(status);
  if (status == 226) {
    console.log(username + "in use");
  }
  if (status == 201) {
    console.log("It works!");
    window.location.href = "http://localhost:8082/home";
  }
  status = 0;
}

createAccountBtn.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredUserName = getUserName.value;
  const enteredPassword = getPassword.value;
  if (validatePassword) {
    createPost(enteredUserName, enteredPassword);
  } else {
    console.log("Passwords do not match!");
  }
});

function validatePassword() {
  if (getPassword.value != getConfirmationPassword.value) {
    alert("Passwords do not match.");
    return false;
  }
  return true;
}
