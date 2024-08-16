// : Create search component with autocomplete functionality that generates a list of genres from which to choose.
import PropTypes from "prop-types";
import { useEffect, useMemo } from "react";
import { getGenreSeeds } from "../api/SpotifyApi";
import { useState } from "react";

export const Search = ({ setChosenGenres, chosenGenres }) => {
  const [allGenres, setAllGenres] = useState([]);
  useEffect(() => getGenreSeeds(setAllGenres), [setAllGenres]);
  const almostAllGenres = useMemo(
    () => allGenres.filter((g) => !chosenGenres.includes(g)),
    [allGenres, chosenGenres]
  );
  const [searchTerm, setSearchTerm] = useState("");
  const htmlifyOption = (option) => (
    <span>
      <button onClick={() => {
        setChosenGenres([...chosenGenres, option]);
        setSearchTerm("");
      }}>
        {option}
      </button>
    </span>
  );

  const options = useMemo(() => {
    console.log(searchTerm);
    if (!searchTerm || searchTerm.length === 0 || chosenGenres.length > 2) {
      return [];
    }
    if (searchTerm.length === 1) {
      return almostAllGenres.filter((g) => g.startsWith(searchTerm));
    }
    return almostAllGenres.filter((g) => g.includes(searchTerm));
  }, [almostAllGenres, searchTerm, chosenGenres.length]);

  return (
    chosenGenres.length < 3 && (
    <>
      <div className="search">
        <label>Find a genre: </label>
        <input
          type="search"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      <div className="search-options">
        {options.slice(0, 9).map(htmlifyOption)}
      </div>
      </>
    )
  );
};

Search.propTypes = {
  chosenGenres: PropTypes.array,
  setChosenGenres: PropTypes.func,
};
