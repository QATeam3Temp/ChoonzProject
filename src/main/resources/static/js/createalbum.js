"use strict";

const addingTrackToAlbum = document.querySelector("#addTrackToAlbum");
const addingArtistToAlbum = document.querySelector("#addArtistToAlbum");
const createAlbumBtn = document.querySelector("#createAlbum");
const getAlbumName = document.querySelector("#album-name");
const getCover = document.querySelector("#customAlbumCover");
const getTracks = document.querySelector("#tracksSelector");
const getGenres = document.querySelector("#genreSelectorAlbums");
const getArtists = document.querySelector("#artistSelectorAlbums");
const getFeaturedArtists = document.querySelector("#artistSelector");
const tracksBox = document.querySelector("#tracksbox");
const artistBox = document.querySelector("#artistBox");
var tracks = [];
var artists = [];

function sendHttpRequest(method, url, data) {
  console.log(data);
  return fetch(url, {
    method: method,
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
      "Key": document.cookie.split("=")[1],
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

async function createAlbum(name, cover, artist, genre) {
  let postAlbum = {
    name: name,
    cover: cover,
    artist: artist,
    tracks: tracks,
    genre: genre,
    featuredArtists: artists
  };
  await sendHttpRequest(
    "POST",
    `http://localhost:8082/albums/create`,
    postAlbum
  );
  console.log(status);
  if (status == 201) {
    console.log("Album has been created");
  } else {
    console.log("Invalid entry, please entry fields are valid");
  }
}

function load() {
  setupArtists();
  setupGenre();
  setupTrack();
}

async function setupArtists() {
  artistSelectorAlbums.innerHTML = "";
    getFeaturedArtists.innerHTML = "";
  let artistTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/artists/read/`
  );
  artistTargets.forEach((artistTarget) => {

      if (artists.includes(artistTarget.id)) {
        let t = "<a  onclick=\"removeArtist("+artistTarget.id+")\">"+artistTarget.name+"</a>";
        artistBox.innerHTML += t;
        artistBox.innerHTML += "<br>";
      }else{
        let t = "<option value="+ artistTarget.id + ">"+artistTarget.name + "</option>"
        getFeaturedArtists.innerHTML += t;
      artistSelectorAlbums.innerHTML += t;
  
      }
    


  });
}

async function setupGenre() {
  let genreTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/genres/read/`
  );
  genreTargets.forEach((genreTarget) => {
    let t =
      "<option value=" + genreTarget.id + ">" + genreTarget.name + "</option>";
    genreSelectorAlbums.innerHTML += t;
  });
}
function removeTrack(id){
  tracks.pop(id);
  setupTrack();
}
function removeArtist(id){
  artists.pop(id);
  setupArtists();
}
async function setupTrack() {
  let trackTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/tracks/read/`
  );
  trackTargets.forEach((trackTarget) => {
    if (tracks.includes(trackTarget.id)) {
      let t = "<a onclick=\"removeTrack("+trackTarget.id+")\">"+trackTarget.name+"</a>";
      tracksBox.innerHTML += t;
      tracksBox.innerHTML += "<br>";
    }else{
      let t = "<option value="+ trackTarget.id + ">"+trackTarget.name + "</option>"
      getTracks.innerHTML += t;
      
    }

      
  });
}
addingTrackToAlbum.addEventListener("click", (event) => {
  event.preventDefault();
  tracks.push(parseInt(getTracks.value));
  setupTrack();
})

addingArtistToAlbum.addEventListener("click", (event) => {
  event.preventDefault();
  artists.push(parseInt(getFeaturedArtists.value));
  setupArtists();
})


createAlbumBtn.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredAlbumName = getAlbumName.value;
  const enteredArtistName = getArtists.value;
  const enteredGenreName = getGenres.value;
  const enteredCover = getCover.value;
  createAlbum(
    enteredAlbumName,
    enteredCover,
    enteredArtistName,
    enteredGenreName
  );
});
