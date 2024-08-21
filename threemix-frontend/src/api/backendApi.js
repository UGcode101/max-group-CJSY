const checkError = (auth, response) => {
  if (response.status === 401 && auth.accessToken) {
    console.log(auth.accessToken);
    auth.setAccessToken(null);
  }
  return response;
};

export const generateTracklist = (auth, genres, setTracklist) =>
  setTracklist({
    tracks: [
      {
        id: "id1",
        name: "Dois Rois",
        artists: [{ name: "Skank" }],
        album: {
          name: "Radiola",
          url: "https://i.scdn.co/image/ab67616d00004851703adc9ba56d69b5aba64ba1",
          //          url: "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
        },
        duration_ms: 67890,
      },
      {
        id: "id2",
        name: "STREET",
        artists: [{ name: "NGHTMRE" }],
        album: {
          name: "Street",
          url: "	https://i.scdn.co/image/ab67616d000048513e1e6191bce005c6f2a17557",
        },
        duration_ms: 56789,
      },
      {
        id: "id3",
        name: "Dois Rois",
        artists: [{ name: "Skank" }],
        album: {
          name: "Radiola",
          url: "https://i.scdn.co/image/ab67616d00004851703adc9ba56d69b5aba64ba1",
          //          url: "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
        },
        duration_ms: 67890,
      },
      {
        id: "id4",
        name: "STREET",
        artists: [{ name: "NGHTMRE" }],
        album: {
          name: "Street",
          url: "	https://i.scdn.co/image/ab67616d000048513e1e6191bce005c6f2a17557",
        },
        duration_ms: 56789,
      },
    ],
  });


export const generateTracklist2 = (auth, genres, setTracklist) =>
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
