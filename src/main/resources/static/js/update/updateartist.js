"use strict";

const updateArtistBtn = document.querySelector("#artistName")
const getArtistName = document.querySelector("#track-name");
const emptyList = 0;
var artist;

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
            "Key":document.cookie.split("=")[1]
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

  function load(){
    setup();
}

  async function setup(){
    
    artist  = (await sendHttpRequest("GET",`http://localhost:8082/artists/read/id/`+getUrlVars()["x"]))
    let name =  document.querySelector("#track-name");
    name.value = artist.name;
    console.log(artist)
  }


  async function updateArtist(artistName, album) {
      const post = {
          name: artistName,
          albums: artist.albums
      };
      await sendHttpRequest("PUT", `http://localhost:8082/artists/update/`+getUrlVars()["x"] , post);
      console.log(status);
      if (status = 201) {
          console.log("Artist has been updated updated")
      } else {
          console.log("Invalid entry, please check the")
      }
  }

  updateArtistBtn.addEventListener("click", (event) => {
      event.preventDefault();
      const enteredArtist = getArtistName.value;
      const enteredAlbum = emptyList;
      updateArtist(enteredArtist, enteredAlbum);
  })