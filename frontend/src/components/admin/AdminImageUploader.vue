<template>
  <form class="admin-upload" @submit.prevent="submit">
    <label>
      Image file
      <input type="file" accept=".jpg,.jpeg,.png" @change="onFileChange" required />
    </label>
    <label>
      Alt text
      <input v-model="altText" placeholder="Describe the image briefly" />
    </label>
    <button class="button-link" :disabled="!file">Upload image</button>
  </form>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits<{ upload: [payload: { file: File; altText: string }] }>();

const file = ref<File | null>(null);
const altText = ref('');

function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement;
  file.value = input.files?.[0] ?? null;
}

function submit() {
  if (!file.value) return;
  emit('upload', { file: file.value, altText: altText.value });
  file.value = null;
  altText.value = '';
}
</script>
