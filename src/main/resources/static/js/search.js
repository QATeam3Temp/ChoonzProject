const TrackBox = document.querySelector("#Track");
const AlbumBox= document.querySelector("#Album");
const ArtistBox= document.querySelector("#Artist");
const GenreBox= document.querySelector("#Genre");
const PlaylistBox = document.querySelector("#Playlist");

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

  function load() {
    searchTrack();
    searchArtists();
    searchGenre();
    searchAlbum();
    searchPlaylist();
  }
  async function searchAlbum() {
    let album = await sendHttpRequest(
      "GET",
      `http://localhost:8082/albums/read/name/`+ getUrlVars()['x']
    );
    AlbumBox.innerHTML =` <a href ="/album?id=`+album.id+`"><div id = "Album"><h2>Playlist: `+album.name+`</h2>
    <img src=`+album.cover+`></img>
 </div>`;
  }
  async function searchPlaylist() {
    let playlist = await sendHttpRequest(
      "GET",
      `http://localhost:8082/playlists/read/name/`+ getUrlVars()['x']
    );
    PlaylistBox.innerHTML =` <a href ="/playlist?id=`+playlist.id+`"><div id = "Track"><h2>Playlist: `+playlist.name+`</h2>
    <p>`+playlist.description.substring(0,50)+`...</p>
 </div>`;
  }
  async function searchTrack() {
    
    let track = await sendHttpRequest(
      "GET",
      `http://localhost:8082/tracks/read/name/`+ getUrlVars()['x']
    );
    TrackBox.innerHTML =` <a href ="/track?id=`+track.id+`"><div id = "Track"><h2>Track: `+track.name+`</h2>
   <h5>`+track.duration+`</h5>
   <p>`+track.lyrics.substring(0,50)+`...</p>
</div>`;
  }
  async function searchGenre() {
    let genre = await sendHttpRequest(
      "GET",
      `http://localhost:8082/genres/read/name/`+ getUrlVars()['x']
    );
    GenreBox.innerHTML =` <a href ="/genre?id=`+genre.id+`"><div id = "Genre"><h2>Genre: `+genre.name+`</h2>
    <p>`+genre.description+`</p>
 </div>`;
  }
  async function searchArtists() {
    let artist = await sendHttpRequest(
      "GET",
      `http://localhost:8082/artists/read/name/`+ getUrlVars()['x']
    );
    ArtistBox.innerHTML =` <a href ="/artist?id=`+artist.id+`"><div id = "Artist"><h2>Artist: `+artist.name+`</h2>
 </div>`;
  }