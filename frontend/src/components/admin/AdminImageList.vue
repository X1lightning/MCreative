<template>
  <div class="admin-image-list">
    <article v-for="image in images" :key="image.id" class="admin-image-card">
      <img :src="image.url" :alt="image.altText || 'Project image'" />
      <div class="admin-image-card__body">
        <input :value="image.altText || ''" @change="updateAlt(image.id, $event)" placeholder="Alt text" />
        <div class="admin-image-card__actions">
          <button @click="$emit('set-cover', image.id)">{{ image.cover ? 'Cover image' : 'Set cover' }}</button>
          <button @click="$emit('remove', image.id)">Delete</button>
        </div>
      </div>
    </article>
  </div>
</template>

<script setup lang="ts">
import type { ProjectImage } from '@/types/api';

defineProps<{ images: ProjectImage[] }>();

const emit = defineEmits<{
  'update-alt': [payload: { imageId: number; altText: string }];
  'set-cover': [imageId: number];
  remove: [imageId: number];
}>();

function updateAlt(imageId: number, event: Event) {
  const input = event.target as HTMLInputElement;
  emit('update-alt', { imageId, altText: input.value });
}
</script>
