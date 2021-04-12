"use strict";

const getGenreName = document.querySelector("#genre-name");
const getDescription = document.querySelector("#description");
const createButton = document.querySelector("#create-button");
var genre;

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
  }

function sendHttpRequest(method, url, data) {
  return fetch(url, {
    method: method,
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
      "Key": document.cookie.split("=") [1]
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

function load(){
    setup();
}

async function setup(){
    
    genre  = (await sendHttpRequest("GET",`http://localhost:8082/genres/read/id/`+getUrlVars()["x"]))
    let name =  document.querySelector("#genre-name");
    name.value = genre.name;
    let description =  document.querySelector("#description");
    description.value = genre.description;
  }


async function createGenre(name, description) {
  const postGenre = {
    name: name,
    description: description,
    albums: genre.albums
  };
  await sendHttpRequest("PUT", `http://localhost:8082/genres/update/`+getUrlVars()["x"], postGenre);
}

createButton.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredGenreName = getGenreName.value;
  const enteredDescription = getDescription.value;

  createGenre(enteredGenreName, enteredDescription);
});