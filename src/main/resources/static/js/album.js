function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}
const albumId = getUrlVars()['id'];
console.log(albumId);
async function fetchAlbum(id) {
  const response = await fetch(`http://localhost:8082/albums/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const album = await response.json();
  return album;
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

async function fetchGenre(id) {
  const response = await fetch(`http://localhost:8082/genres/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const genre = await response.json();
  return genre;
}

async function fetchArtist(id) {
  const response = await fetch(`http://localhost:8082/artists/read/id/${id}`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const artist = await response.json();
  return artist;
}

fetchAlbum(albumId)
  .then((album) => {
    document.title = album.name;
    const genreId = album.genre;
    const artistId = album.artist;
    const main = document.querySelector('.container');
    const albumCard = document.createElement('div');
    const albumTrackWrapper = document.createElement('div');
    const albumName = document.createElement('h4');
    const albumGenre = document.createElement('p');
    const albumArtist = document.createElement('p');
    const albumFeatureArtist = document.createElement('p');
    const albumCover = document.createElement('img');

    albumCover.src = album.cover;
    albumName.innerHTML = album.name;
    fetchGenre(genreId).then((genre) => {
      albumGenre.innerHTML = `Genre: <a href='/genre?id=${genreId}'>${genre.name}</a>`;
    });

    fetchArtist(artistId).then((artist) => {
      albumArtist.innerHTML = `Artist: <a href='/artist?id=${artistId}'>${artist.name}</a>`;
    });

    album.featuredArtists.forEach((fArtist) => {
      fetchArtist(fArtist).then((artist) => {
        albumFeatureArtist.innerHTML = `Feature Artist: ${artist.name}`;
      });
    });

    console.log(album);
    album.tracks.forEach((track) => {
      const albumTrack = document.createElement('a');
      fetchTrack(track).then((t) => {
        albumTrack.innerHTML = t.name;
        albumTrack.href = '/track?id=' + t.id;
      });
      albumTrackWrapper.appendChild(albumTrack);
    });

    albumCard.className = 'd-flex flex-column';
    albumName.className = 'my-4';

    albumCard.appendChild(albumName);
    albumCard.appendChild(albumCover);
    albumCard.appendChild(albumArtist);
    albumCard.appendChild(albumFeatureArtist);
    albumCard.appendChild(albumGenre);
    albumCard.appendChild(albumTrackWrapper);
    main.appendChild(albumCard);
  })
  .catch((error) => error.message);
