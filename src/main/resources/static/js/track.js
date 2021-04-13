function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}
const trackId = getUrlVars()['id'];

async function fetchTrack(id) {
  const response = await fetch(`http://localhost:8082/tracks/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const track = response.json();
  return track;
}

async function fetchPlaylist(id) {
  const response = await fetch(`http://localhost:8082/playlists/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const playlist = response.json();
  return playlist;
}

async function fetchAlbum(id) {
  const response = await fetch(`http://localhost:8082/albums/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const album = response.json();
  return album;
}

fetchTrack(trackId).then((track) => {
  console.log(track);
  document.title = track.name;

  const albumId = track.album;
  const playlistId = track.playlist;
  const main = document.querySelector('.container');
  const trackContainer = document.createElement('div');
  const trackName = document.createElement('h4');
  const album = document.createElement('h4');
  const playlist = document.createElement('h4');
  const duration = document.createElement('p');
  const lyrics = document.createElement('p');

  trackName.innerHTML = `Track name: ${track.name}`;
  duration.innerHTML = `Duration: ${track.duration}`;
  lyrics.innerHTML = `Lyrics: ${track.lyrics}`;

  if (playlistId == 0) {
    playlist.innerHTML = 'No playlist attributed yet';
  } else {
    fetchPlaylist(playlistId).then((p) => {
      playlist.innerHTML = `playlist: ${p.name}`;
    });
  }

  fetchAlbum(albumId).then((a) => {
    album.innerHTML = `Album: ${a.name}`;
  });

  trackContainer.appendChild(trackName);
  trackContainer.appendChild(album);
  trackContainer.appendChild(playlist);
  trackContainer.appendChild(duration);
  trackContainer.appendChild(lyrics);
  main.appendChild(trackContainer);
});
