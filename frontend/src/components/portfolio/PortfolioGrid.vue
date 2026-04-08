<template>
  <div class="portfolio-grid">
    <PortfolioCard v-for="project in projects" :key="project.id" :project="project" />
  </div>
</template>

<script setup lang="ts">
import PortfolioCard from './PortfolioCard.vue';
import type { ProjectSummary } from '@/types/api';

defineProps<{ projects: ProjectSummary[] }>();
</script>
