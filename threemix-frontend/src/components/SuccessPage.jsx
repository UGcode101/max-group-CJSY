import PropTypes from "prop-types";

export const SuccessPage = ({ setCurrentPageId }) => {
  return (
    <>
      <div className="success-headline">
        <h2>Aww yiss!</h2>
      </div>

      <div className="success-tagline">
        <h3>
          Threemix just sent your playlist to Spotify. You can check it out
          there or create a new list.
        </h3>
      </div>

      <div className="listen-on-button">
        <a href="https://open.spotify.com/" target="_blank">
          <button>Listen on Spotify</button>
        </a>
      </div>

      <div className="new-playlist-button">
        <button onClick={() => setCurrentPageId()}>New Threemix playlist</button>
      </div>
    </>
  );
};

SuccessPage.propTypes = {
  setCurrentPageId: PropTypes.func,
};

