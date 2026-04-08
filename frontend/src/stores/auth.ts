import { defineStore } from 'pinia';
import { adminApi } from '@/api/adminApi';
import type { AdminUser } from '@/types/api';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: { id: null, username: null, authenticated: false } as AdminUser,
    loading: false
  }),
  actions: {
    async restoreSession() {
      this.loading = true;
      try {
        const response = await adminApi.me();
        this.user = response.user;
      } catch {
        this.user = { id: null, username: null, authenticated: false };
      } finally {
        this.loading = false;
      }
    },
    async login(username: string, password: string) {
      this.loading = true;
      try {
        const response = await adminApi.login(username, password);
        this.user = response.user;
      } finally {
        this.loading = false;
      }
    },
    async logout() {
      await adminApi.logout();
      this.user = { id: null, username: null, authenticated: false };
    }
  }
});
