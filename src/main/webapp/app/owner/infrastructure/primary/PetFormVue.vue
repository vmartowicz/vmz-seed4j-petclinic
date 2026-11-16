<script setup lang="ts">
import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { isoDate } from '@/owner/domain/ClinicDates';
import { findPet, fullName, type Owner, type Pet, type PetToSave, type PetType } from '@/owner/domain/Owner';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps<{ ownerId: string; petId?: string }>();

const owners = inject(OWNERS_REPOSITORY);
const router = useRouter();

const owner = ref<Owner | undefined>();
const types = ref<PetType[]>([]);
const pet = ref<PetToSave>({ name: '', birthDate: '', type: '' });
const error = ref('');
const today = isoDate(new Date());

const prefill = (loaded: Owner): void => {
  owner.value = loaded;
  const existing = props.petId ? findPet(loaded, props.petId) : undefined;

  if (existing) {
    pet.value = { name: existing.name, birthDate: existing.birthDate, type: existing.type };
  }
};

onMounted(() => {
  Promise.all([owners.get(props.ownerId), owners.petTypes()])
    .then(([loaded, petTypes]) => {
      types.value = petTypes;
      prefill(loaded);
    })
    .catch((e: Error) => (error.value = e.message));
});

const save = (): Promise<Pet> =>
  props.petId ? owners.updatePet(props.ownerId, props.petId, pet.value) : owners.addPet(props.ownerId, pet.value);

const submit = async (): Promise<void> => {
  error.value = '';

  try {
    await save();
    await router.push({ name: 'OwnerDetails', params: { ownerId: props.ownerId } });
  } catch (e) {
    error.value = (e as Error).message;
  }
};
</script>

<template>
  <section>
    <h1>{{ petId ? 'Edit pet' : 'New pet' }}</h1>
    <p v-if="owner" data-selector="owner-name">Owner: {{ fullName(owner) }}</p>
    <form class="clinic-form" @submit.prevent="submit">
      <label>
        Name
        <input v-model="pet.name" data-selector="name" required maxlength="30" />
      </label>
      <label>
        Birth date
        <input v-model="pet.birthDate" data-selector="birth-date" type="date" required :max="today" />
      </label>
      <label>
        Type
        <select v-model="pet.type" data-selector="type" required>
          <option disabled value="">Choose a type</option>
          <option v-for="type in types" :key="type" :value="type">{{ type }}</option>
        </select>
      </label>
      <p v-if="error" class="clinic-error" data-selector="error">{{ error }}</p>
      <div class="clinic-actions">
        <button type="submit" data-selector="save">{{ petId ? 'Update pet' : 'Add pet' }}</button>
      </div>
    </form>
  </section>
</template>
