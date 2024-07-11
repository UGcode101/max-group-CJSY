import PropTypes from "prop-types";
export const ChosenGenres = ({ setChosenGenres, chosenGenres }) => {
  const htmlifyGenre = (genre) => (
    <>
      <div>
        {genre}
        <button className="fill"
          onClick={() =>
            setChosenGenres(chosenGenres.filter((g) => g !== genre))
          }
        >
          x
        </button>
      </div>
    </>
  );

  return (
   chosenGenres.length > 0 &&
      <>
      <h3>Chosen genres</h3>

      {chosenGenres.map(htmlifyGenre)}
    </>
  );
};

ChosenGenres.propTypes = {
  chosenGenres: PropTypes.array,
  setChosenGenres: PropTypes.func,
};
