<template>
  <div class="greyBackground whiteText mainsection">
    <h1>Users Who Can Select Me For Vacation Plans</h1>
    <user-list></user-list>

    <label class="typo__label">Allow Users to Select Me</label>
    <multiselect
      v-model="addUserAccess"
      track-by="id"
      label="description"
      placeholder="Guests' bucket lists will be included in random destination selection"
      :options="guestsFiltered"
      :multiple="true"
      open-direction="bottom"
    >
    </multiselect>
    <b-button>
      <b-button class="button" v-on:click="grantAccess" variant="primary"
        >Grant Access</b-button
      >
    </b-button>
  </div>
</template>

<script>
import UserList from "../components/UserList.vue";
import userService from "@/services/UserService.js";
import Multiselect from "vue-multiselect";

export default {
  components: { UserList, Multiselect },
  data() {
    return {
      guests: [],
      usersAlreadyWithAccess: [],
      addUserAccess: [],
    };
  },
  created() {
    this.populateUsers();
    userService
      .getUsersWhoSeeMe()
      .then((response) => (this.usersAlreadyWithAccess = response.data));
  },
  computed: {
    guestsFiltered() {
      //keep everything not in usersAlreadywithAccess
      return this.guests.filter((g) => {
        return this.usersAlreadyWithAccess.findIndex((u) => u.id == g.id) < 0;
      });
    },
  },
  methods: {
    populateUsers() {
      userService
        .getAllUsers()
        .then((response) => {
          this.guests = response.data.map((user) => {
            let guest = {};
            (guest.id = user.id), (guest.description = user.username);
            return guest;
          });
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error getting expense options: " + response);
        });
    },
    grantAccess() {
      console.log("granting access to users");

      this.addUserAccess.forEach((guest) => {
        console.log("Giving access to " + guest.id);
        userService.grantUserAccess(guest.id).then((response) => {
          console.log(response.status + " access granted to " + guest.id);
          this.$router.push("/manage-access");
        });
      });
    },
  },
};
</script>

<style>
</style>