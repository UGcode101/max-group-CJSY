// : Create search component with autocomplete functionality that generates a list of genres from which to choose.
import PropTypes from "prop-types";
import { useContext, useEffect, useMemo, useRef } from "react";
import { getGenreSeeds } from "../api/SpotifyApi";
import { useState } from "react";
import { AuthContext } from "../App";

export const Search = ({ setChosenGenres, chosenGenres }) => {
  const [allGenres, setAllGenres] = useState([]);
  const searchRef = useRef();
  const auth = useContext(AuthContext);
  useEffect(() => getGenreSeeds(auth, setAllGenres), [setAllGenres, auth]);
  const almostAllGenres = useMemo(
    () => allGenres.filter((g) => !chosenGenres.includes(g)),
    [allGenres, chosenGenres]
  );
  const [searchTerm, setSearchTerm] = useState("");
  const htmlifyOption = (option) => (
    <span key={option}>
      <button onClick={() => {
        setChosenGenres([...chosenGenres, option]);
        setSearchTerm("");
        searchRef?.current?.focus();
      }}>
        {option}
      </button>
    </span>
  );

  const options = useMemo(() => {
    if (!searchTerm || searchTerm.length === 0 || chosenGenres.length > 2) {
      return [];
    }
    if (searchTerm.length === 1) {
      return almostAllGenres.filter((g) => g.startsWith(searchTerm));
    }
    return almostAllGenres.filter((g) => g.includes(searchTerm));
  }, [almostAllGenres, searchTerm, chosenGenres.length]);

  const searchResults = options.length === 0 && searchTerm.length > 0 ? (
    <div className="no--search-results">
      Ope! &quot;{searchTerm}&quot; is not an available genre.
    </div>
  ): (
    <div className="search-options">
      {options.slice(0, 9).map(htmlifyOption)}
    </div>
  );

  return (
    chosenGenres.length < 3 && (
    <>
      <div className="search">
        <label>Find a genre: </label>
        <input
            type="search"
            ref={searchRef}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value.toLowerCase())}
        />
        </div>
        {searchResults}
      </>
    )
  );
};

Search.propTypes = {
  chosenGenres: PropTypes.array,
  setChosenGenres: PropTypes.func,
};
