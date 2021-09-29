import axios from 'axios';

export default {

  getUsers() {
    return axios.get('/users')
  },

  getAllUsers() {
    return axios.get('/allusers')
  },

  getUsersWhoSeeMe(){
    return axios.get('/userswhocanseeme')
  },

  removeUserAccess(username){
    return axios.delete('/users/revoke/'+username)
  },

  grantUserAccess(user_id){
    return axios.post('/users/grant_access/'+ user_id)
  }


}