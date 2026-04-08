<template>
  <div v-if="site" class="site-shell">
    <SiteHeader :brand-name="site.designerName" />
    <main class="page-shell">
      <HeroSection :designer-name="site.designerName" :tagline="site.tagline" />
      <FeaturedProjectsGrid :projects="featuredProjects" />
      <section class="stack-section">
        <div class="section-heading">
          <p class="eyebrow">Categories</p>
          <h2>Ways of working</h2>
        </div>
        <div class="category-preview-grid">
          <RouterLink v-for="category in categories" :key="category" class="category-preview-card" :to="`/portfolio?category=${encodeURIComponent(category)}`">
            {{ category }}
          </RouterLink>
        </div>
      </section>
      <section class="about-snippet-card">
        <p class="eyebrow">About</p>
        <p>{{ site.aboutSnippet }}</p>
      </section>
    </main>
    <SiteFooter
      :designer-name="site.designerName"
      :tagline="site.tagline"
      :email="site.email"
      :linkedin-url="site.linkedinUrl"
      :instagram-url="site.instagramUrl"
    />
  </div>
  <LoadingBlock v-else-if="loading" message="Loading portfolio..." />
  <ErrorBlock v-else :message="error" />
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { publicApi } from '@/api/publicApi';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import LoadingBlock from '@/components/common/LoadingBlock.vue';
import SiteFooter from '@/components/layout/SiteFooter.vue';
import SiteHeader from '@/components/layout/SiteHeader.vue';
import FeaturedProjectsGrid from '@/components/portfolio/FeaturedProjectsGrid.vue';
import HeroSection from '@/components/portfolio/HeroSection.vue';
import { useAsyncState } from '@/composables/useAsyncState';
import type { ProjectSummary, SiteInfo } from '@/types/api';

const site = ref<SiteInfo | null>(null);
const projects = ref<ProjectSummary[]>([]);
const categories = ref<string[]>([]);
const { loading, error, run } = useAsyncState();

const featuredProjects = computed(() => projects.value.filter((project) => project.featured).slice(0, 4));

onMounted(async () => {
  await run(async () => {
    const [siteInfo, projectList, categoryList] = await Promise.all([
      publicApi.getSite(),
      publicApi.getProjects({ featured: true }),
      publicApi.getCategories()
    ]);
    site.value = siteInfo;
    projects.value = projectList.items;
    categories.value = categoryList.categories;
  });
});
</script>
