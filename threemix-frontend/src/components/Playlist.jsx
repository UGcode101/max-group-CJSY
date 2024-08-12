import { blockSong } from "../api/backendApi";

export const Playlist = ({playlist}) => {
  const tracks = playlist?.tracks;

  const trackList = tracks?.map(track => {
    return (
      <li key={track.id}>
        {track.name}
        <button onClick={() => blockSong(track.id)}>block song</button>
      </li>
    );
  })
  return (
    <>
      {"This is the playlist page"}
      <ul>
        {trackList}
      </ul>
    </>);
};
