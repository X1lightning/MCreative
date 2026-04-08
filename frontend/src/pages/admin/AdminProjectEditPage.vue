<template>
  <main class="admin-page">
    <section class="admin-toolbar">
      <div>
        <p class="eyebrow">Admin Project</p>
        <h1>{{ pageTitle }}</h1>
      </div>
      <RouterLink class="button-link" to="/admin">Back</RouterLink>
    </section>
    <ErrorBlock v-if="errorMessage" :message="errorMessage" />
    <AdminProjectForm :model-value="form" :submit-label="submitLabel" @submit="saveProject" />
    <section v-if="projectId && project" class="admin-image-section">
      <h2>Images</h2>
      <AdminImageUploader @upload="uploadImage" />
      <AdminImageList
        :images="project.images"
        @update-alt="updateAlt"
        @set-cover="setCover"
        @remove="deleteImage"
      />
      <AdminImageReorder :images="project.images" @save="reorderImages" />
      <button class="danger-button" @click="deleteProject">Delete project</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { adminApi } from '@/api/adminApi';
import AdminImageList from '@/components/admin/AdminImageList.vue';
import AdminImageReorder from '@/components/admin/AdminImageReorder.vue';
import AdminImageUploader from '@/components/admin/AdminImageUploader.vue';
import AdminProjectForm from '@/components/admin/AdminProjectForm.vue';
import ErrorBlock from '@/components/common/ErrorBlock.vue';
import type { ProjectDetail, ProjectRequest } from '@/types/api';

const props = defineProps<{ id?: string }>();
const router = useRouter();
const projectId = computed(() => (props.id ? Number(props.id) : null));
const project = ref<ProjectDetail | null>(null);
const errorMessage = ref('');

const defaultForm: ProjectRequest = {
  title: '',
  slug: '',
  category: '',
  description: '',
  toolsUsed: '',
  createdFor: '',
  projectDate: '',
  featured: false,
  published: false,
  sortOrder: 0
};

const form = ref<ProjectRequest>({ ...defaultForm });

const pageTitle = computed(() => (projectId.value ? 'Edit project' : 'Create project'));
const submitLabel = computed(() => (projectId.value ? 'Save changes' : 'Create project'));

onMounted(async () => {
  if (!projectId.value) return;
  await loadProject(projectId.value);
});

async function loadProject(id: number) {
  try {
    project.value = await adminApi.getProject(id);
    form.value = {
      title: project.value.title,
      slug: project.value.slug,
      category: project.value.category,
      description: project.value.description,
      toolsUsed: project.value.toolsUsed ?? '',
      createdFor: project.value.createdFor ?? '',
      projectDate: project.value.projectDate ?? '',
      featured: project.value.featured,
      published: project.value.published,
      sortOrder: project.value.sortOrder
    };
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load project';
  }
}

async function saveProject(payload: ProjectRequest) {
  errorMessage.value = '';
  try {
    const saved = projectId.value
      ? await adminApi.updateProject(projectId.value, payload)
      : await adminApi.createProject(payload);
    await router.push(`/admin/projects/${saved.id}`);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to save project';
  }
}

async function uploadImage(payload: { file: File; altText: string }) {
  if (!projectId.value) return;
  try {
    await adminApi.uploadImage(projectId.value, payload.file, payload.altText);
    await loadProject(projectId.value);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to upload image';
  }
}

async function updateAlt(payload: { imageId: number; altText: string }) {
  if (!projectId.value || !project.value) return;
  const image = project.value.images.find((item) => item.id === payload.imageId);
  if (!image) return;
  await adminApi.updateImage(projectId.value, payload.imageId, {
    altText: payload.altText,
    displayOrder: image.displayOrder
  });
  await loadProject(projectId.value);
}

async function setCover(imageId: number) {
  if (!projectId.value) return;
  await adminApi.setCoverImage(projectId.value, imageId);
  await loadProject(projectId.value);
}

async function deleteImage(imageId: number) {
  if (!projectId.value) return;
  await adminApi.deleteImage(projectId.value, imageId);
  await loadProject(projectId.value);
}

async function reorderImages(items: Array<{ imageId: number; displayOrder: number }>) {
  if (!projectId.value) return;
  await adminApi.reorderImages(projectId.value, items);
  await loadProject(projectId.value);
}

async function deleteProject() {
  if (!projectId.value) return;
  await adminApi.deleteProject(projectId.value);
  await router.push('/admin');
}
</script>
