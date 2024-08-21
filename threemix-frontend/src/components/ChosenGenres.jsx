import PropTypes from "prop-types";
import { RemoveGenreIcon } from "./Icons";
export const ChosenGenres = ({ setChosenGenres, chosenGenres }) => {
  const htmlifyGenre = (genre, i) => (
    <>
      <span className={`circle number${i + 1}`}>{i + 1} </span>
      <span className="chosen-genre">{genre} </span>
      <div className="x">
        <title>remove genre</title>
        <button
          className="x"
          onClick={() =>
            setChosenGenres(chosenGenres.filter((g) => g !== genre))
          }
        >
          <RemoveGenreIcon />
        </button>
      </div>
    </>
  );

  return (
    chosenGenres.length > 0 && (
      <>
        <div className="chosen-genres">
          <div className="chosen-genres-headline">
            <h3>Chosen genres</h3>
          </div>

          {chosenGenres.map(htmlifyGenre)}
        </div>
      </>
    )
  );
};

ChosenGenres.propTypes = {
  chosenGenres: PropTypes.array,
  setChosenGenres: PropTypes.func,
};
