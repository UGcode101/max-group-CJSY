// TODO: Create search component with autocomplete functionality that generates a list of genres from which to choose.
import PropTypes from "prop-types";
import { useMemo } from "react";
import { getGenreSeeds } from "../api/SpotifyApi";
import { useState } from "react";

export const Search = (props) => {
  const allGenres = useMemo(getGenreSeeds, []);
  const almostAllGenres = useMemo(
    () => allGenres.filter((g) => !props.chosenGenres.includes(g)),
    [allGenres, props.chosenGenres]
  );
  const [searchTerm, setSearchTerm] = useState("");
  const htmlifyOption = (option) => (
    <div>
      <button
        onClick={() => props.setChosenGenres([...props.chosenGenres, option])}
      >
        {option}
      </button>
    </div>
  );

  const options = useMemo(() => {
    console.log(searchTerm);
    if (
      !searchTerm ||
      searchTerm.length === 0 ||
      props.chosenGenres.length > 2
    ) {
      return [];
    }
    if (searchTerm.length === 1) {
      return almostAllGenres.filter((g) => g.startsWith(searchTerm));
    }
    return almostAllGenres.filter((g) => g.includes(searchTerm));
  }, [almostAllGenres, searchTerm]);

  return (
    <>
      <label>Find a genre: </label>
      <input
        type="search"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      {options.slice(0, 9).map(htmlifyOption)}
    </>
  );
};

Search.propTypes = {
  chosenGenres: PropTypes.array,
  setChosenGenres: PropTypes.func,
};
