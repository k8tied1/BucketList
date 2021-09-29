<template>
<div>
    <h1>Admin Page    </h1>
    <div v-if="$store.state.user.authorities[0].name=='ROLE_ADMIN'">
        <h1> I'll allow it </h1>
    </div>
    <div v-else> Nothing to see here</div>
 
</div>
</template>

<script>
export default {

}
</script>

<style>

</style>