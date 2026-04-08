<template>
  <p class="error-block">{{ message }}</p>
</template>

<script setup lang="ts">
defineProps<{ message: string }>();
</script>
