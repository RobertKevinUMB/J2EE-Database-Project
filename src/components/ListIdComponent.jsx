import React, { useState } from 'react';
import userService from '../services/UserService';

const ListIdComponent = () => {
  const [userId, setUserId] = useState('');
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

  const handleIdChange = (e) => {
    setUserId(e.target.value);
  };

  const handleGetUser = (e) => {
    e.preventDefault();
    

    userService.getUserById(userId)
      .then((res) => {
        setUser(res.data);
        
        setError(null);
      })
      .catch((error) => {
        setUser(null);
        setError('Error retrieving user');
      });
  };

  return (
    <div>
      <h2>Get User by ID</h2>
      <form onSubmit={handleGetUser}>
        <div className="mb-3">
          <label htmlFor="userId" className="form-label">
            User ID:
          </label>
          <input
            type="text"
            className="form-control"
            id="userId"
            value={userId}
            onChange={handleIdChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Get User
        </button>
      </form>
      {error && <div className="alert alert-danger mt-3">{error}</div>}
      {user && (
        <div className="mt-3">
          <h4>User Details:</h4>
          <p>First Name: {user.name}</p>
          <p>Last Name: {user.lastname}</p>
          <p>Email: {user.email}</p>
          <p>Balance: {user.balance}</p>
        </div>
      )}
    </div>
  );
};

export default ListIdComponent;
