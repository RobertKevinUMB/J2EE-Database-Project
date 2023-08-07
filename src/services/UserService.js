import axios from 'axios';

const URL = "http://localhost:8080/musely-takehome/rest/users";

class UserService {
  
  getUsers() {
    return axios.get(URL + "/all");
  }

  createUser(user) {
    console.log(user);
    return axios.post(URL + "/create", user);
  }

  getUserById(id) {
    return axios.get(URL + "/" + id);
  }

  updateUserCredit(id, credit) {
    console.log(credit);
    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
    };
    return axios.post(URL + "/" + id + "/credit",  credit );
  }

  updateUserDebit(id, debit) {
    console.log(debit);
    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
    };
    return axios.post(URL + "/" + id + "/debit",  debit );
  }
}

export default new UserService();
