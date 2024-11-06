import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import AuthService from '../services/auth.service';
import Modal from './Profile-Edit-Modal';

const Profile = () => {
  const [redirect, setRedirect] = useState(null);
  const [userReady, setUserReady] = useState(false);
  const [currentUser, setCurrentUser] = useState({ username: '' });
  const [showModal, setShowModal] = useState(false);
  const [updatedUsername, setUpdatedUsername] = useState('');

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (!user) {
      setRedirect('/home');
    } else {
      setCurrentUser(user);
      setUserReady(true);
    }
  }, []);

  const handleShow = () => {
    setUpdatedUsername(currentUser.username);
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleChange = (event) => {
    setUpdatedUsername(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log('Updated Username:', updatedUsername);
    setCurrentUser((prevUser) => ({
      ...prevUser,
      username: updatedUsername
    }));
    setShowModal(false);
  };

  if (redirect) {
    return <Navigate to={redirect} />;
  }

  return (
    <div className="container">
      {userReady && (
        <div>
          <header className="jumbotron">
            <h3>
              Welcome <strong>{currentUser.username}</strong>
            </h3>
            <button onClick={handleShow} className='btn btn-outline-info mt-2'>Edit Profile</button>
          </header>
          <p>
            <strong>Token:</strong>{" "}
            {currentUser.accessToken.substring(0, 20)} ...{" "}
            {currentUser.accessToken.substr(currentUser.accessToken.length - 20)}
          </p>
          <p>
            <strong>Id:</strong>{" "}
            {currentUser.id}
          </p>
          <p>
            <strong>Email:</strong>{" "}
            {currentUser.email}
          </p>
          <strong>Authorities:</strong>
          <ul>
            {currentUser.roles && currentUser.roles.map((role, index) => (
              <li key={index}>{role}</li>
            ))}
          </ul>
         
          <Modal
            show={showModal}
            onClose={handleClose}
            onSubmit={handleSubmit}
            username={updatedUsername}
            onUsernameChange={handleChange}
          />
        </div>
      )}
    </div>
  );
};

export default Profile;
