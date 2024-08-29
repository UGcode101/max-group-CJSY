import { refresh } from "./backendApi";

// Mock
export const getGenreSeeds = (auth, setAllGenres) => {
  setAllGenres([
    "acoustic",
    "afrobeat",
    "alt-rock",
    "alternative",
    "ambient",
    "anime",
    "black-metal",
    "bluegrass",
    "blues",
    "bossanova",
    "brazil",
    "breakbeat",
    "british",
    "cantopop",
    "chicago-house",
    "children",
    "chill",
    "classical",
    "club",
    "comedy",
    "country",
    "dance",
    "dancehall",
    "death-metal",
    "deep-house",
    "detroit-techno",
    "disco",
    "disney",
    "drum-and-bass",
    "dub",
    "dubstep",
    "edm",
    "electro",
    "electronic",
    "emo",
    "folk",
    "forro",
    "french",
    "funk",
    "garage",
    "german",
    "gospel",
    "goth",
    "grindcore",
    "groove",
    "grunge",
    "guitar",
    "happy",
    "hard-rock",
    "hardcore",
    "hardstyle",
    "heavy-metal",
    "hip-hop",
    "holidays",
    "honky-tonk",
    "house",
    "idm",
    "indian",
    "indie",
    "indie-pop",
    "industrial",
    "iranian",
    "j-dance",
    "j-idol",
    "j-pop",
    "j-rock",
    "jazz",
    "k-pop",
    "kids",
    "latin",
    "latino",
    "malay",
    "mandopop",
    "metal",
    "metal-misc",
    "metalcore",
    "minimal-techno",
    "movies",
    "mpb",
    "new-age",
    "new-release",
    "opera",
    "pagode",
    "party",
    "philippines-opm",
    "piano",
    "pop",
    "pop-film",
    "post-dubstep",
    "power-pop",
    "progressive-house",
    "psych-rock",
    "punk",
    "punk-rock",
    "r-n-b",
    "rainy-day",
    "reggae",
    "reggaeton",
    "road-trip",
    "rock",
    "rock-n-roll",
    "rockabilly",
    "romance",
    "sad",
    "salsa",
    "samba",
    "sertanejo",
    "show-tunes",
    "singer-songwriter",
    "ska",
    "sleep",
    "songwriter",
    "soul",
    "soundtracks",
    "spanish",
    "study",
    "summer",
    "swedish",
    "synth-pop",
    "tango",
    "techno",
    "trance",
    "trip-hop",
    "turkish",
    "work-out",
    "world-music",
  ]);
}

//Real
export const getGenreSeeds2 = (auth, setAllGenres) => {
  const headers = { Authorization: `Bearer ${auth.accessToken}` };
  const url =
    "https://api.spotify.com/v1/recommendations/available-genre-seeds";
  fetch(url, { headers })
    .then((r) => {
      if (r.status === 401) {
        return refresh(auth).then(() => {
          const newHeaders = { Authorization: `Bearer ${auth.accessToken}` };
          return fetch(url, { headers: newHeaders });
        });
      }
      return r;
    })
    .then((r) => r.json())
    .then(b => b.genres ?? [])
    .then(setAllGenres)
    .catch((e) => console.log(e));
}

export const getCurrentUserProfile = (auth, setProfileInfo) => {
  const headers = { Authorization: `Bearer ${auth.accessToken}` }
  const url = "https://api.spotify.com/v1/me";
  fetch(url, { headers })
    .then(r => {
      if (r.status === 401) {
        return refresh(auth).then(() => {
          const newHeaders = { Authorization: `Bearer ${auth.accessToken}` };
          return fetch(url, { headers: newHeaders });
        });
      }
      return r;
    })
    .then((r) => r.json())
    .then(setProfileInfo)
    .catch((e) => console.log(e));
}

export const getArtists = (auth, artistIds, setArtists) => {
  const headers = { Authorization: `Bearer ${auth.accessToken}` }
   console.log(artistIds, artistIds.join(","));
 const url = `https://api.spotify.com/v1/artists?ids=${artistIds.join(",")}`;
  fetch(url, { headers })
  .then(r => {
    if (r.status === 401) {
      return refresh(auth).then(() => {
        const newHeaders = { Authorization: `Bearer ${auth.accessToken}` };
        return fetch(url, { headers: newHeaders });
      });
    }
    return r;
  })
  .then((r) => r.json())
  .then(b => b.artists)
  .then(setArtists)
  .catch((e) => console.log(e));
}

export const getSongs = (auth, songIds, setSongs) => {
  const headers = { Authorization: `Bearer ${auth.accessToken}` }
  const url = `https://api.spotify.com/v1/tracks?ids=${songIds.join(",")}`;
  fetch(url, { headers })
  .then(r => {
    if (r.status === 401) {
      return refresh(auth).then(() => {
        const newHeaders = { Authorization: `Bearer ${auth.accessToken}` };
        return fetch(url, { headers: newHeaders });
      });
    }
    return r;
  })
  .then((r) => r.json())
  .then(b => b.tracks)
  .then(setSongs)
  .catch((e) => console.log(e));
}
