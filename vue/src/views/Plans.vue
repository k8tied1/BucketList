<template>
  <div class="greyBackground whiteText mainSection" >
    <div
      class="alert alert-danger"
      role="alert"
      v-if="tried && destList.length == 0"
    >
      There's nothing in your or your guests' bucket lists that fit your
      requirements!
    </div>
        <div
      class="alert alert-warning"
      role="alert"
      v-if="showRequiredFieldsMessage"
    >
      How much you're willing to spend, when you want to go, and if you need a passport are required to properly pick a vacation destination for you!
    </div>
    <h1>Let's plan your vacation!</h1>
    <h6>
      A destination will be randomly selected from your and your guests bucket
      lists based on the criteria you select.
    </h6>
    <form
      v-if="destList.length == 0"
      v-on:submit.prevent="generateVacaPlan()"
      class="lightBackground greyText gimmeSomeSpace"
    >
      <div>
        <label class="typo__label">How much are you willing to spend?</label>
        <multiselect
          v-model="expense"
          track-by="id"
          label="description"
          placeholder="Select one or more"
          :options="$store.state.expenseLevels"
          :multiple="true"
          open-direction="bottom"
          required
        >
        </multiselect>
        <label class="typo__label">When do you want to go?</label>
        <multiselect
          v-model="timeOfYear"
          track-by="id"
          label="description"
          placeholder="Select one or more"
          :options="$store.state.timeOfYearOptions"
          :multiple="true"
          open-direction="bottom"
          required
        >
        </multiselect>
        <label class="typo__label"
          >Are you up for traveling with a passport?</label
        >
        <multiselect
          v-model="passport"
          track-by="id"
          label="description"
          placeholder="Select one or more"
          :options="passportOptions"
          :multiple="false"
          open-direction="bottom"
          required
        >
        </multiselect>
        <label class="typo__label">Who are you inviting to join you?</label>
        <multiselect
          v-model="invitedGuests"
          track-by="id"
          label="description"
          placeholder="Guests' bucket lists will be included in random destination selection"
          :options="guests"
          :multiple="true"
          open-direction="bottom"
        >
        </multiselect>
      </div>
      <div>
        <b-button class="button gimmeSomeSpace" type="submit" variant="primary"
          >Let's Do It!</b-button
        >
      </div>
    </form>
    <div v-if="destList.length > 0" class="mainsection">
      <h3>You should totally go to...</h3>
      <destination-list
        v-bind:destinationListDisplay="destinationListDisplay"
        v-bind:allowEdit="false"
      />
    </div>
  </div>
</template>

<script>
import Multiselect from "vue-multiselect";
import destService from "@/services/DestinationService.js";
import userService from "@/services/UserService.js";
import vacaService from "@/services/VPService.js";
import DestinationList from "../components/DestinationList.vue";

export default {
  components: { Multiselect, DestinationList },
  data() {
    return {
      expense: null,
      timeOfYear: null,
      guests: [],
      passport: null,
      invitedGuests: null,
      passportOptions: [
        { id: "yes", description: "Yes! It's already packed!" },
        { id: "no", description: "No way! Customs is scary" },
        { id: "meh", description: "I'm down either way!" },
      ],
      destList: [],
      tried: false,
      showRequiredFieldsMessage:false,
    };
  },
  created() {
    this.populateDescriptions();
    this.populateUsers();
  },
  methods: {
    generateVacaPlan() {
      let info = {};
      if (!this.expense || !this.timeOfYear || !this.passport) {
        this.showRequiredFieldsMessage=true;
        return;
      } else {
        this.showRequiredFieldsMessage = false;
      }
      info.expense = this.expense.map((e) => e.id);
      info.timeOfYear = this.timeOfYear.map((t) => t.id);
      if (this.invitedGuests) {
        info.guests = this.invitedGuests.map((g) => g.description);
      }
      info.passport = this.passport.id;
      vacaService
        .findADestination(info)
        .then((response) => {
          this.destList.push(response.data.destination);
          this.tried = true;
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error getting expense options: " + response);
          this.tried = true;
        });
    },
    populateUsers() {
      userService
        .getUsers()
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
      return this.destList.map((item) => {
        let newItem = {
          destinationId: item.destinationId,
          locationName: item.locationName,
          expense: this.expenseDescription(item.expense),
          toy: this.timeOfYearDescription(item.timeOfYear),
          ppReq: item.passportRequired ? "YES!! Get it." : "Nope",
          completed: item.completed ? "Hell Yeah!" : "Not Yet!",
          user: item.creatorName,
        };
        return newItem;
      });
    },
  },
};
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>


</style>