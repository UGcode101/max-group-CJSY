const checkError = (auth, response) => {
  if (response.status === 401 && auth.accessToken) {
    console.log(auth.accessToken);
    auth.setAccessToken(null);
  }
  return response;
}

export const generatePlaylist = (auth, genres, setPlaylist) => fetch(
    `http://localhost:8080/generateTrackList?chosenGenres=${genres.join(",")}`,
    {
      method: "POST",
      credentials: "include",
    }
)
    .then(r => checkError(auth, r))
    .then(r => r.json())
    .then(setPlaylist)
    .catch((e) => console.log(e));

export const blockSong = (auth, songId) =>
  fetch(`http://localhost:8080/api/blockedSong?songId=${songId}`, {
    method: "POST",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .then((r) => r.json())
    .then(console.log)
    .catch((e) => console.log(e));

export const createPlaylist = (auth, name, description, tracks) =>
  fetch(
    `http://localhost:8080/exportPlaylist?name=${name}&description=${description}&trackIds=${tracks.join(
      ","
    )}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const refresh = (auth) =>
  fetch("http://localhost:8080/refresh", {
    method: "POST",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const logout = (auth, callback) =>
  fetch("http://localhost:8080/logout", {
    method: "POST",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .then(callback)
    .catch((e) => console.log(e));