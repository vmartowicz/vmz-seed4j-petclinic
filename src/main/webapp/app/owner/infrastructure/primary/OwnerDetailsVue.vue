<script setup lang="ts">
import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { fullName, type Owner } from '@/owner/domain/Owner';
import { Loader } from '@/shared/loader/infrastructure/primary/Loader';
import { onMounted, ref } from 'vue';

const props = defineProps<{ ownerId: string }>();

const owners = inject(OWNERS_REPOSITORY);
const owner = ref(Loader.loading<Owner>());

onMounted(() => {
  owners
    .get(props.ownerId)
    .then(result => (owner.value = Loader.loaded(result)))
    .catch((error: Error) => (owner.value = Loader.error(error)));
});
</script>

<template>
  <section>
    <p v-if="owner.isLoading">Loading…</p>
    <p v-else-if="owner.isInError" class="clinic-error" data-selector="error">{{ owner.error.message }}</p>
    <template v-else>
      <h1>Owner information</h1>
      <table data-selector="owner">
        <tbody>
          <tr>
            <th>Name</th>
            <td data-selector="owner-name">{{ fullName(owner.value) }}</td>
          </tr>
          <tr>
            <th>Address</th>
            <td>{{ owner.value.address }}</td>
          </tr>
          <tr>
            <th>City</th>
            <td>{{ owner.value.city }}</td>
          </tr>
          <tr>
            <th>Telephone</th>
            <td>{{ owner.value.telephone }}</td>
          </tr>
        </tbody>
      </table>
      <div class="clinic-actions">
        <router-link class="clinic-button" :to="{ name: 'OwnerEdition', params: { ownerId } }">Edit owner</router-link>
        <router-link class="clinic-button" :to="{ name: 'PetCreation', params: { ownerId } }">Add new pet</router-link>
      </div>

      <h2>Pets and visits</h2>
      <p v-if="owner.value.pets.length === 0">No pet yet.</p>
      <table v-else>
        <tbody>
          <tr v-for="pet in owner.value.pets" :key="pet.id" data-selector="pet">
            <td>
              <strong data-selector="pet-name">{{ pet.name }}</strong>
              <div>Birth date: {{ pet.birthDate }}</div>
              <div>Type: {{ pet.type }}</div>
              <div class="clinic-actions">
                <router-link :to="{ name: 'PetEdition', params: { ownerId, petId: pet.id } }">Edit pet</router-link>
                <router-link :to="{ name: 'VisitCreation', params: { ownerId, petId: pet.id } }">Add visit</router-link>
              </div>
            </td>
            <td>
              <p v-if="pet.visits.length === 0">No visit.</p>
              <table v-else>
                <thead>
                  <tr>
                    <th>Visit date</th>
                    <th>Description</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="visit in pet.visits" :key="visit.id" data-selector="visit">
                    <td>{{ visit.date }}</td>
                    <td>{{ visit.description }}</td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
    </template>
  </section>
</template>
