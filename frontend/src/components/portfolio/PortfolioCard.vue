<template>
  <RouterLink class="portfolio-card" :to="`/portfolio/${project.slug}`">
    <img
      v-if="project.coverImage"
      :src="project.coverImage.url"
      :alt="project.coverImage.altText || project.title"
      class="portfolio-card__image"
    />
    <div v-else class="image-placeholder">No cover image</div>
    <div class="portfolio-card__body">
      <p class="portfolio-card__category">{{ project.category }}</p>
      <h3>{{ project.title }}</h3>
      <p>{{ project.description }}</p>
    </div>
  </RouterLink>
</template>

<script setup lang="ts">
import type { ProjectSummary } from '@/types/api';

defineProps<{ project: ProjectSummary }>();
</script>
