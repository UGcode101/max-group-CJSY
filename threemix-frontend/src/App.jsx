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
        <div className="app-name-container">
          <div className="app-name">
            <h1>THREEMIX</h1>
          </div>
          <div className="app-tagline">
            <h3>A multi-genre playlist generator created for Spotify</h3>
          </div>
        </div>

        <Threemix accessToken={accessToken} />
        <ProfileHeader
          accessToken={accessToken}
          setAccessToken={setAccessToken}
        />
      </div>
    </>
  );
}

export default App
