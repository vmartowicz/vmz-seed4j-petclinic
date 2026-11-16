<script lang="ts">
export default {
  name: 'AppVue',
};
</script>

<template>
  <header class="clinic-header">
    <span class="clinic-brand">Petclinic</span>
    <nav class="clinic-nav">
      <router-link :to="{ name: 'Homepage' }">Home</router-link>
      <router-link :to="{ name: 'OwnersSearch' }" data-selector="nav-owners">Find owners</router-link>
      <router-link :to="{ name: 'Vets' }" data-selector="nav-vets">Veterinarians</router-link>
    </nav>
  </header>
  <main class="clinic-main">
    <router-view />
  </main>
</template>

<style>
:root {
  --clinic-green: #2e7d4f;
  --clinic-border: #d5dbe0;
  --clinic-error: #b3261e;
}

body {
  margin: 0;
  background: #f7f9fa;
  color: #1f2a33;
  font-family:
    system-ui,
    -apple-system,
    'Segoe UI',
    Roboto,
    sans-serif;
}

.clinic-header {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: center;
  padding: 0.75rem 1rem;
  background: var(--clinic-green);
  color: #fff;
}

.clinic-brand {
  font-weight: 700;
  font-size: 1.2rem;
}

.clinic-nav {
  display: flex;
  gap: 1rem;
}

.clinic-nav a {
  color: #fff;
  text-decoration: none;
}

.clinic-nav a.router-link-active {
  text-decoration: underline;
}

.clinic-main {
  max-width: 960px;
  margin: 0 auto;
  padding: 1rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
}

th,
td {
  padding: 0.5rem;
  border-bottom: 1px solid var(--clinic-border);
  text-align: left;
  vertical-align: top;
}

form.clinic-form {
  display: grid;
  gap: 0.75rem;
  max-width: 480px;
}

form.clinic-form label {
  display: grid;
  gap: 0.25rem;
  font-weight: 600;
}

input,
select,
button {
  font: inherit;
  padding: 0.4rem 0.5rem;
}

button,
.clinic-button {
  background: var(--clinic-green);
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
}

button:disabled {
  opacity: 0.5;
  cursor: default;
}

.clinic-error {
  color: var(--clinic-error);
}

.clinic-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin: 1rem 0;
}

.clinic-pagination {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  margin-top: 1rem;
}
</style>
