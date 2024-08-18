import { blockSong, createPlaylist } from "../api/backendApi";
import { useContext, useState } from "react";
import { AuthContext } from "../App";

export const Playlist = ({playlist}) => {
  const [name, setName] = useState("New playlist");
  const auth = useContext(AuthContext);
  const tracks = playlist?.tracks;

  const trackList = tracks?.map(track => {
    return (
      <li key={track.id}>
        {track.name}
        {track.artists.map(a => ` - ${a.name}`)}
        <button onClick={() => blockSong(auth, track.id)}>block song</button>
      </li>
    );
  })
  return (
    <>
      {"This is the playlist page"}
      <ul>
        {trackList}
      </ul>
      Playlist name: <input value={name} onChange={(e) => setName(e.target.value)}/>
      <button onClick={() => createPlaylist(auth, name, "Generated by Threemix", tracks?.map(track => track.id))}>
        Export to Spotify
      </button>
    </>);
};
