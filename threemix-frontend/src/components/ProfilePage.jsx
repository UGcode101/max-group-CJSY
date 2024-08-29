import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import {
  getBlockedArtists,
  getBlockedSongs,
  unblockArtist,
  unblockSong,
} from "../api/backendApi.js";
import { AuthContext } from "../App.jsx";
import { getArtists, getSongs } from "../api/SpotifyApi.js";

export const ProfilePage = ({ setCurrentPageId }) => {
  const auth = useContext(AuthContext);
  const [blockedSongs, setBlockedSongs] = useState([]);
  const [blockedArtists, setBlockedArtists] = useState([]);
  useEffect(() => {
    getBlockedSongs(
      auth,
      (songs) => songs && getSongs(auth, songs, setBlockedSongs)
    );
    getBlockedArtists(
      auth,
      (artists) => artists && getArtists(auth, artists, setBlockedArtists)
    );
  }, [auth, setBlockedSongs, setBlockedArtists]);
  const htmlifyArtist = (artist) => (
    <>
      <td>
        <img src={artist?.images?.[artist?.images.length - 1].url} />
      </td>
      <td>{artist.name}</td>
      <td>
        <button
          onClick={() => {
            unblockArtist(auth, artist.id);
            setBlockedArtists(blockedArtists.filter((a) => a.id !== artist.id));
          }}
        >
          x
        </button>
      </td>
    </>
  );

  const htmlifyTrack = (track) => (
    <>
      <td>
        <img src={track.album?.images?.[track.album?.images.length - 1].url} />
      </td>
      <td>{track.name}</td>{" "}
      <td>
        <button
          onClick={() => {
            unblockSong(auth, track.id);
            setBlockedSongs(blockedSongs.filter((t) => t.id !== track.id));
          }}
        >
          x
        </button>
      </td>
    </>
  );

  return (
    <>
      <div className="profile-content">
        This is the profile page
        <button onClick={() => setCurrentPageId()}>x</button>
      </div>
      <div className="blocked-artists">
        <table>
          <thead>
            <th colSpan={3}>Blocked Artists</th>
          </thead>
          <tbody>
            {blockedArtists?.map((artist) => (
              <tr key={artist.id}>{htmlifyArtist(artist)}</tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="blocked-songs">
        <table>
          <thead>
            <th colSpan={3}>Blocked Songs</th>
          </thead>
          <tbody>
            {blockedSongs?.map((song) => (
              <tr key={song.id}>{htmlifyTrack(song)}</tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

ProfilePage.propTypes = {
  setCurrentPageId: PropTypes.func,
};
