<template>
  <aside class="project-meta-panel">
    <div>
      <p class="eyebrow">{{ category }}</p>
      <h1>{{ title }}</h1>
    </div>
    <p>{{ description }}</p>
    <dl class="meta-list">
      <div v-if="toolsUsed">
        <dt>Tools</dt>
        <dd>{{ toolsUsed }}</dd>
      </div>
      <div v-if="createdFor">
        <dt>Created for</dt>
        <dd>{{ createdFor }}</dd>
      </div>
      <div v-if="projectDate">
        <dt>Date</dt>
        <dd>{{ projectDate }}</dd>
      </div>
    </dl>
  </aside>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  category: string;
  description: string;
  toolsUsed?: string | null;
  createdFor?: string | null;
  projectDate?: string | null;
}>();
</script>
