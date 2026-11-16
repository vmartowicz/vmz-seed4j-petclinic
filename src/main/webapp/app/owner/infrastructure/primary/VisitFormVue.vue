<script setup lang="ts">
import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { nextDay } from '@/owner/domain/ClinicDates';
import { findPet, type Pet, type VisitToCreate } from '@/owner/domain/Owner';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps<{ ownerId: string; petId: string }>();

const owners = inject(OWNERS_REPOSITORY);
const router = useRouter();

const pet = ref<Pet | undefined>();
const visit = ref<VisitToCreate>({ date: '', description: '' });
const error = ref('');
const tomorrow = nextDay(new Date());

onMounted(() => {
  owners
    .get(props.ownerId)
    .then(owner => (pet.value = findPet(owner, props.petId)))
    .catch((e: Error) => (error.value = e.message));
});

const submit = async (): Promise<void> => {
  error.value = '';

  try {
    await owners.scheduleVisit(props.ownerId, props.petId, visit.value);
    await router.push({ name: 'OwnerDetails', params: { ownerId: props.ownerId } });
  } catch (e) {
    error.value = (e as Error).message;
  }
};
</script>

<template>
  <section>
    <h1>New visit</h1>
    <p v-if="pet" data-selector="pet">Pet: {{ pet.name }} ({{ pet.type }}, born {{ pet.birthDate }})</p>
    <form class="clinic-form" @submit.prevent="submit">
      <label>
        Date
        <input v-model="visit.date" data-selector="date" type="date" required :min="tomorrow" />
      </label>
      <label>
        Description
        <input v-model="visit.description" data-selector="description" required maxlength="255" />
      </label>
      <p v-if="error" class="clinic-error" data-selector="error">{{ error }}</p>
      <div class="clinic-actions">
        <button type="submit" data-selector="save">Add visit</button>
      </div>
    </form>
    <template v-if="pet && pet.visits.length > 0">
      <h2>Previous visits</h2>
      <table>
        <tbody>
          <tr v-for="previous in pet.visits" :key="previous.id">
            <td>{{ previous.date }}</td>
            <td>{{ previous.description }}</td>
          </tr>
        </tbody>
      </table>
    </template>
  </section>
</template>
