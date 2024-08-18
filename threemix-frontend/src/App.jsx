import { useState } from 'react';
import './App.css'
import { ProfileHeader } from "./components/ProfileHeader";
import { Threemix } from './components/Threemix.jsx'
import { getToken } from './api/SpotifyApi.js';
import { ProfilePage } from './components/ProfilePage.jsx';
import { Spotify } from './components/Spotify.jsx';

function App() {
  const [accessToken, setAccessToken] = useState(getToken());
  const [showProfilePage, setShowProfilePage] = useState(false);

  if (accessToken !== getToken()) {
    setAccessToken(getToken());
  }

  const threemix = (
    <>
      <Threemix
        accessToken={accessToken}
      />
    </>
  );

  const profilePage = (
    <>
      <ProfilePage setShowProfilePage={setShowProfilePage}/>
    </>
  )

  const isSmall = showProfilePage;
  const smallClass = isSmall ? "small" : ""
  const tagline = isSmall
    ? "for "
    : "A multi-genre playlist generator created for ";

  return (
    <>
      <div className="grid-container">
        <div className={`app-name-container ${smallClass}`}>
          <div className="app-name">
            <h1>THREEMIX</h1>
          </div>
          <div className="app-tagline">
            <h4> { tagline }<Spotify includeName /></h4>
          </div>
        </div>

        {(showProfilePage && profilePage) || threemix}

        <ProfileHeader
          accessToken={accessToken}
          setAccessToken={setAccessToken}
          setShowProfilePage={setShowProfilePage}
        />
      </div>
    </>
  );
}

export default App
