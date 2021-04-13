function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}

const playlistId = getUrlVars()['id'];

async function fetchPlaylist(playlistId) {
  const response = await fetch(`http://localhost:8082/playlists/read/id/${playlistId}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const playlist = await response.json();
  return playlist;
}

async function fetchTrack(id) {
  const response = await fetch(`http://localhost:8082/tracks/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const track = await response.json();
  return track;
}

fetchPlaylist(playlistId)
  .then((playlist) => {
    console.log(playlist);
    document.title = playlist.name;
    const main = document.querySelector('#container');
    const playlistCard = document.createElement('div');
    const playlistWrapper = document.createElement('div');
    const playlistName = document.createElement('h4');
    const playlistDescription = document.createElement('p');
    const playlistArtwork = document.createElement('p');


    playlist.tracks.forEach((track) => {
      const playlistTrack = document.createElement('p');
      fetchTrack(track).then((t) => {
        playlistTrack.innerHTML = t.name;
      });
      playlistWrapper.appendChild(playlistTrack);
    });


    playlistName.innerHTML =  `Playlist: ${playlist.name}`;
    playlistDescription.innerHTML = `Description: ${playlist.description}`;
    playlistArtwork.innerHTML = `Artwork: ${playlist.artwork}`;

    playlistCard.className = 'd-flex flex-column';
    playlistName.className = 'my-4';

    playlistCard.appendChild(playlistName);
    playlistCard.appendChild(playlistDescription);
    playlistCard.appendChild(playlistArtwork);
    playlistCard.appendChild(playlistWrapper);
    main.appendChild(playlistCard);
  })
  .catch((error) => error.message);