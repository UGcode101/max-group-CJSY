import { useState } from 'react';
import './App.css'
import { ProfileHeader } from "./components/ProfileHeader";
import { Threemix } from './components/Threemix.jsx'
import { getToken } from './api/SpotifyApi.js';

function App() {
  const [accessToken, setAccessToken] = useState(getToken());

  if (accessToken !== getToken()) {
    setAccessToken(getToken());
  }

  return (
    <>
      <div className="grid-container">
        <div className="grid-item grid-item-1">grid item1</div>
        <div className="grid-item grid-item-1">grid item 2 </div>
        <div className="grid-item grid-item-1">grid item 3</div>
      </div>

      <div>
        <h1 className="app-name">THREEMIX</h1>
      </div>
      <div>
        <h3 className="app-tagline">
          A multi-genre playlist generator created for Spotify
        </h3>
      </div>
      <Threemix accessToken={accessToken} />
      <ProfileHeader accessToken={accessToken} setAccessToken={setAccessToken} />
    </>
  );
}

export default App
