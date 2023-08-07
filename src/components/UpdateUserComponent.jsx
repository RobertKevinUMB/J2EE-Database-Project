import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link, useNavigate } from 'react-router-dom';
import userService from '../services/UserService';

const UpdateUserComponent = () => {
  const [users, setUsers] = useState([
    {
      id: '',
      name: '',
      lastname: '',
      email: '',
      balance: ''
    }
  ]);

  let history = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
  
    // Filter out empty user forms
    const updatedUsers = users.filter(user => user.id !== '');
  
    // Perform any necessary validation before saving the users
  
    userService.createUser(updatedUsers);
  
    history('/');
  };

  const handleAddUser = () => {
    setUsers([...users, {
      id: "",
      name: '',
      lastname: '',
      email: '',
      balance: ''
    }]);
  };

  const handleUserChange = (index, field, value) => {
    setUsers(prevUsers => {
      const updatedUsers = [...prevUsers];
      updatedUsers[index][field] = value;
      return updatedUsers;
    });
  };
  

  return (
    <div>
      {users.map((user, index) => (
        <Form key={index} className="d-grid gap-2" style={{ margin: '5rem' }} >
          <Form.Group className="mb-3" controlId={`formBasicEmail-${index}`}>
            <Form.Control
              value={user.id}
              onChange={(e) => handleUserChange(index, 'id', e.target.value)}
              type="text"
              placeholder="Enter Id"
            />
            </Form.Group>
          <Form.Group className="mb-3" controlId={`formBasicEmail-${index}`}>
            <Form.Control
              value={user.name}
              onChange={(e) => handleUserChange(index, 'name', e.target.value)}
              type="text"
              placeholder="Enter First Name"
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId={`formBasicEmail-${index}`}>
            <Form.Control
              value={user.lastname}
              onChange={(e) => handleUserChange(index, 'lastname', e.target.value)}
              type="text"
              placeholder="Enter Last Name"
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId={`formBasicEmail-${index}`}>
            <Form.Control
              value={user.email}
              onChange={(e) => handleUserChange(index, 'email', e.target.value)}
              type="text"
              placeholder="Enter Email"
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId={`formBasicPassword-${index}`}>
            <Form.Control
              value={user.balance}
              onChange={(e) => handleUserChange(index, 'balance', e.target.value)}
              type="text"
              placeholder="Enter Balance"
            />
          </Form.Group>
        </Form>
      ))}

      <Button onClick={handleAddUser} variant="secondary" size="lg">
        Add User
      </Button>

      <Button onClick={handleSubmit} variant="primary" type="submit" size="lg">
        Update
      </Button>
    </div>
  );
};

export default UpdateUserComponent;



