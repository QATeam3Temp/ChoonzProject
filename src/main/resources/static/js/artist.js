function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}

async function fetchArtist() {
  const artistId = getUrlVars()['id'];
  const response = await fetch(`http://localhost:8082/artists/read/id/${artistId}`);

  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }

  const artist = await response.json();
  return artist;
}

async function fetchAlbums() {
  const response = await fetch(`http://localhost:8082/albums/read`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const albums = await response.json();
  return albums;
}

fetchAlbums()
  .then((albums) => {
    fetchArtist()
      .then((artist) => {
        console.log(albums);
        const artistName = document.querySelector('.my-4');
        const albumRow = document.querySelector('.row');

        artistName.innerHTML = artist.name;
        document.title = `Choonz Tracks | ${artist.name}`;
        console.log(artist);

        for (const album of artist.albums) {
          const cardContainer = document.createElement('div');
          const cardWrapper = document.createElement('div');
          const cardBody = document.createElement('div');
          const albumTitle = document.createElement('h4');

          for (let i = 0; i < albums.length; i++) {
            if (albums[i].id === album) {
              cardContainer.className = 'col-lg-4 col-sm-6 mb-4';
              cardWrapper.className = 'card h-100';
              cardBody.className = 'card-body';
              albumTitle.className = 'card-title';

              albumTitle.innerHTML = albums[i].name;
              // alubumTitle.href = '/album?id=' + album;

              cardWrapper.appendChild(albumTitle);
              cardWrapper.appendChild(cardBody);
              cardContainer.appendChild(cardWrapper);
              albumRow.appendChild(cardContainer);
            }
          }
        }
      })
      .catch((error) => error.message);
  })
  .catch((error) => error.message);
