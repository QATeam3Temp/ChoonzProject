"use strict";

const getGenreName = document.querySelector("#genre-name");
const getDescription = document.querySelector("#description");
const createButton = document.querySelector("#create-button");

function sendHttpRequest(method, url, data) {
  return fetch(url, {
    method: method,
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
      Key: document.cookie.split("=")[1],
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

async function createGenre(name, description) {
  const postGenre = {
    name: name,
    description: description,
    albums: [],
  };
  console.log(postGenre);
  await sendHttpRequest(
    "POST",
    `http://localhost:8082/genres/create`,
    postGenre
  );
}

createButton.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredGenreName = getGenreName.value;
  const enteredDescription = getDescription.value;

  createGenre(enteredGenreName, enteredDescription);
});
