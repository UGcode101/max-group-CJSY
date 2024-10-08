import PropTypes from "prop-types";
import { blockEntities, createPlaylist } from "../api/backendApi";
import { useContext, useMemo, useState } from "react";
import { AuthContext } from "../App";
import {
  BlockArtistIcon,
  BlockSongIcon,
  Clock,
  RemoveSongIcon,
  Undo,
} from "./Icons";
import { successPageId } from "../pages";

const undo = (title, className, id, list, setList) => (
  <Undo
    className={className}
    title={title}
    onClick={() => setList(list.filter((e) => e !== id))}
  />
);

export const Tracklist = ({ tracklist, chosenGenres, setCurrentPageId }) => {
  const [name, setName] = useState("New playlist");
  const [removedSongs, setRemovedSongs] = useState([]);
  const [blockedSongs, setBlockedSongs] = useState([]);
  const [blockedArtists, setBlockedArtists] = useState([]);
  const [isSaving, setIsSaving] = useState(false);
  const auth = useContext(AuthContext);
  const tracks = tracklist?.tracks;

  const trackList = useMemo(() => {
    const explicit = (
      <>
        <span className="explicit">E</span>
      </>
    );
    return tracks?.map((track, i) => {
      const duration = new Date(track.duration_ms);
      return (
        <tr key={track.id}>
          <td>{i + 1}</td>
          <td>
            <img
              src={track.album?.images?.[track.album?.images.length - 1].url}
            ></img>
          </td>
          <td className="track-artist-name">
            <div className="track-name">{track.name}</div>{" "}
            <div className="artist-name">
              {track.explicit && explicit}
              {track.artists?.map((a) => a.name).join(" - ")}
            </div>
          </td>
          <td className="album-name">{track.album?.name}</td>
          <td className="duration">
            {duration.getMinutes()}:
            {String(duration.getSeconds()).padStart(2, "0")}
          </td>
          <td>
            {/* First icon */}
            {removedSongs.includes(track.id) ? (
              undo(
                "Undo remove song",
                "undo-remove-song",
                track.id,
                removedSongs,
                setRemovedSongs
              )
            ) : (
              <RemoveSongIcon
                onClick={() => setRemovedSongs([...removedSongs, track.id])}
              />
            )}
            {/* Second icon */}
            {blockedSongs.includes(track.id) ? (
              undo(
                "Undo block song",
                "undo-block-song",
                track.id,
                blockedSongs,
                setBlockedSongs
              )
            ) : (
              <BlockSongIcon
                onClick={() => setBlockedSongs([...blockedSongs, track.id])}
              />
            )}
            {/* Third icon */}
            {blockedArtists.includes(track.artists?.[0].id) ? (
              undo(
                "Undo block artist",
                "undo-block-artist",
                track.artists?.[0].id,
                blockedArtists,
                setBlockedArtists
              )
            ) : (
              <BlockArtistIcon
                onClick={() =>
                  setBlockedArtists([...blockedArtists, track.artists?.[0].id])
                }
              />
            )}
          </td>
        </tr>
      );
    });
  }, [removedSongs, blockedSongs, blockedArtists, tracks]);
  console.log(tracklist);
  return (
    <>
      <div className="tracklist">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th></th>
              <th>Title</th>
              <th>Album</th>
              <th className="duration-header">
                <Clock />
              </th>
            </tr>
          </thead>
          <tbody>{trackList}</tbody>
        </table>
      </div>

      <div className="playlist-name">
        Playlist name:{" "}
        <input value={name} onChange={(e) => setName(e.target.value)} />
      </div>

      <div className="export-button">
        <button
          disabled={isSaving}
          onClick={() => {
            setIsSaving(true);
            createPlaylist(
              auth,
              name,
              `Generated by Threemix (${chosenGenres.join(", ")})`,
              tracks
                ?.map((track) => track.id)
                .filter((t) => !removedSongs.includes(t))
            )
              .then(() => setIsSaving(false))
              .then(() => setCurrentPageId(successPageId));
            blockEntities(auth, blockedSongs, blockedArtists);
          }}
        >
          Export to Spotify
        </button>
      </div>
    </>
  );
};

Tracklist.propTypes = {
  tracklist: PropTypes.object,
  chosenGenres: PropTypes.array,
  setCurrentPageId: PropTypes.func,
};
