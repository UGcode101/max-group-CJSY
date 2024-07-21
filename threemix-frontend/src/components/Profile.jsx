import { isLoggedIn } from "../api/SpotifyApi";

export const Profile = () => {
  
  const loginLink = (
    <a href="http://localhost:8080/login">Login with Spotify</a>
  );
  return (
    <>
      <h2></h2>
      {
        isLoggedIn()?"You are logged in" : loginLink
      }
    </>
  );
};
