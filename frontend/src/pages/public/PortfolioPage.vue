<template>
  <div class="site-shell">
    <SiteHeader brand-name="M Creative" />
    <main class="page-shell">
      <section class="page-intro">
        <p class="eyebrow">Portfolio</p>
        <h1>Recent work</h1>
      </section>
      <CategoryFilterBar v-model="selectedCategory" :categories="categories" />
      <LoadingBlock v-if="loading" message="Loading projects..." />
      <ErrorBlock v-else-if="error" :message="error" />
      <PortfolioGrid v-else :projects="projects" />
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { publicApi } from '@/api/publicApi';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import LoadingBlock from '@/components/common/LoadingBlock.vue';
import SiteHeader from '@/components/layout/SiteHeader.vue';
import CategoryFilterBar from '@/components/portfolio/CategoryFilterBar.vue';
import PortfolioGrid from '@/components/portfolio/PortfolioGrid.vue';
import { useAsyncState } from '@/composables/useAsyncState';
import type { ProjectSummary } from '@/types/api';

const route = useRoute();
const selectedCategory = ref(typeof route.query.category === 'string' ? route.query.category : '');
const categories = ref<string[]>([]);
const projects = ref<ProjectSummary[]>([]);
const { loading, error, run } = useAsyncState();

async function loadProjects() {
  await run(async () => {
    const [projectList, categoryList] = await Promise.all([
      publicApi.getProjects(selectedCategory.value ? { category: selectedCategory.value } : undefined),
      publicApi.getCategories()
    ]);
    projects.value = projectList.items;
    categories.value = categoryList.categories;
  });
}

watch(selectedCategory, loadProjects);
onMounted(loadProjects);
</script>
