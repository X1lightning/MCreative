import { apiGet } from './http';
import type { CategoryListResponse, ProjectDetail, ProjectListResponse, SiteInfo } from '@/types/api';

export const publicApi = {
  getSite: () => apiGet<SiteInfo>('/api/public/site'),
  getProjects: (params?: { category?: string; featured?: boolean }) => {
    const query = new URLSearchParams();
    if (params?.category) query.set('category', params.category);
    if (params?.featured !== undefined) query.set('featured', String(params.featured));
    return apiGet<ProjectListResponse>(`/api/public/projects?${query.toString()}`);
  },
  getProject: (slug: string) => apiGet<ProjectDetail>(`/api/public/projects/${slug}`),
  getCategories: () => apiGet<CategoryListResponse>('/api/public/categories')
};
