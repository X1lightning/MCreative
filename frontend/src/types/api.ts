export interface SiteInfo {
  siteName: string;
  designerName: string;
  tagline: string;
  aboutSnippet: string;
  email: string;
  linkedinUrl: string;
  instagramUrl: string;
}

export interface ProjectImage {
  id: number;
  url: string;
  fileName: string;
  originalFileName: string;
  altText: string | null;
  displayOrder: number;
  cover: boolean;
}

export interface ProjectSummary {
  id: number;
  title: string;
  slug: string;
  category: string;
  description: string;
  toolsUsed: string | null;
  createdFor: string | null;
  projectDate: string | null;
  featured: boolean;
  published: boolean;
  sortOrder: number;
  coverImage: ProjectImage | null;
}

export interface ProjectDetail extends ProjectSummary {
  images: ProjectImage[];
}

export interface ProjectListResponse {
  items: ProjectSummary[];
}

export interface CategoryListResponse {
  categories: string[];
}

export interface AdminUser {
  id: number | null;
  username: string | null;
  authenticated: boolean;
}

export interface AuthResponse {
  message: string;
  user: AdminUser;
}

export interface ProjectRequest {
  title: string;
  slug: string;
  category: string;
  description: string;
  toolsUsed: string;
  createdFor: string;
  projectDate: string;
  featured: boolean;
  published: boolean;
  sortOrder: number;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  validationErrors?: string[];
}
