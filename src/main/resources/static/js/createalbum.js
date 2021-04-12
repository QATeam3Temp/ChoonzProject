"use strict";

const addingTrackToAlbum = document.querySelector("#addTrackToAlbum");
const createAlbumBtn = document.querySelector("#createAlbum");
const getAlbumName = document.querySelector("#album-name");
const getCover = document.querySelector("#customAlbumCover");
const getTracks = document.querySelector("#tracksSelector");
const getGenres = document.querySelector("#genreSelectorAlbums");
const getArtists = document.querySelector("#artistSelectorAlbums");
var tracks = [];

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

async function createAlbum(name, cover, artist, genre) {
  let postAlbum = {
    name: name,
    cover: cover,
    artist: artist,
    tracks: tracks,
    genre: genre,
  };
  await sendHttpRequest(
    "POST",
    `http://localhost:8082/albums/create`,
    postAlbum
  );
  console.log(status);
  if (status == 201) {
    alert("Artist created");
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
  let artistTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/artists/read/`
  );
  artistTargets.forEach((artistTarget) => {
    let t =
      "<option value=" +
      artistTarget.id +
      ">" +
      artistTarget.name +
      "</option>";
    artistSelectorAlbums.innerHTML += t;
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

async function setupTrack() {
  let trackTargets = await sendHttpRequest(
    "GET",
    `http://localhost:8082/tracks/read/`
  );
  trackTargets.forEach((trackTarget) => {
    let t =
      "<option value=" + trackTarget.id + ">" + trackTarget.name + "</option>";
    getTracks.innerHTML += t;
  });
}

addingTrackToAlbum.addEventListener("click", (event) => {
  event.preventDefault();
  tracks.push(getTracks.value);
});

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
