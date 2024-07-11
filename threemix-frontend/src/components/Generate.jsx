import PropTypes from "prop-types";
export const Generate = ({ chosenGenres }) => {
  return (
    chosenGenres.length > 2 && (
      <>
        <button>Generate Playlist</button>
      </>
    )
  );
};

Generate.propTypes = {
  chosenGenres: PropTypes.array,
};
