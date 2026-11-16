<script setup lang="ts">
import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { emptyOwner, ownerToSave, type Owner } from '@/owner/domain/Owner';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps<{ ownerId?: string }>();

const owners = inject(OWNERS_REPOSITORY);
const router = useRouter();

const owner = ref(emptyOwner());
const error = ref('');

onMounted(() => {
  if (props.ownerId) {
    owners
      .get(props.ownerId)
      .then(loaded => (owner.value = ownerToSave(loaded)))
      .catch((e: Error) => (error.value = e.message));
  }
});

const save = (): Promise<Owner> => (props.ownerId ? owners.update(props.ownerId, owner.value) : owners.register(owner.value));

const submit = async (): Promise<void> => {
  error.value = '';

  try {
    const saved = await save();
    await router.push({ name: 'OwnerDetails', params: { ownerId: saved.id } });
  } catch (e) {
    error.value = (e as Error).message;
  }
};
</script>

<template>
  <section>
    <h1>{{ ownerId ? 'Edit owner' : 'New owner' }}</h1>
    <form class="clinic-form" @submit.prevent="submit">
      <label>
        First name
        <input v-model="owner.firstName" data-selector="first-name" required maxlength="30" />
      </label>
      <label>
        Last name
        <input v-model="owner.lastName" data-selector="last-name" required maxlength="30" />
      </label>
      <label>
        Address
        <input v-model="owner.address" data-selector="address" required maxlength="255" />
      </label>
      <label>
        City
        <input v-model="owner.city" data-selector="city" required maxlength="80" />
      </label>
      <label>
        Telephone
        <input v-model="owner.telephone" data-selector="telephone" required pattern="\d{10}" title="Exactly 10 digits" />
      </label>
      <p v-if="error" class="clinic-error" data-selector="error">{{ error }}</p>
      <div class="clinic-actions">
        <button type="submit" data-selector="save">{{ ownerId ? 'Update owner' : 'Add owner' }}</button>
      </div>
    </form>
  </section>
</template>
