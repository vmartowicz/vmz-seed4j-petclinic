<script setup lang="ts">
import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { fullName } from '@/owner/domain/Owner';
import { searchOutcome, type OwnersPage } from '@/owner/domain/OwnersPage';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const owners = inject(OWNERS_REPOSITORY);
const router = useRouter();

const lastName = ref('');
const page = ref<OwnersPage | undefined>();
const notFound = ref(false);
const error = ref('');

const search = async (pageNumber: number): Promise<void> => {
  error.value = '';
  notFound.value = false;

  try {
    const outcome = searchOutcome(await owners.search(lastName.value, pageNumber));

    if (outcome.kind === 'single') {
      await router.push({ name: 'OwnerDetails', params: { ownerId: outcome.owner.id } });
    } else if (outcome.kind === 'not-found') {
      page.value = undefined;
      notFound.value = true;
    } else {
      page.value = outcome.page;
    }
  } catch (e) {
    error.value = (e as Error).message;
  }
};
</script>

<template>
  <section>
    <h1>Find owners</h1>
    <form class="clinic-form" @submit.prevent="search(0)">
      <label>
        Last name
        <input v-model="lastName" data-selector="last-name" placeholder="Beginning of the last name" />
      </label>
      <div class="clinic-actions">
        <button type="submit" data-selector="search">Find owner</button>
        <router-link class="clinic-button" :to="{ name: 'OwnerCreation' }">Add owner</router-link>
      </div>
    </form>

    <p v-if="notFound" data-selector="not-found">Owner not found.</p>
    <p v-if="error" class="clinic-error" data-selector="error">{{ error }}</p>

    <template v-if="page">
      <table data-selector="owners">
        <thead>
          <tr>
            <th>Name</th>
            <th>Address</th>
            <th>City</th>
            <th>Telephone</th>
            <th>Pets</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="owner in page.owners" :key="owner.id" data-selector="owner">
            <td>
              <router-link :to="{ name: 'OwnerDetails', params: { ownerId: owner.id } }">{{ fullName(owner) }}</router-link>
            </td>
            <td>{{ owner.address }}</td>
            <td>{{ owner.city }}</td>
            <td>{{ owner.telephone }}</td>
            <td>{{ owner.pets.map(pet => pet.name).join(', ') }}</td>
          </tr>
        </tbody>
      </table>
      <div class="clinic-pagination">
        <button type="button" data-selector="previous" :disabled="page.currentPage === 0" @click="search(page.currentPage - 1)">
          Previous
        </button>
        <span data-selector="page">Page {{ page.currentPage + 1 }} / {{ page.pageCount }}</span>
        <button type="button" data-selector="next" :disabled="page.currentPage + 1 >= page.pageCount" @click="search(page.currentPage + 1)">
          Next
        </button>
      </div>
    </template>
  </section>
</template>
