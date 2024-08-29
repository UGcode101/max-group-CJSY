import PropTypes from "prop-types";
import { generateTracklist } from "../api/backendApi";
import { useContext } from "react";
import { AuthContext } from "../App";
export const Generate = ({ chosenGenres, setTracklist }) => {
  const auth = useContext(AuthContext);
  return (
    chosenGenres.length > 2 && (
      <>
        <div className="generate-button">
          <button
            onClick={() => generateTracklist(auth, chosenGenres, setTracklist)}
          >
            Generate Playlist
          </button>
        </div>
      </>
    )
  );
};

Generate.propTypes = {
  chosenGenres: PropTypes.array,
  setTracklist: PropTypes.func,
};
