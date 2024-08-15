export const getGenreSeeds = (setAllGenres) => {
  const headers = { Authorization: `Bearer ${getToken()}` };
  fetch("https://api.spotify.com/v1/recommendations/available-genre-seeds", { headers })
    .then((r) => r.json())
    .then(b => b.genres)
    .then(setAllGenres)
    .catch((e) => console.log(e));
}

const getToken = () => document.cookie
  .split("; ")
  .find((row) => row.startsWith("accessToken="))
  ?.split("=")[1];  

export const isLoggedIn = () => !!getToken();

export const getCurrentUserProfile = (setProfileInfo) => {
  const headers = {Authorization: `Bearer ${getToken()}`} 
  fetch("https://api.spotify.com/v1/me", { headers })
    .then((r) => r.json())
    .then(setProfileInfo)
    .catch((e) => console.log(e));
} 