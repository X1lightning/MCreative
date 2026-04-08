<template>
  <div class="site-shell">
    <SiteHeader brand-name="M Creative" />
    <main v-if="project" class="page-shell project-detail-layout">
      <img
        v-if="project.coverImage"
        :src="project.coverImage.url"
        :alt="project.coverImage.altText || project.title"
        class="project-cover-image"
      />
      <ProjectMetaPanel
        :title="project.title"
        :category="project.category"
        :description="project.description"
        :tools-used="project.toolsUsed"
        :created-for="project.createdFor"
        :project-date="project.projectDate"
      />
      <ProjectImageGallery :images="project.images" />
    </main>
    <LoadingBlock v-else-if="loading" message="Loading project..." />
    <ErrorBlock v-else :message="error" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { publicApi } from '@/api/publicApi';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import LoadingBlock from '@/components/common/LoadingBlock.vue';
import SiteHeader from '@/components/layout/SiteHeader.vue';
import ProjectImageGallery from '@/components/portfolio/ProjectImageGallery.vue';
import ProjectMetaPanel from '@/components/portfolio/ProjectMetaPanel.vue';
import { useAsyncState } from '@/composables/useAsyncState';
import type { ProjectDetail } from '@/types/api';

const route = useRoute();
const project = ref<ProjectDetail | null>(null);
const { loading, error, run } = useAsyncState();

onMounted(async () => {
  await run(async () => {
    project.value = await publicApi.getProject(route.params.slug as string);
  });
});
</script>
