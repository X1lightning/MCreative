<template>
  <section class="project-gallery">
    <img
      v-for="image in images"
      :key="image.id"
      :src="image.url"
      :alt="image.altText || 'Project image'"
      class="project-gallery__image"
    />
  </section>
</template>

<script setup lang="ts">
import type { ProjectImage } from '@/types/api';

defineProps<{ images: ProjectImage[] }>();
</script>
