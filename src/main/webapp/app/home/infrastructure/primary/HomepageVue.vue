<template>
  <section>
    <h1>Welcome to the Petclinic</h1>
    <p>Register owners and their pets, schedule visits and find our veterinarians.</p>
    <div class="clinic-actions">
      <router-link class="clinic-button" :to="{ name: 'OwnersSearch' }">Find owners</router-link>
      <router-link class="clinic-button" :to="{ name: 'Vets' }">Our veterinarians</router-link>
    </div>
  </section>
</template>

<script lang="ts">
export default {
  name: 'HomepageVue',
};
</script>
