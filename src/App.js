import React from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import ListUserComponent from './components/ListUserComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import UpdateUserComponent from './components/UpdateUserComponent';
import ListIdComponent from './components/ListIdComponent';
import UpdateCreditComponent from './components/UpdateCreditComponent';
import UpdateDebitComponent from './components/UpdateDebitComponent';
// import createUsersComponent from './components/createUsersComponent';
// import ViewEmployeeComponent from './components/ViewEmployeeComponent';

function App() {
  return (
    <div>
        <Router>
              {/* <HeaderComponent /> */}
                <div className="container">
                    <Routes> 
                          <Route path = "/" exact element = { <ListUserComponent /> }></Route>
                          <Route path = "/users" element = { <ListUserComponent /> }></Route>
                          <Route path = "/create" element = { < UpdateUserComponent/>}> </Route>
                          <Route path = "/id" element = { < ListIdComponent/>}> </Route>
                          <Route path = "/credit" element = { < UpdateCreditComponent/>}> </Route>
                          <Route path = "/debit" element = { < UpdateDebitComponent/>}> </Route>
                    </Routes>
                </div>
              {/* <FooterComponent /> */}
        </Router>
    </div>
    
  );
}

export default App;
