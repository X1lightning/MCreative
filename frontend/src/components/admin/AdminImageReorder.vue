<template>
  <div class="admin-reorder-list">
    <div v-for="(image, index) in localImages" :key="image.id" class="admin-reorder-row">
      <span>{{ index + 1 }}.</span>
      <span>{{ image.originalFileName }}</span>
      <div>
        <button @click="move(index, -1)" :disabled="index === 0">Up</button>
        <button @click="move(index, 1)" :disabled="index === localImages.length - 1">Down</button>
      </div>
    </div>
    <button class="button-link" @click="save">Save order</button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import type { ProjectImage } from '@/types/api';

const props = defineProps<{ images: ProjectImage[] }>();
const emit = defineEmits<{ save: [items: Array<{ imageId: number; displayOrder: number }>] }>();

const localImages = ref<ProjectImage[]>([...props.images]);

watch(
  () => props.images,
  (images) => {
    localImages.value = [...images];
  },
  { deep: true }
);

function move(index: number, delta: number) {
  const nextIndex = index + delta;
  const clone = [...localImages.value];
  const [item] = clone.splice(index, 1);
  clone.splice(nextIndex, 0, item);
  localImages.value = clone;
}

function save() {
  emit(
    'save',
    localImages.value.map((image, index) => ({ imageId: image.id, displayOrder: index }))
  );
}
</script>
