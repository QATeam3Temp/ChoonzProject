"use strict";

const getTrackName = document.querySelector("#track-name");
const getDuration = document.querySelector("#duration");
const getLyrics = document.querySelector("#inp-lyr");
const createButton = document.querySelector("#create-button");
var track;


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

async function createTrack(name, duration, lyrics) {
  const postTrack = {
    name: name,
    duration: duration,
    lyrics: lyrics,
    album: track.album,
    playlist: track.playlist
  };
  await sendHttpRequest("PUT", `http://localhost:8082/tracks/update/`+getUrlVars()["x"], postTrack);
}

function load(){
    setup()
}

async function setup(){
    console.log("aaaa")
    track  = (await sendHttpRequest("GET",`http://localhost:8082/tracks/read/id/`+getUrlVars()["x"]))
    let name =  document.querySelector("#track-name");
    name.value = track.name;
    let duration =  document.querySelector("#duration");
    duration.value = track.duration;
    let lyrics =  document.querySelector("#inp-lyr");
    lyrics.value = track.lyrics;
    console.log(track)
  }

createButton.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredTrackName = getTrackName.value;
  const enteredDuration = getDuration.value;
  const enteredLyrics = getLyrics.value;

  createTrack(enteredTrackName, enteredDuration, enteredLyrics);
});