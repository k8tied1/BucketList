<template>
  <div class="greyBackground whiteText">
    <b-form v-on:submit.prevent="addDestination">
      <b-form-group
        id="input-group-1"
        label="Destination:"
        label-for="input-1"
       
      >
        <b-form-input
          id="input-1"
          v-model="destination.locationName"
          placeholder="St Somewhere"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Expense:" label-for="input-3">
        <b-form-select id="input-3" v-model="destination.expense" required>
          <option
            v-for="option in $store.state.expenseLevels"
            v-bind:key="option.id"
            v-bind:value="option.id"
          >
            {{ option.description }}
          </option></b-form-select
        >
      </b-form-group>

      <b-form-group
        id="input-group-4"
        label="Time of Year:"
        label-for="input-4"
      >
        <b-form-select id="input-4" v-model="destination.timeOfYear" required>
          <option
            v-for="option in $store.state.timeOfYearOptions"
            v-bind:key="option.id"
            v-bind:value="option.id"
          >
            {{ option.description }}
          </option></b-form-select
        >
      </b-form-group>

      <b-form-checkbox
        id="checkbox-1"
        v-model="destination.passportRequired"
        name="checkbox-1"
      >
        Need a passport!
      </b-form-checkbox>

      <b-button class="button" type="submit" variant="primary"
        >Add It!</b-button
      >
      <b-button class="button" v-on:click="reset" variant="danger"
        >Cancel</b-button
      >
    </b-form>
  </div>
</template>

<script>
import destService from "@/services/DestinationService.js";

export default {
  data() {
    return {
      destination: {
        locationName: "",
        passportRequired: false,
        expense: 0,
        timeOfYear: 0,
      },
    };
  },
  methods: {
    addDestination() {
      destService
        .createDestination(this.destination)
        .then((response) => {  
          console.log('Successfully added '+response.status)        
          this.$store.commit("ADD_DESTINATION",response.data)
          this.reset();
        })
        .catch((err) => {
          console.log('Error adding: '+err.status)
        });
    },
    reset() {
      this.destination = {
        locationName: "",
        passportRequired: false,
        expense: 0,
        timeOfYear: 0,
      };
      this.$emit('clicked')
    },
  },

  created() {
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
};
</script>

<style scoped>
.button {
  margin: 10px;
}

div{
  margin: 20px;
}


</style>