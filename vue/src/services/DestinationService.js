import axios from 'axios';

export default {

  createDestination(dest) {
    return axios.post('/destination', dest)
  },
  updateDestination(dest) {
    return axios.put('/destination', dest)
  },
  getExpenseOptions() {
    return axios.get('/destination/expenseLevels')
  },
  getTimeOfYearOptions() {
    return axios.get('/destination/timeOfYearOptions')
  },
  getDestinationList() {
    return axios.get('/destinations')
  },
  deleteDestination(id) {
    return axios.delete(`/destination/${id}`);
  }

}