<template>
  <footer class="site-footer">
    <div>
      <p class="footer-title">{{ designerName }}</p>
      <p>{{ tagline }}</p>
    </div>
    <div class="footer-links">
      <a :href="`mailto:${email}`">{{ email }}</a>
      <a :href="linkedinUrl" target="_blank" rel="noreferrer">LinkedIn</a>
      <a :href="instagramUrl" target="_blank" rel="noreferrer">Instagram</a>
    </div>
  </footer>
</template>

<script setup lang="ts">
defineProps<{
  designerName: string;
  tagline: string;
  email: string;
  linkedinUrl: string;
  instagramUrl: string;
}>();
</script>
