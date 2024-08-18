import PropTypes from "prop-types";
export const ChosenGenres = ({ setChosenGenres, chosenGenres }) => {
  const htmlifyGenre = (genre) => (
    <div key={genre}>
      {genre}
      <button
        className="fill"
        onClick={() =>
          setChosenGenres(chosenGenres.filter((g) => g !== genre))
        }
      >
        x
      </button>
    </div>
  );

  return (
    chosenGenres.length > 0 && (
      <>
        <div className="chosen-genres">
          <h3>Chosen genres</h3>

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
