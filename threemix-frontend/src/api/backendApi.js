const checkError = (auth, response) => {
  if (response.status === 401 && auth.accessToken) {
    auth.setAccessToken(undefined);
  }
  return response;
};

export const getToken = () =>
  document.cookie
    .split("; ")
    .find((row) => row.startsWith("accessToken="))
    ?.split("=")[1];

export const generateTracklist3 = (auth, genres, setTracklist) =>
  setTracklist({
    tracks: [
      {
        id: "id1",
        name: "Dois Rois",
        artists: [{ name: "Skank", id: "a2" }],
        album: {
          name: "Radiola",
          images: [
            {
              url: "https://i.scdn.co/image/ab67616d00004851703adc9ba56d69b5aba64ba1",
            },
          ],
        },
        duration_ms: 67890,
      },
      {
        id: "id2",
        name: "STREET",
        artists: [{ name: "NGHTMRE", id: "a1" }],
        album: {
          name: "Street",
          images: [
            {
              url: "https://i.scdn.co/image/ab67616d000048513e1e6191bce005c6f2a17557",
            },
          ],
        },
        duration_ms: 56789,
      },
      {
        id: "id3",
        name: "Dois Rois",
        artists: [{ name: "Skank", id: "a2" }],
        album: {
          name: "Radiola",
          images: [
            {
              url: "https://i.scdn.co/image/ab67616d00004851703adc9ba56d69b5aba64ba1",
            },
          ],
        },
        duration_ms: 67890,
      },
      {
        id: "id4",
        name: "STREET",
        artists: [{ name: "NGHTMRE", id: "a1" }],
        album: {
          name: "Street",
          images: [
            {
              url: "https://i.scdn.co/image/ab67616d000048513e1e6191bce005c6f2a17557",
            },
          ],
        },
        duration_ms: 56789,
      },
    ],
  });


export const generateTracklist = (auth, genres, setTracklist) =>
  fetch(
    `http://localhost:8080/generateTrackList?chosenGenres=${genres.join(",")}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then((r) => checkError(auth, r))
    .then((r) => r.json())
    .then(setTracklist)
    .catch((e) => console.log(e));

export const blockSong = (auth, songId) =>
  fetch(`http://localhost:8080/api/blockedSong?songId=${songId}`, {
    method: "POST",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const blockArtist = (auth, artistId) =>
  fetch(`http://localhost:8080/api/blockedArtist?artistId=${artistId}`, {
    method: "POST",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const unblockSong = (auth, songId) =>
  fetch(`http://localhost:8080/api/blockedSong/${songId}`, {
    method: "DELETE",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const unblockArtist = (auth, artistId) =>
  fetch(`http://localhost:8080/api/blockedArtist/${artistId}`, {
    method: "DELETE",
    credentials: "include",
  })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const getBlockedSongs = (auth, setBlockedSongs) =>
  fetch("http://localhost:8080/api/blockedSong", { credentials: "include" })
  .then((r) => checkError(auth, r))
  .then((r) => r.json())
  .then(setBlockedSongs)
  .catch((e) => console.log(e));

export const getBlockedArtists = (auth, setBlockedArtists) =>
  fetch("http://localhost:8080/api/blockedArtist", { credentials: "include" })
  .then((r) => checkError(auth, r))
  .then((r) => r.json())
  .then(setBlockedArtists)
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

export const blockEntities = (auth, songs, artists) => {
  artists.map(artistId => blockArtist(auth, artistId));
  songs.map(songId => blockSong(auth, songId));
}

export const refresh = (auth) =>
  fetch("http://localhost:8080/refresh", {
    method: "POST",
    credentials: "include",
  })
    .then(r => {
      auth.setAccessToken(getToken());
      return r;
    })
    .then((r) => checkError(auth, r))
    .catch((e) => console.log(e));

export const logout = (callback) =>
  fetch("http://localhost:8080/logout", {
    method: "POST",
    credentials: "include",
  })
    .then(callback)
    .catch((e) => console.log(e));
