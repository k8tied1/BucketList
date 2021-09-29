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
          :items="destinationListDisplay"
          :fields="fieldsComp"
        >
          <template #cell(actions)="row" v-if="allowEdit">
            <b-button
              size="sm"
              @click="deleteDestination(row.item.destinationId)"
              class="btn btn-danger gimmeSomeSpace"
            >
              <i class="fa fa-trash"></i> Delete
            </b-button>
            <b-button
              v-if="row.item.completed=='Not Yet!'"
              size="sm"
              @click="markCompleted(row.item.destinationId)"
              class="btn btn-success gimmeSomeSpace"
            >
              I did it!
            </b-button>
          </template>
        </b-table>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import destService from "@/services/DestinationService.js";
export default {
  props: ["destinationListDisplay", "allowEdit"],
  data() {
    return {
      fields: [
        {
          key: "locationName",
          sortable: true,
          label: "Destination",
        },
        {
          key: "expense",
          sortable: true,
          label: "$$$",
        },
        {
          key: "toy",
          sortable: true,
          label: "Time of year",
        },
        {
          key: "ppReq",
          sortable: true,
          label: "Passport required?",
        },
        {
          key: "completed",
          sortable: true,
          label: "Completed",
        },
        { key: "actions", label: "Actions" },
      ],
    };
  },
  computed: {
    fieldsComp() {
      if (this.allowEdit) {
        return this.fields;
      } else {
        let noEdit = this.fields;
        noEdit.pop(); //remove the Actions
        noEdit.push({
          key: "user",
          sortable: false,
          label: "From the BucketList Of:",
        });
        return noEdit;
      }
    },
  },
  methods: {
    deleteDestination(destinationId) {
      destService
        .deleteDestination(destinationId)
        .then((response) => {
          console.log("success " + response.status);
          this.$store.commit("REMOVE_DESTINATION", destinationId);
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error deleting destination: " + response.status);
        });
    },
    markCompleted(destinationId) {
      let dest = this.$store.state.destinationList.find(d=>d.destinationId ==destinationId);
      if (!dest){
        alert('Unknown error');
        return;
      }
      dest.completed = true;
      destService
        .updateDestination(dest)
        .then((response) => {
          console.log("success " + response.status);
        })
        .catch((error) => {
          const response = error.response;
          console.log("Error updating destination: " + response.status);
        });
    },
  },
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