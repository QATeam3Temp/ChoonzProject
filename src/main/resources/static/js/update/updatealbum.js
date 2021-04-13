"use strict";

const addingTrackToAlbum = document.querySelector("#addTrackToAlbum");
const updateAlbumBtn = document.querySelector("#updateAlbum");
const getAlbumName = document.querySelector("#album-name");
const getCover = document.querySelector("#customAlbumCover");
const getTracks = document.querySelector("#tracksSelector");
const getGenres = document.querySelector("#genreSelectorAlbums");
const getArtists = document.querySelector("#artistSelectorAlbums");
const tracksBox = document.querySelector("#tracksbox");
var tracks = [];
var album;
var initialTracks =  [];

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
  }

function sendHttpRequest(method, url, data) {
  console.log(data);
  return fetch(url, {
    method: method,
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
      Key: document.cookie.split("=")[1],
    },
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

async function updateAlbum(name, cover, artist, genre) {
  let postAlbum = {
    name: name,
    cover: cover,
    artist: artist,
    tracks: tracks,
    genre: genre,
  };
  await sendHttpRequest(
    "PUT",
    `http://localhost:8082/albums/update/`+getUrlVars()["x"],
    postAlbum
  );
  console.log(status);
  if (status == 202) {
    alert("Artist updated");
    console.log("Album has been updated");
  } else {
    console.log("Invalid entry, please entry fields are valid");
  }
  initialTracks.forEach(track => {
    if (tracks.includes(track.id)) {
        
    }else{
        sendHttpRequest("PUT", `http://localhost:8082/tracks/update/album/`+track.id, postAlbum);
    }
});




}

function load() {
    setup();
}
async function setup(){
    initialTracks = (await sendHttpRequest("GET",`http://localhost:8082/tracks/read/album/`+getUrlVars()["x"]));
    album  = (await sendHttpRequest("GET",`http://localhost:8082/albums/read/id/`+getUrlVars()["x"]))
    
    setupArtists();
  setupGenre();
getCover.value = album.cover;
  getAlbumName.value = album.name;
  getGenres.value = album.genre;
    getArtists.value= album.artist;
    tracks = album.tracks
    setupTrack();
}
async function setupArtists() {
  let artistTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/artists/read/`
  );
  artistTargets.forEach((artistTarget) => {
    let t;
      if(artistTarget.id==album.artist){
        t =
        "<option value=" +
        artistTarget.id +
        " selected>" +
        artistTarget.name +
        "</option>";
      }else{
        t =
        "<option value=" +
        artistTarget.id +
        ">" +
        artistTarget.name +
        "</option>";
      }
    
    artistSelectorAlbums.innerHTML += t;
  });
}

async function setupGenre() {
  let genreTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/genres/read/`
  );

  let t;
  genreTargets.forEach((genreTarget) => {
    if(genreTarget.id==album.genre){
        t =
        "<option value=" + genreTarget.id + " selected>" + genreTarget.name + "</option>";
      }else{
        t =
        "<option value=" + genreTarget.id + ">" + genreTarget.name + "</option>";
      }
     
    genreSelectorAlbums.innerHTML += t;
  });
}

async function setupTrack() {
    tracksBox.innerHTML="";
    getTracks.innerHTML="";
  let trackTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/tracks/read/`
  );
  trackTargets.forEach(target => {
    if (tracks.includes(target.id)) {
      let t = "<a onclick=\"removeTrack("+target.id+")\">"+target.name+"</a>";
      tracksBox.innerHTML += t;
      tracksBox.innerHTML += "<br>";
    }else{
      let t = "<option value="+ target.id + ">"+target.name + "</option>"
      getTracks.innerHTML += t;
      
    }
  });
}

addingTrackToAlbum.addEventListener("click", (event) => {
    event.preventDefault();
    if(getTracks.value){  
  tracks.push(getTracks.value);
  setupTrack();
    }
});

updateAlbumBtn.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredAlbumName = getAlbumName.value;
  const enteredArtistName = getArtists.value;
  const enteredGenreName = getGenres.value;
  const enteredCover = getCover.value;
  updateAlbum(
    enteredAlbumName,
    enteredCover,
    enteredArtistName,
    enteredGenreName
  );
});
function removeTrack(id){
    tracks.pop(id);
    setupTrack();
}