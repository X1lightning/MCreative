<template>
  <main class="admin-page">
    <section class="admin-auth-card">
      <p class="eyebrow">Admin</p>
      <h1>Portfolio manager</h1>
      <form class="admin-form" @submit.prevent="submit">
        <label>
          Username
          <input v-model="username" required />
        </label>
        <label>
          Password
          <input v-model="password" type="password" required />
        </label>
        <ErrorBlock v-if="errorMessage" :message="errorMessage" />
        <button class="button-link" :disabled="authStore.loading">Log in</button>
      </form>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const router = useRouter();
const username = ref('admin');
const password = ref('');
const errorMessage = ref('');

async function submit() {
  errorMessage.value = '';
  try {
    await authStore.login(username.value, password.value);
    await router.push({ name: 'admin-dashboard' });
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Login failed';
  }
}
</script>
