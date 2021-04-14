function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
    vars[key] = value;
  });
  return vars;
}

const genreId = getUrlVars()['id'];

async function fetchGenre() {
  const response = await fetch(`http://localhost:8082/genres/read/id/${genreId}`);

  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }

  const genre = response.json();
  return genre;
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
fetchAlbums().then((albums) => {
  fetchGenre(genreId).then((genre) => {
    const genreName = document.querySelector('.my-4');
    const genreRow = document.querySelector('.row');

    document.title = `Choonz | ${genre.name}`;
    genreName.innerHTML = genre.name;

    for (const album of genre.albums) {
      const cardContainer = document.createElement('div');
      const cardWrapper = document.createElement('div');
      const cardBody = document.createElement('div');
      const albumTitle = document.createElement('h4');
      const albumLink = document.createElement('a');

      for (let i = 0; i < albums.length; i++) {
        if (albums[i].id === album) {
          cardContainer.className = 'col-lg-4 col-sm-6 mb-4';
          cardWrapper.className = 'card h-100';
          cardBody.className = 'card-body';
          albumTitle.className = 'card-title';

          albumTitle.innerHTML = albums[i].name;
          albumLink.innerHTML = 'click here to view';
          albumLink.href = '/album?id=' + album;

          cardWrapper.appendChild(albumTitle);
          cardWrapper.appendChild(albumLink);
          cardWrapper.appendChild(cardBody);
          cardContainer.appendChild(cardWrapper);
          genreRow.appendChild(cardContainer);
        }
      }
    }
  });
});
