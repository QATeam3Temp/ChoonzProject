async function fetchArtists() {
  const response = await fetch('http://localhost:8082/artists/read');

  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }

  const artists = await response.json();
  return artists;
}

fetchArtists()
  .then((artists) => {
    console.log(artists);
    const contentContainer = document.querySelector('.content');
    const artistsContainer = document.createElement('div');
    const artistsList = document.createElement('ul');

    artistsContainer.className = 'artists-container';
    artistsList.className = 'artists-list';

    artistsContainer.appendChild(artistsList);
    contentContainer.appendChild(artistsContainer);

    for (const artist of artists) {
      const artistListElement = document.createElement('li');
      const artistName = document.createElement('a');

      artistListElement.id = artist.id;

      artistName.innerHTML = artist.name;
      artistName.className = 'artist-link';
      artistName.href = 'src/main/resources/static/artistinfo.html';

      artistListElement.appendChild(artistName);
      artistsList.appendChild(artistListElement);
    }
  })
  .catch((error) => error.message);
