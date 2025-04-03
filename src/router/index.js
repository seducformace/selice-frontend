// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';

// Importações das views
import Dashboard from '@/views/Dashboard.vue';
import Faculties from '@/views/Faculties.vue';
import CoordinatorPage from '@/views/CoordinatorPage.vue'; // ✅ Nome atualizado corretamente
import StudentsPage from '@/views/StudentsPage.vue';
import Professors from '@/views/Professors.vue';
import ProfessorsManagement from '@/views/ProfessorsManagement.vue';
import Schools from '@/views/Schools.vue';
import Reports from '@/views/Reports.vue';

const routes = [
  // Página de login
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: 'Login', requiresAuth: false },
  },

  // Redirecionamento da raiz para login
  {
    path: '/',
    redirect: '/login',
  },

  // Rotas protegidas
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: 'Painel Principal', requiresAuth: true },
  },
  {
    path: '/faculties',
    name: 'Faculties',
    component: Faculties,
    meta: { title: 'Gerenciamento de Faculdades', requiresAuth: true },
  },
  {
    path: '/coordinators',
    name: 'CoordinatorPage',
    component: CoordinatorPage,
    meta: { title: 'Gerenciamento de Coordenadores', requiresAuth: true },
  },
  {
    path: '/students',
    name: 'Students',
    component: StudentsPage,
    meta: { title: 'Gerenciamento de Estudantes', requiresAuth: true },
  },
  {
    path: '/professors',
    name: 'Professors',
    component: Professors,
    meta: { title: 'Gerenciamento de Professores', requiresAuth: true },
  },
  {
    path: '/professors-management',
    name: 'ProfessorsManagement',
    component: ProfessorsManagement,
    meta: { title: 'Gestão de Professores', requiresAuth: true },
  },
  {
    path: '/schools',
    name: 'Schools',
    component: Schools,
    meta: { title: 'Gerenciamento de Escolas', requiresAuth: true },
  },
  {
    path: '/reports',
    name: 'Reports',
    component: Reports,
    meta: { title: 'Relatórios', requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Middleware para proteger rotas com base no token de autenticação
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'Sistema de Gerenciamento';

  const token = localStorage.getItem('token');
  const isAuthenticated = token !== null && token !== '';

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else if (to.path === '/login' && isAuthenticated) {
    next('/dashboard');
  } else {
    next();
  }
});

export default router;
