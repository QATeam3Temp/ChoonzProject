"use strict";

const getUserName = document.querySelector("#username");
const getPassword = document.querySelector("#password");
const getConfirmationPassword = document.querySelector("#confpassword");
const createAccountBtn = document.querySelector("#submit");

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
  if (await sendHttpRequest("POST", `http://localhost:8082/users/signup`, post).status == 226) {
      console.log(username + "in use")
  }
  
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
