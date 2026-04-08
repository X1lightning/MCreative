import { apiForm, apiGet, apiJson } from './http';
import type { AuthResponse, ProjectDetail, ProjectImage, ProjectListResponse, ProjectRequest } from '@/types/api';

export const adminApi = {
  login: (username: string, password: string) =>
    apiJson<AuthResponse>('/api/admin/auth/login', 'POST', { username, password }),
  logout: () => apiJson<{ message: string }>('/api/admin/auth/logout', 'POST'),
  me: () => apiGet<AuthResponse>('/api/admin/auth/me'),
  getProjects: () => apiGet<ProjectListResponse>('/api/admin/projects'),
  getProject: (id: number) => apiGet<ProjectDetail>(`/api/admin/projects/${id}`),
  createProject: (payload: ProjectRequest) => apiJson<ProjectDetail>('/api/admin/projects', 'POST', payload),
  updateProject: (id: number, payload: ProjectRequest) => apiJson<ProjectDetail>(`/api/admin/projects/${id}`, 'PUT', payload),
  deleteProject: (id: number) => apiJson<{ message: string }>(`/api/admin/projects/${id}`, 'DELETE'),
  uploadImage: (projectId: number, file: File, altText: string) => {
    const formData = new FormData();
    formData.set('file', file);
    formData.set('altText', altText);
    return apiForm<ProjectImage>(`/api/admin/projects/${projectId}/images`, formData);
  },
  updateImage: (projectId: number, imageId: number, payload: { altText: string; displayOrder: number }) =>
    apiJson<ProjectImage>(`/api/admin/projects/${projectId}/images/${imageId}`, 'PUT', payload),
  deleteImage: (projectId: number, imageId: number) =>
    apiJson<{ message: string }>(`/api/admin/projects/${projectId}/images/${imageId}`, 'DELETE'),
  reorderImages: (projectId: number, items: Array<{ imageId: number; displayOrder: number }>) =>
    apiJson<ProjectImage[]>(`/api/admin/projects/${projectId}/images/reorder`, 'PUT', { items }),
  setCoverImage: (projectId: number, imageId: number) =>
    apiJson<ProjectDetail>(`/api/admin/projects/${projectId}/images/${imageId}/set-cover`, 'PUT')
};
