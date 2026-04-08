<template>
  <div class="filter-bar">
    <button :class="{ active: modelValue === '' }" @click="$emit('update:modelValue', '')">All</button>
    <button
      v-for="category in categories"
      :key="category"
      :class="{ active: modelValue === category }"
      @click="$emit('update:modelValue', category)"
    >
      {{ category }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{ categories: string[]; modelValue: string }>();
defineEmits<{ 'update:modelValue': [value: string] }>();
</script>
