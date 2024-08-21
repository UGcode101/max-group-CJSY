import { createContext, useState } from 'react';
import './App.css'
import { ProfileHeader } from "./components/ProfileHeader";
import { Threemix } from './components/Threemix.jsx'
import { getToken } from './api/SpotifyApi.js';
import { ProfilePage } from './components/ProfilePage.jsx';
import { Spotify } from './components/Spotify.jsx';
import { ProfilePageId, SuccessPageId, TracklistPageId } from './pages.js';

export const AuthContext = createContext();

function App() {
  const [accessToken, setAccessToken] = useState(getToken());
  const [currentPageId, setCurrentPageId] = useState();
 
  const threemix = (
    <>
      <Threemix
        setCurrentPageId={setCurrentPageId}
      />
    </>
  );

  const profilePage = (
    <>
      <ProfilePage setShowProfilePage={() => setCurrentPageId()} />
    </>
  );

  const successPage = (
    <>
      Success!
    </>
  );

  const isSmall = [ProfilePageId, SuccessPageId].includes(currentPageId);
  const smallClass = isSmall ? "small" : ""
  const tagline = isSmall
    ? "for "
    : "A multi-genre playlist generator created for ";

  const getCurrentPage = () => {
    switch (currentPageId) {
      case ProfilePageId:
        return profilePage;
      case TracklistPageId:
        return threemix;
      case SuccessPageId:
        return successPage;
    }
    return undefined;
  };

  
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

            {accessToken && (getCurrentPage() || threemix)}

            <ProfileHeader
              setShowProfilePage={() => setCurrentPageId(ProfilePageId)}
            />
          </div>
        </AuthContext.Provider>
      </>
    );
  }

  export default App

