import { useEffect, useState } from "react";
import { getCurrentUserProfile } from "../api/SpotifyApi";
import { logout } from "../api/backendApi";

export const ProfileHeader = ({accessToken, setAccessToken}) => {
  const [profileInfo, setProfileInfo] = useState();
  const [dropdownVisible, setDropdownVisible] = useState(false);
  useEffect(() => {
    if (accessToken) {
      getCurrentUserProfile(setProfileInfo);

    }
  }, [accessToken, setProfileInfo])

  const loginLink = (
    <div className="login">
      <a href="http://localhost:8080/login">Login with Spotify</a>
    </div>
  );
  
  const dropdown = (
    <div className="dropdown" onMouseDown={(e) => e.preventDefault()}>
      <button>Profile</button>
      <button
        className="logout-button"
        onClick={() => {
          logout(setAccessToken);
        }}
      >
        Log out
      </button>
    </div>
  );

  const profileFragment =
    profileInfo && profileInfo.images ? (
      <>
        <div className="profile-header">{profileInfo.display_name}</div>
        <div className="profile-pic">
          <img
            className="circle-pic"
            src={
              profileInfo.images[profileInfo.images.length - 1]?.url ??
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
    ) : null;

  return (
    <>
      { profileFragment || !accessToken && loginLink}
    </>
  );
};
