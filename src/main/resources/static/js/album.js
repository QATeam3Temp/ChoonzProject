async function fetchAlbum() {
  const response = await fetch(`http://localhost:8082/albums/read/id/1`);
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

fetchAlbum()
  .then((album) => {
    document.title = album.name;
    const genreId = album.genre;
    const artistId = album.artist;
    const main = document.querySelector('.container');
    const albumCard = document.createElement('div');
    const albumName = document.createElement('h4');
    const albumTrackList = document.createElement('ul');
    const albumGenre = document.createElement('p');
    const albumArtist = document.createElement('p');
    const albumCover = document.createElement('img');

    albumCover.src = album.cover;
    albumName.innerHTML = album.name;
    fetchGenre(genreId).then((genre) => {
      albumGenre.innerHTML = genre.name;
    });

    fetchArtist(artistId).then((artist) => {
      albumArtist.innerHTML = artist.name;
    });

    album.tracks.forEach((track) => {
      const albumTrack = document.createElement('li');
      fetchTrack(track).then((t) => {
        albumTrack.innerHTML = t.name;
      });
      albumTrackList.appendChild(albumTrack);
    });

    albumCard.appendChild(albumName);
    albumCard.appendChild(albumCover);
    albumCard.appendChild(albumArtist);
    albumCard.appendChild(albumGenre);
    albumCard.appendChild(albumTrackList);
    main.appendChild(albumCard);
  })
  .catch((error) => error.message);
