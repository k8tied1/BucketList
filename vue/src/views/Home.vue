<template>
  <div class="home greyBackground whiteText">
    <h1>Welcome! You need to make plans for FUN!</h1>
    <h2>Use this site to keep track of destinations on your bucket list and randomly pick one if you need help deciding what's next!</h2>
    <h2>         
      <router-link v-bind:to="{ name: 'login' }">Login</router-link>
      or
      <router-link v-bind:to="{ name: 'register' }">Register </router-link>
       to get started!</h2>

  </div>
</template>

<script>
export default {
  name: "home"
};
</script>

<style scoped>
.home{
    margin: auto;
  width: 70%;
  border: 3px solid #3b434b;
  padding: 10px;
}
</style>