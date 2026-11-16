<script setup lang="ts">
import { inject } from '@/injections';
import { VETS_REPOSITORY } from '@/vet/application/VetsKeys';
import { specialtiesLabel, type VetsPage } from '@/vet/domain/Vet';
import { onMounted, ref } from 'vue';

const vets = inject(VETS_REPOSITORY);

const page = ref<VetsPage | undefined>();
const error = ref('');

const load = (pageNumber: number): void => {
  vets
    .list(pageNumber)
    .then(result => (page.value = result))
    .catch((e: Error) => (error.value = e.message));
};

onMounted(() => load(0));
</script>

<template>
  <section>
    <h1>Veterinarians</h1>
    <p v-if="error" class="clinic-error" data-selector="error">{{ error }}</p>
    <template v-if="page">
      <table data-selector="vets">
        <thead>
          <tr>
            <th>Name</th>
            <th>Specialties</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vet in page.vets" :key="vet.id" data-selector="vet">
            <td>{{ vet.firstName }} {{ vet.lastName }}</td>
            <td>{{ specialtiesLabel(vet) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="clinic-pagination">
        <button type="button" data-selector="previous" :disabled="page.currentPage === 0" @click="load(page.currentPage - 1)">
          Previous
        </button>
        <span data-selector="page">Page {{ page.currentPage + 1 }} / {{ page.pageCount }}</span>
        <button type="button" data-selector="next" :disabled="page.currentPage + 1 >= page.pageCount" @click="load(page.currentPage + 1)">
          Next
        </button>
      </div>
    </template>
  </section>
</template>
