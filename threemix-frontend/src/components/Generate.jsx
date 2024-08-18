import PropTypes from "prop-types";
import { generatePlaylist } from "../api/backendApi";
import { useContext } from "react";
import { AuthContext } from "../App";
export const Generate = ({ chosenGenres, setPlaylist }) => {
  const auth = useContext(AuthContext);
  return (
    chosenGenres.length > 2 && (
      <>
        <div className="generate-button">
          <button onClick={() => generatePlaylist(auth, chosenGenres, setPlaylist)}>
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
