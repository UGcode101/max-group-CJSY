import { useContext, useEffect, useState } from "react";
import { getCurrentUserProfile } from "../api/SpotifyApi";
import { logout } from "../api/backendApi";
import { AuthContext } from "../App";

export const ProfileHeader = ({setShowProfilePage}) => {
  const [profileInfo, setProfileInfo] = useState();
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const auth = useContext(AuthContext);
  useEffect(() => {
    if (auth.accessToken) {
      getCurrentUserProfile(auth, setProfileInfo);

    }
  }, [setProfileInfo, auth])

  const loginLink = (
    <div className="login">
      <a href="http://localhost:8080/login">Login with Spotify</a>
    </div>
  );
  
  const dropdown = (
    <div className="dropdown" onMouseDown={(e) => e.preventDefault()}>
      <button onClick={() => {
        setDropdownVisible(false);
        setShowProfilePage(true);
      }}>Profile</button>
      <button
        className="logout-button"
        onClick={() => {
          auth.setAccessToken();
          logout(auth.setAccessToken);
          setProfileInfo();
          setShowProfilePage(false);
        }}
      >
        Log out
      </button>
    </div>
  );

  const profileFragment =
    auth.accessToken && profileInfo && (
      <>
        <div className="profile-header">{profileInfo.display_name}</div>
        <div className="profile-pic">
          <img
            className="circle-pic"
            src={
              profileInfo.images?.[profileInfo.images?.length - 1]?.url ??
              `https://ui-avatars.com/api/?name=${profileInfo.display_name}&background=ff00d2&color=fff`
            }
            role="button"
            onClick={() => setDropdownVisible(true)}
            onBlur={() => setDropdownVisible(false)}
            tabIndex="0"
          />
        </div>
        {dropdownVisible && dropdown}
      </>
    );

  return (
    <>
      { profileFragment || !auth.accessToken && loginLink}
    </>
  );
};
