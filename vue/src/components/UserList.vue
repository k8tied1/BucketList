<template>
  <div id="app" class="container dList">
    <b-row>
      <b-col>
        <b-table
          striped
          dark
          hover
          outlined
          stacked="sm"
          :items="users"
          :fields="fields"
        >
          <template #cell(actions)="row" >
            <b-button
              size="sm"
              @click="removeUserAccess(row.item.username)"
              class="btn btn-danger gimmeSomeSpace"
            >
              <i class="fa fa-trash"></i> Delete
            </b-button>
           
          </template>
        </b-table>
      </b-col>
    </b-row>

  </div>
</template>

<script>
import userService from "@/services/UserService.js";
export default {

  data() {
    return {
      users: [],
      fields: [
        {
          key: "username",
          sortable: true,
          label: "Username",
        },
        {
          key: "email",
          sortable: true,
          label: "Email",
        },
       
        { key: "actions", label: "Actions" },
      ],
    };
  },
  created() {
      userService.getUsersWhoSeeMe()
      .then(response=> this.users = response.data)
  },
  methods: {
    removeUserAccess(username) {
      userService
        .removeUserAccess(username)
        .then((response) => {
          console.log("success " + response.status);
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error removing access: " + response.status);
        });
    },
  }
};
</script>

<style scoped>
.icon {
  height: 25px;
}
.b-table {
  opacity: 0.8;
}
</style>