import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import userService from '../services/UserService';

const UpdateDebitComponent = () => {
  const [id, setId] = useState('');
  const [debit, setDebit] = useState({"balance": 0});
  const [response, setResponse] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();

    userService.updateUserDebit(id, debit)
      .then((res) => {
        // Handle the response from the server
        setResponse(res.data);
      })
      .catch((error) => {
        // Handle any errors
        console.log(error);
      });
  };

  return (
    <div>
      <Form className="d-grid gap-2" style={{ margin: '5rem' }}>
        <Form.Group className="mb-3" controlId="formBasicId">
          <Form.Control
            value={id}
            onChange={(e) => setId(e.target.value)}
            type="text"
            placeholder="Enter ID"
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicDebit">
          <Form.Control
            value={debit.balance}
            onChange={(e) => setDebit({balance: e.target.value })}
            type="text"
            placeholder="Enter Debit Amount"
          />
        </Form.Group>

        <Button
          onClick={handleSubmit}
          variant="primary"
          type="submit"
          size="lg"
        >
          Update Debit
        </Button>
      </Form>

    
    </div>
  );
};

export default UpdateDebitComponent;
