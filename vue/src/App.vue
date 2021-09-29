<template>
  <div id="app">
    <div id="nav">
      <div class="left">
        <router-link class="whiteText" v-bind:to="{ name: 'destinations' }"
          >My Bucket List</router-link
        >&nbsp;|&nbsp;
        <router-link class="whiteText" v-bind:to="{ name: 'vacationplans' }"
          >Plan A Vacation</router-link
        >
      </div>
      <div>
        <p v-if="$store.state.token != ''" class="whiteText">
          Welcome, {{ $store.state.user.username }}
        </p>
        <router-link
          class="whiteText"
          v-bind:to="{ name: 'manageaccess' }"
          v-if="$store.state.token != ''"
          >Manage Access</router-link
        > &nbsp;|&nbsp;
        <router-link
          class="whiteText"
          v-bind:to="{ name: 'logout' }"
          v-if="$store.state.token != ''"
          >Logout</router-link
        >
      </div>
    </div>
    <router-view />
  </div>
</template>

<style>
body {
  background: url("./assets/b8.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center center;
  background-attachment: fixed;
}

#nav {
  background-color: #3b434b;
  display: flex;
  justify-content: space-between;
  color: antiquewhite;
}

.greyBackground {
  background-color: #3b434b;
  opacity: 0.9;
}

.greyText {
  color: #2b3136;
}

.lightBackground {
  background-color: lightgray;
}
.whiteText {
  color: white;
}

.gimmeSomeSpace {
  margin: 10px;
}
.mainSection {
  margin: auto;
  width: 70%;
  border: 3px solid #3b434b;
  padding: 10px;
}
</style>
