"use strict";

const getPlaylistName = document.querySelector("#playlist-name");
const getDescription = document.querySelector("#description");
const getArtWork = document.querySelector("#art-work");
const updateButton = document.querySelector("#update-button");
const addTracksButton = document.querySelector("#add-tracks");
const tracksBox = document.querySelector("#tracksbox");
var tracks = [];
var playlist;

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

async function updatePlaylist(name, description, artwork) {
    const postPlaylist = {
        name: name,
        description: description,
        artwork: artwork,
        tracks: tracks
    };
    await sendHttpRequest("PUT", `http://localhost:8082/playlists/update/`+getUrlVars()["x"], postPlaylist);
    
    initialTracks.forEach(track => {
        if (tracks.includes(track.id)) {
            
        }else{
            sendHttpRequest("PUT", `http://localhost:8082/tracks/update/playlist/`+track.id, postPlaylist);
        }
    });
}

updateButton.addEventListener("click", (event) => {
    event.preventDefault();
    const enteredPlaylistName = getPlaylistName.value;
    const enteredDescriptionName = getDescription.value;
    const enteredArtworkName = getArtWork.value;

    updatePlaylist(enteredPlaylistName, enteredDescriptionName, enteredArtworkName);
});

function load(){
    setup()
    
  }
async function setup(){
    initialTracks =  (await sendHttpRequest("GET",`http://localhost:8082/tracks/read/playlist/`+getUrlVars()["x"]));
    playlist  = (await sendHttpRequest("GET",`http://localhost:8082/playlists/read/id/`+getUrlVars()["x"]))
    getPlaylistName.value = playlist.name;
    getDescription.value = playlist.description;
    getArtWork.value= playlist.artwork;
    tracks = playlist.tracks
    setupTargets()
  }
async function setupTargets(){
  swordOfDamocles.innerHTML=""
  tracksBox.innerHTML=""
    let targets = (await sendHttpRequest("GET",`http://localhost:8082/tracks/read`))
    targets.forEach(target => {
    if (tracks.includes(target.id)) {
      let t = "<a onclick=\"removeTrack("+target.id+")\">"+target.name+"</a>";
      tracksBox.innerHTML += t;
      tracksBox.innerHTML += "<br>";
    }else{
      let t = "<option value="+ target.id + ">"+target.name + "</option>"
      swordOfDamocles.innerHTML+= t;
      
    }
    });
  }

addTracksButton.addEventListener("click", (event) => {
    event.preventDefault();
    tracks.push(parseInt(swordOfDamocles.value));
    setupTargets();
})

function removeTrack(id){
    tracks.pop(id);
    setupTargets();
}
var initialTracks =  [];
