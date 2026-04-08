<template>
  <section class="hero-section">
    <p class="eyebrow">Graphic Design Portfolio</p>
    <h1>{{ designerName }}</h1>
    <p class="hero-copy">{{ tagline }}</p>
    <RouterLink class="button-link" to="/portfolio">Browse Projects</RouterLink>
  </section>
</template>

<script setup lang="ts">
defineProps<{ designerName: string; tagline: string }>();
</script>
