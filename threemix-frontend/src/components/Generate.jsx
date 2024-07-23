import PropTypes from "prop-types";
import { generatePlaylist } from "../api/backendApi";
export const Generate = ({ chosenGenres, setPlaylist }) => {
  return (
    chosenGenres.length > 2 && (
      <>
        <button onClick={()=>setPlaylist(generatePlaylist(chosenGenres))}>Generate Playlist</button>
      </>
    )
  );
};

Generate.propTypes = {
  chosenGenres: PropTypes.array,
};
