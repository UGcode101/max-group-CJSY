import { useEffect, useState } from "react";
import { getCurrentUserProfile, isLoggedIn } from "../api/SpotifyApi";

export const ProfileHeader = () => {
  const [profileInfo, setProfileInfo] = useState();
  useEffect(() => {
    if (isLoggedIn()) {
      getCurrentUserProfile(setProfileInfo);

    }
  }, [isLoggedIn(), setProfileInfo])

  const loginLink = (
    <a href="http://localhost:8080/login">Login with Spotify</a>
  );
  
  const profileFragment = profileInfo && profileInfo.images ? (
    <>
      <div className="profile-header">
        {profileInfo.display_name}
        <img
          className="profile-pic"
          src={profileInfo.images[profileInfo.images?.length - 1].url}
        />
      </div>
    </>
  ) : null;

  return (
    <>
      {isLoggedIn() ? profileFragment : loginLink}
    </>
  );
};
