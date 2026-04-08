<template>
  <main class="admin-page">
    <section class="admin-toolbar">
      <div>
        <p class="eyebrow">Admin Dashboard</p>
        <h1>Projects</h1>
      </div>
      <div class="admin-toolbar__actions">
        <RouterLink class="button-link" to="/admin/projects/new">New Project</RouterLink>
        <button @click="logout">Log out</button>
      </div>
    </section>
    <LoadingBlock v-if="loading" message="Loading projects..." />
    <ErrorBlock v-else-if="error" :message="error" />
    <section v-else class="admin-list">
      <RouterLink v-for="project in projects" :key="project.id" :to="`/admin/projects/${project.id}`" class="admin-project-row">
        <div>
          <strong>{{ project.title }}</strong>
          <p>{{ project.category }}</p>
        </div>
        <div class="admin-status-group">
          <span>{{ project.published ? 'Published' : 'Draft' }}</span>
          <span v-if="project.featured">Featured</span>
        </div>
      </RouterLink>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { adminApi } from '@/api/adminApi';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import LoadingBlock from '@/components/common/LoadingBlock.vue';
import { useAsyncState } from '@/composables/useAsyncState';
import { useAuthStore } from '@/stores/auth';
import type { ProjectSummary } from '@/types/api';

const router = useRouter();
const authStore = useAuthStore();
const projects = ref<ProjectSummary[]>([]);
const { loading, error, run } = useAsyncState();

onMounted(async () => {
  await run(async () => {
    projects.value = (await adminApi.getProjects()).items;
  });
});

async function logout() {
  await authStore.logout();
  await router.push({ name: 'admin-login' });
}
</script>
