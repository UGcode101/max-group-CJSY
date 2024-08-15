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
      <div>
        <h1>THREEMIX</h1>
      </div>
      <Threemix accessToken={accessToken} />
      <ProfileHeader accessToken={accessToken} setAccessToken={setAccessToken} />
    </>
  );
}

export default App
