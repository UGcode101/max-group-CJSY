import { createContext, useState } from 'react';
import './App.css'
import { ProfileHeader } from "./components/ProfileHeader";
import { Threemix } from './components/Threemix.jsx'
import { getToken } from './api/SpotifyApi.js';
import { ProfilePage } from './components/ProfilePage.jsx';
import { Spotify } from './components/Spotify.jsx';

export const AuthContext = createContext();

function App() {
  const [accessToken, setAccessToken] = useState(getToken());
  const [showProfilePage, setShowProfilePage] = useState(false);

  const threemix = (
    <>
      <Threemix />
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
      <AuthContext.Provider value={{ accessToken, setAccessToken }}>
        <div className="grid-container">
          <div className={`app-name-container ${smallClass}`}>
            <div className="app-name">
              <h1>THREEMIX</h1>
            </div>
            <div className="app-tagline">
              <h4>
                {" "}
                {tagline}
                <Spotify includeName />
              </h4>
            </div>
          </div>

          {(showProfilePage && profilePage) || threemix}

          <ProfileHeader
            setShowProfilePage={setShowProfilePage}
          />
        </div>
      </AuthContext.Provider>
    </>
  );
}

export default App
