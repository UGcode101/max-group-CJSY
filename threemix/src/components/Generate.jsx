import PropTypes from "prop-types";
export const Generate = (props) => {
  return (
    props.chosenGenres.length > 2 && (
      <>
        <button>Generate Playlist</button>
      </>
    )
  );
};

Generate.propTypes = {
  chosenGenres: PropTypes.array,
};
