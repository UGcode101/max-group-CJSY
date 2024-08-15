export const generatePlaylist = (genres, setPlaylist) => {
  fetch(
    `http://localhost:8080/generateTrackList?chosenGenres=${genres.join(",")}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then(r => r.json())
    .then(setPlaylist)
    .catch((e) => console.log(e));
};

export const blockSong = (songId) => {
  fetch(
    `http://localhost:8080/api/blockedSong?songId=${songId}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then((r) => r.json())
    .then(console.log)
    .catch((e) => console.log(e));
}

export const createPlaylist = (name, description, tracks) => {
  fetch(
    `http://localhost:8080/exportPlaylist?name=${name}&description=${description}&trackIds=${tracks.join(",")}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then(r => r.json())
    .then(console.log)
    .catch(e => console.log(e));
}

export const refresh = () => {
  fetch("http://localhost:8080/refresh", {
    method: "POST",
    credentials: "include",
  })
    .then((r) => r.json())
    .then(console.log)
    .catch((e) => console.log(e));  
}