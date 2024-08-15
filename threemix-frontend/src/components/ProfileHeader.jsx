import { useEffect, useState } from "react";
import { getCurrentUserProfile } from "../api/SpotifyApi";
import { logout } from "../api/backendApi";

export const ProfileHeader = ({accessToken, setAccessToken}) => {
  const [profileInfo, setProfileInfo] = useState();
  useEffect(() => {
    if (accessToken) {
      getCurrentUserProfile(setProfileInfo);

    }
  }, [accessToken, setProfileInfo])

  const loginLink = (
    <a href="http://localhost:8080/login">Login with Spotify</a>
  );
  
  const profileFragment = profileInfo && profileInfo.images ? (
    <>
      <div className="profile-header">
        {profileInfo.display_name}
        <img
          className="profile-pic"
          src={profileInfo.images[profileInfo.images.length - 1]?.url}
        />
        <button onClick={() => { logout(setAccessToken) }}>Log out</button>
      </div>
    </>
  ) : null;

  return (
    <>
      {accessToken ? profileFragment : loginLink}
    </>
  );
};
