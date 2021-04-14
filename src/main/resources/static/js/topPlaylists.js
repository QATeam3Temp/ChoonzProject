async function fetchPlaylists() {
  const response = await fetch(`http://localhost:8082/playlists/read/`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const playlists = response.json();
  return playlists;
}

fetchPlaylists().then((playlists) => {
  console.log(playlists);
  const main = document.querySelector('.container');
  const listGroup = document.createElement('ul');
  listGroup.className = 'list-group';

  playlists.forEach((playlist) => {
    const listItem = document.createElement('li');

    listItem.className = 'list-group-item d-flex justify-content-between align-items-center"';

    listItem.innerHTML = `<a href='/playlist?id=${playlist.id}'>${playlist.name}</a>`;

    listGroup.appendChild(listItem);
  });

  main.appendChild(listGroup);
});
