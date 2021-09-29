import axios from 'axios';

export default {

  findADestination(info) {
    return axios.post('/vacationPlan',info)
  },


}