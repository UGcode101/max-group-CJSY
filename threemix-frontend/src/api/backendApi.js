import { getToken } from "./SpotifyApi";

export const generatePlaylist = (genres, setPlaylist) => {
  fetch(
    `http://localhost:8080/generateTrackList?chosenGenres=${genres.join(",")}`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then(setPlaylist)
    .catch((e) => console.log(e));
};
