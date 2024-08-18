import { Search } from "./Search";
import { ChosenGenres } from "./ChosenGenres";
import { Playlist } from "./Playlist";
import { useContext, useState } from "react";
import { Generate } from "./Generate";
import { AuthContext } from "../App";

export const Threemix = () => {
  const [chosenGenres, setChosenGenres] = useState([]);
  const [playlist, setPlaylist] = useState();
  const auth = useContext(AuthContext);
  const playlistScreen = (
    <>
      <Playlist playlist={playlist} />
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
      {auth.accessToken && loggedInFeatures}
    </>
  );
};
