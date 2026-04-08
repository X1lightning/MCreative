import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/pages/public/HomePage.vue';
import PortfolioPage from '@/pages/public/PortfolioPage.vue';
import ProjectDetailPage from '@/pages/public/ProjectDetailPage.vue';
import AboutPage from '@/pages/public/AboutPage.vue';
import ContactPage from '@/pages/public/ContactPage.vue';
import AdminLoginPage from '@/pages/admin/AdminLoginPage.vue';
import AdminDashboardPage from '@/pages/admin/AdminDashboardPage.vue';
import AdminProjectEditPage from '@/pages/admin/AdminProjectEditPage.vue';
import { useAuthStore } from '@/stores/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomePage },
    { path: '/portfolio', name: 'portfolio', component: PortfolioPage },
    { path: '/portfolio/:slug', name: 'project-detail', component: ProjectDetailPage, props: true },
    { path: '/about', name: 'about', component: AboutPage },
    { path: '/contact', name: 'contact', component: ContactPage },
    { path: '/admin/login', name: 'admin-login', component: AdminLoginPage },
    { path: '/admin', name: 'admin-dashboard', component: AdminDashboardPage, meta: { requiresAuth: true } },
    { path: '/admin/projects/new', name: 'admin-project-new', component: AdminProjectEditPage, meta: { requiresAuth: true } },
    { path: '/admin/projects/:id', name: 'admin-project-edit', component: AdminProjectEditPage, props: true, meta: { requiresAuth: true } }
  ],
  scrollBehavior() {
    return { top: 0 };
  }
});

router.beforeEach(async (to) => {
  const authStore = useAuthStore();
  if (!authStore.user.authenticated && !authStore.loading) {
    await authStore.restoreSession();
  }
  if (to.meta.requiresAuth && !authStore.user.authenticated) {
    return { name: 'admin-login' };
  }
  return true;
});

export default router;
