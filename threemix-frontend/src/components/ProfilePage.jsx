import PropTypes from "prop-types";

export const ProfilePage = ({ setCurrentPageId }) => {
  return (
    <>
      <div className="profile-content">
        This is the profile page
        <button onClick={() => setCurrentPageId()}>x</button>
      </div>
    </>
  );
};

ProfilePage.propTypes = {
  setCurrentPageId: PropTypes.func,
};
