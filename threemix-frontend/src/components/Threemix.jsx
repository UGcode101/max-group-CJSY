import { Search } from "./Search";
import { ChosenGenres } from "./ChosenGenres";
import { Playlist } from "./Playlist";
import { useState } from "react";
import { Generate } from "./Generate";

export const Threemix = () => {
  const [chosenGenres, setChosenGenres] = useState([]);
  return (
    <>
      <Search setChosenGenres={setChosenGenres} chosenGenres={chosenGenres} />
      <ChosenGenres
        setChosenGenres={setChosenGenres}
        chosenGenres={chosenGenres}
      />
      <Generate chosenGenres={chosenGenres} />
      <Playlist />
    </>
  );
};
