<template>
  <div class="container" v-if="!loading">
    <div class="gimmeSomeSpace">
      <b-button
        class="greyBackground"
        v-on:click="showCreate = true"
        v-if="showCreate == false"
        >Add A Destination to My Bucket List</b-button
      >
      
      <router-link class="whiteText gimmeSomeSpace" v-bind:to="{ name: 'vacationplans' }">
        <b-button class="greyBackground">Plan a Vacation</b-button>
      </router-link>
    </div>
    <create-destination v-if="showCreate" v-on:clicked="showCreate = false" />

    <destination-list
      v-if="destinationListDisplay.length > 0"
      v-bind:destinationListDisplay="destinationListDisplay"
      v-bind:allowEdit="true"
    />
  </div>
</template>

<script>
import CreateDestination from "@/components/CreateDestination.vue";
import DestinationList from "../components/DestinationList.vue";
import destService from "@/services/DestinationService.js";
export default {
  components: {
    CreateDestination,
    DestinationList,
  },
  data() {
    return {
      showCreate: false,
      loading: true,
    };
  },
  created() {
    this.populateDestinationList();
  },

  methods: {
    populateDestinationList() {
      destService
        .getDestinationList()
        .then((response) => {
          this.$store.commit("SET_DESTINATION_LIST", response.data);
          this.populateDescriptions();
          this.loading = false;
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error getting expense options: " + response);
        });
    },
    populateDescriptions() {
      if (
        this.$store.state.expenseLevels.length == 0 ||
        this.$store.state.timeOfYearOptions.length == 0
      ) {
        destService
          .getExpenseOptions()
          .then((response) => {
            this.$store.commit("SET_EXPENSE_LEVELS", response.data);
          })
          .catch((error) => {
            const response = error.response;
            console.log("Error getting expense options: " + response);
          });
        destService
          .getTimeOfYearOptions()
          .then((response) => {
            this.$store.commit("SET_TOY_OPTIONS", response.data);
          })
          .catch((error) => {
            const response = error.response;
            console.log("Error getting time of year options: " + response);
          });
      }
    },
    expenseDescription(id) {
      let e = this.$store.state.expenseLevels.find((item) => item.id == id);
      return e ? e.description : "";
    },
    timeOfYearDescription(id) {
      let d = this.$store.state.timeOfYearOptions.find((item) => item.id == id);
      return d ? d.description : "";
    },
  },
  computed: {
    destinationListDisplay() {
      return this.$store.state.destinationList.map((item) => {
        let newItem = {
          destinationId: item.destinationId,
          locationName: item.locationName,
          expense: this.expenseDescription(item.expense),
          toy: this.timeOfYearDescription(item.timeOfYear),
          ppReq: item.passportRequired ? "YES!! Get it." : "Nope",
          completed: item.completed ? "Hell Yeah!" : "Not Yet!",
          user: item.user_id,
        };
        return newItem;
      });
    },
  },
};
</script>

<style scoped>
.gimmeSomeSpace {
  margin: 10px;
}

</style>