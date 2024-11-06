import React from 'react';
import './Modal.css'; 
import axios from 'axios';

const Modal = ({ show, onClose, username, onUsernameChange }) => {
  if (!show) return null;

  const onSubmit = async (event) => {
    event.preventDefault();

    try {
      const user = JSON.parse(localStorage.getItem('user'));
      const token = user?.accessToken;

      const config = {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      };

      const response = await axios.put('/update-username', 
        { username },  // Тялото на заявката, което съдържа новото потребителско име
        config
      );

      if (response.status === 200) {
        console.log('Username updated successfully:', response.data);
        window.location.reload();  // Презарежда страницата
      }
    } catch (error) {
      console.error('Error updating username:', error);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <span className="modal-close" onClick={onClose}>×</span>
        <h2>Edit Profile</h2>
        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={onUsernameChange}
              required
            />
          </div>
          <button className="btn btn-success" type="submit">Save Changes</button>
          <button className="btn btn-secondary ml-4" type="button" onClick={onClose}>Cancel</button>
        </form>
      </div>
    </div>
  );
};

export default Modal;
