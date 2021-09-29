import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem('token')
const currentUser = JSON.parse(localStorage.getItem('user'));

if(currentToken != null) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  state: {
    token: currentToken || '',
    user: currentUser || {},
    expenseLevels: [],
    timeOfYearOptions: [],
    destinationList:[],
  },
  mutations: {
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem('user',JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      state.token = '';
      state.user = {};
      axios.defaults.headers.common = {};
    },
    SET_TOY_OPTIONS(state,toy_options){
      state.timeOfYearOptions =  toy_options;
    },
    SET_EXPENSE_LEVELS(state,expenseLevels){
      state.expenseLevels =  expenseLevels;
    },
    SET_DESTINATION_LIST(state,dest_list){
      state.destinationList =  dest_list;
    },
    REMOVE_DESTINATION(state,destId){
      state.destinationList =  state.destinationList.filter(d=>d.destinationId != destId);
    },
    ADD_DESTINATION(state,dest){
      state.destinationList.push(dest);
    },

  }
})
