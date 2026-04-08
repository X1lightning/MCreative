<template>
  <div class="site-shell">
    <SiteHeader brand-name="M Creative" />
    <main class="page-shell prose-page">
      <p class="eyebrow">Contact</p>
      <h1>Available for internships and creative collaborations.</h1>
      <div class="contact-list">
        <a href="mailto:morgan@example.com">morgan@example.com</a>
        <a href="https://www.linkedin.com" target="_blank" rel="noreferrer">LinkedIn</a>
        <a href="https://www.instagram.com" target="_blank" rel="noreferrer">Instagram</a>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import SiteHeader from '@/components/layout/SiteHeader.vue';
</script>
