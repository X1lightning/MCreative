<template>
  <header class="site-header">
    <RouterLink class="brand-mark" to="/">{{ brandName }}</RouterLink>
    <nav class="site-nav">
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/portfolio">Portfolio</RouterLink>
      <RouterLink to="/about">About</RouterLink>
      <RouterLink to="/contact">Contact</RouterLink>
    </nav>
  </header>
</template>

<script setup lang="ts">
defineProps<{ brandName: string }>();
</script>
