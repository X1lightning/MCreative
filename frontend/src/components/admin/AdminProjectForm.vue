<template>
  <form class="admin-form" @submit.prevent="handleSubmit">
    <div class="form-grid">
      <label>
        Title
        <input v-model="localForm.title" required />
      </label>
      <label>
        Slug
        <input v-model="localForm.slug" required />
      </label>
      <label>
        Category
        <input v-model="localForm.category" required />
      </label>
      <label>
        Project Date
        <input v-model="localForm.projectDate" type="date" />
      </label>
      <label>
        Tools Used
        <input v-model="localForm.toolsUsed" />
      </label>
      <label>
        Created For
        <input v-model="localForm.createdFor" />
      </label>
      <label>
        Sort Order
        <input v-model.number="localForm.sortOrder" type="number" min="0" />
      </label>
      <label class="checkbox-row">
        <input v-model="localForm.featured" type="checkbox" />
        Featured
      </label>
      <label class="checkbox-row">
        <input v-model="localForm.published" type="checkbox" />
        Published
      </label>
    </div>
    <label>
      Description
      <textarea v-model="localForm.description" rows="8" required />
    </label>
    <div class="admin-actions">
      <button type="submit" class="button-link">{{ submitLabel }}</button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue';
import type { ProjectRequest } from '@/types/api';

const props = defineProps<{
  modelValue: ProjectRequest;
  submitLabel: string;
}>();

const emit = defineEmits<{ submit: [value: ProjectRequest] }>();

const localForm = reactive<ProjectRequest>({ ...props.modelValue });

watch(
  () => props.modelValue,
  (value) => Object.assign(localForm, value),
  { deep: true }
);

function handleSubmit() {
  emit('submit', { ...localForm });
}
</script>
