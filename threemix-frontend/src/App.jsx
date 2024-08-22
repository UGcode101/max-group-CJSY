import { createContext, useState } from "react";
import "./App.css";
import { ProfileHeader } from "./components/ProfileHeader";
import { Threemix } from "./components/Threemix.jsx";
import { getToken } from "./api/backendApi.js";
import { ProfilePage } from "./components/ProfilePage.jsx";
import { SuccessPage } from "./components/SuccessPage.jsx";
import { Spotify } from "./components/Spotify.jsx";
import { profilePageId, successPageId, tracklistPageId } from "./pages.js";

export const AuthContext = createContext();

function App() {
  const [accessToken, setAccessToken] = useState(getToken());
  const [currentPageId, setCurrentPageId] = useState();

  const threemix = (
    <>
      <Threemix setCurrentPageId={setCurrentPageId} />
    </>
  );

  const profilePage = (
    <>
      <ProfilePage setCurrentPageId={setCurrentPageId} />
    </>
  );

  const successPage = (
    <>
      <SuccessPage setCurrentPageId={setCurrentPageId} />
    </>
  );

  const isSmall = [profilePageId, tracklistPageId, successPageId].includes(
    currentPageId
  );
  const smallClass = isSmall ? "small" : "";
  const tagline = isSmall
    ? "for "
    : "A multi-genre playlist generator created for ";

  const getCurrentPage = () => {
    switch (currentPageId) {
      case profilePageId:
        return profilePage;
      case tracklistPageId:
        return threemix;
      case successPageId:
        return successPage;
    }
    return threemix;
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

          {accessToken && getCurrentPage()}

          <ProfileHeader setCurrentPageId={setCurrentPageId} />
        </div>
      </AuthContext.Provider>
    </>
  );
}

export default App;
