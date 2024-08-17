import { refresh } from "./backendApi";

export const getGenreSeeds = (setAllGenres) => {
  const headers = { Authorization: `Bearer ${getToken()}` };
  const url =
    "https://api.spotify.com/v1/recommendations/available-genre-seeds";
  fetch(url, { headers })
    .then((r) => {
      if (r.status === 401) {
        return refresh().then(() => {
          const newHeaders = { Authorization: `Bearer ${getToken()}` };
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

export const getToken = () => document.cookie
  .split("; ")
  .find((row) => row.startsWith("accessToken="))
  ?.split("=")[1];  

export const getCurrentUserProfile = (setProfileInfo) => {
  const headers = { Authorization: `Bearer ${getToken()}` } 
  const url = "https://api.spotify.com/v1/me";
  fetch(url, { headers })
    .then(r => {
      if (r.status === 401) {
        return refresh().then(() => {
          const newHeaders = { Authorization: `Bearer ${getToken()}` }; 
          return fetch(url, { headers: newHeaders });
        });
      };
      return r;
    })
    .then((r) => r.json())
    .then(setProfileInfo)
    .catch((e) => console.log(e));
} 