import React, { useState, useEffect } from 'react';
import userService from '../services/UserService';
import { Link } from 'react-router-dom';

const ListUserComponent = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    userService.getUsers().then((res) => {
      console.log(res.data);

      const transformedData = res.data.map((user) => ({
        id: user.id,
        firstName: user.name,
        lastName: user.lastname,
        emailId: user.email,
        balance: user.balance,
      }));

      setUsers(transformedData);
    });
  }, []);

  return (
    <div>
      <h2 className="text-center">Users List</h2>
      <div className="row">
        <Link className="btn btn-primary" to="/create">
          Create User
        </Link>
        <Link className="btn btn-primary" to="/credit">
          Update User Credit
        </Link>
        <Link className="btn btn-primary" to="/debit">
          Update User Debit
        </Link>
        <Link className="btn btn-primary" to="/id">
          Get User by ID
        </Link>
      </div>
      <br />
      <div className="row">
        <table className="table table-striped table-bordered">
          <thead>
            <tr>
              <th>ID</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email Id</th>
              <th>Balance</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.emailId}</td>
                <td>{user.balance}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ListUserComponent;
