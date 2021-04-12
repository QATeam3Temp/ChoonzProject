"use strict";

const getPlaylistName = document.querySelector("#playlist-name");
const getDescription = document.querySelector("#description");
const getArtWork = document.querySelector("#art-work");
const createButton = document.querySelector("#create-button");
const addTracksButton = document.querySelector("#add-tracks");
var tracks = [];

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

async function createPlaylist(name, description, artwork) {
    const postPlaylist = {
        name: name,
        description: description,
        artwork: artwork,
        tracks: tracks
    };
    console.log(postPlaylist);
    await sendHttpRequest("POST", `http://localhost:8082/playlists/create`, postPlaylist);
}

createButton.addEventListener("click", (event) => {
    event.preventDefault();
    const enteredPlaylistName = getPlaylistName.value;
    const enteredDescriptionName = getDescription.value;
    const enteredArtworkName = getArtWork.value;

    createPlaylist(enteredPlaylistName, enteredDescriptionName, enteredArtworkName);
});

function load(){
    setupTargets()
  }

async function setupTargets(){
    let targets = (await sendHttpRequest("GET",`http://localhost:8082/tracks/read`))
    targets.forEach(target => {
    if (tracks.indexOf(target.id)) {
        let t = "<option value="+ target.id + ">"+target.name + "</option>"
    swordOfDamocles.innerHTML+= t;
    }
    });
  }

addTracksButton.addEventListener("click", (event) => {
    event.preventDefault();
    tracks.push(swordOfDamocles.value);
})

