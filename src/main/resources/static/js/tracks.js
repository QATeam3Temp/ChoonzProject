async function fetchTrack() {
  const response = await fetch(`http://localhost:8082/tracks/read/`);
  if (!response.ok) {
    const message = `Something has gone wrong: ${response.status}`;
    throw new Error(message);
  }
  const tracks = response.json();
  return tracks;
}

fetchTrack().then((tracks) => {
  console.log(tracks);
  const main = document.querySelector('.container');
  const listGroup = document.createElement('ul');
  listGroup.className = 'list-group';

  tracks.forEach((track) => {
    const listItem = document.createElement('li');

    listItem.className = 'list-group-item d-flex justify-content-between align-items-center"';

    listItem.innerHTML = `<a href='/track?id=${track.id}'>${track.name}</a>`;

    listGroup.appendChild(listItem);
  });

  main.appendChild(listGroup);
});
