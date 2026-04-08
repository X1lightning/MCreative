<template>
  <section class="stack-section">
    <div class="section-heading">
      <p class="eyebrow">Featured Work</p>
      <h2>Selected projects</h2>
    </div>
    <div class="portfolio-grid">
      <PortfolioCard v-for="project in projects" :key="project.id" :project="project" />
    </div>
  </section>
</template>

<script setup lang="ts">
import PortfolioCard from './PortfolioCard.vue';
import type { ProjectSummary } from '@/types/api';

defineProps<{ projects: ProjectSummary[] }>();
</script>
