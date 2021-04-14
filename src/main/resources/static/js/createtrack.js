"use strict";

const getTrackName = document.querySelector("#track-name");
const getDuration = document.querySelector("#duration");
const getLyrics = document.querySelector("#inp-lyr");
const createButton = document.querySelector("#create-button");

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
    album: 0,
    playlist: 0
  };
  console.log(postTrack);
  await sendHttpRequest("POST", `http://localhost:8082/tracks/create`, postTrack);
}

createButton.addEventListener("click", (event) => {
  event.preventDefault();
  const enteredTrackName = getTrackName.value;
  const enteredDuration = getDuration.value;
  const enteredLyrics = getLyrics.value;

  createTrack(enteredTrackName, enteredDuration, enteredLyrics);
});