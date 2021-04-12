"use strict";

const createArtistBtn = document.querySelector("#artistName")
const getArtistName = document.querySelector("#track-name");
const emptyList = 0;

function sendHttpRequest(method, url, data) {
    console.log(data);
    return fetch(url, {
        method: method,
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
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

  async function createArtist(artistName, album) {
      const post = {
          name: artistName,
          album: album,
      };
      await sendHttpRequest("POST", `http://localhost:8082/artist/create`, post);
      console.log(status);
      if (status = 201) {
          console.log("Artist with name " + artistName + " has been created")
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