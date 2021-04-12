"use strict";

const createArtistBtn = document.querySelector("#artistName");
const getArtistName = document.querySelector("#track-name");
const emptyList = [];

function sendHttpRequest(method, url, data) {
    console.log(data);
    return fetch(url, {
        method: method,
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
            "Key": document.cookie.split("=")[1]
        }
    })
    .then((response) => {
        status = response.status;
        if (response.status >= 200 && response.status < 300) {
          return response.json();
        } else {
          return response.json().then((errData) => {
            console.log(errData);
            throw new Error(response.status);
          });
        }
      })
      .catch((error) => {
        console.log(error);
        throw new Error("Something went wrong");
      });
  }

  async function createArtist(name) {
      let post = {
          name: name,
          albums: []
      };
      await sendHttpRequest("POST", `http://localhost:8082/artists/create`, post);
      console.log(status);
      if (status == 201) {
          alert("Artist created")
          console.log("Artist has been created")
      } else {
          console.log("Invalid entry, please check the")
      }
  }

  createArtistBtn.addEventListener("click", (event) => {
      event.preventDefault();
      const enteredArtist = getArtistName.value;
      const enteredAlbum = emptyList;
      createArtist(enteredArtist, enteredAlbum);
  })