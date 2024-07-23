import { Search } from "./Search";
import { ChosenGenres } from "./ChosenGenres";
import { Playlist } from "./Playlist";
import { useState } from "react";
import { Generate } from "./Generate";
import { Profile } from "./Profile";
import { isLoggedIn } from "../api/SpotifyApi";

export const Threemix = () => {
  const [chosenGenres, setChosenGenres] = useState([]);
  const [playlist, setPlaylist] = useState();
  const playlistScreen = (
    <>
      <Playlist />
    </>
  );
  const chooseGenresScreen = (
    <>
      <Search setChosenGenres={setChosenGenres} chosenGenres={chosenGenres} />
      <ChosenGenres
        setChosenGenres={setChosenGenres}
        chosenGenres={chosenGenres}
      />
      <Generate chosenGenres={chosenGenres} setPlaylist={setPlaylist} />
    </>
  );
  const loggedInFeatures = (
    <>{(!!playlist && playlistScreen) || chooseGenresScreen}</>
  );

  
  return (
    <>
      {isLoggedIn() && loggedInFeatures}
      <Profile />
    </>
  );
};
