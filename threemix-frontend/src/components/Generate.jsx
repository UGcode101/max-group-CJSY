import PropTypes from "prop-types";
import { generatePlaylist } from "../api/backendApi";
export const Generate = ({ chosenGenres, setPlaylist }) => {
  return (
    chosenGenres.length > 2 && (
      <>
        <div className="generate-button">
          <button onClick={() => generatePlaylist(chosenGenres, setPlaylist)}>
            Generate Playlist
          </button>
        </div>
      </>
    )
  );
};

Generate.propTypes = {
  chosenGenres: PropTypes.array,
};
