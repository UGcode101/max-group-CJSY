import { Search } from "./Search";
import { ChosenGenres } from "./ChosenGenres";
import { Tracklist } from "./Tracklist";
import { useContext, useEffect, useState } from "react";
import { Generate } from "./Generate";
import { AuthContext } from "../App";

export const Threemix = ({ accessToken, setShowTracklistPage }) => {
  const [chosenGenres, setChosenGenres] = useState([]);
  const [tracklist, setTracklist] = useState();
  const auth = useContext(AuthContext);
  useEffect(() => {
    setShowTracklistPage(!!tracklist);
  }, [tracklist, setShowTracklistPage])
  const tracklistScreen = (
    <>
      <Tracklist tracklist={tracklist} />
    </>
  );
  const chooseGenresScreen = (
    <>
      <Search setChosenGenres={setChosenGenres} chosenGenres={chosenGenres} />
      <ChosenGenres
        setChosenGenres={setChosenGenres}
        chosenGenres={chosenGenres}
      />
      <Generate chosenGenres={chosenGenres} setTracklist={setTracklist} />
    </>
  );
  const loggedInFeatures = (
    <>{(!!tracklist && tracklistScreen) || chooseGenresScreen}</>
  );

  return (
    <>
      {auth.accessToken && loggedInFeatures}
    </>
  );
};
