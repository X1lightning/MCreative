<template>
  <div class="loading-block">{{ message }}</div>
</template>

<script setup lang="ts">
defineProps<{ message?: string }>();
</script>
