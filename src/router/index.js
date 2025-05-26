import { createRouter, createWebHistory } from 'vue-router';

import Dashboard from '@/views/Dashboard.vue';
import FacultiesPage from '@/views/FacultiesPage.vue';
import CoordinatorPage from '@/views/CoordinatorPage.vue';
import StudentsPage from '@/views/StudentsPage.vue';
import ProfessorsPage from '@/views/ProfessorsPage.vue';
import SchoolsPage from '@/views/SchoolsPage.vue';
import ReportsPage from '@/views/ReportsPage.vue';
import AttendanceDiary from '@/views/AttendanceDiary.vue';
import StudentsByTeacher from '@/views/StudentsByTeacher.vue';
import DashboardProfessor from '@/views/DashboardProfessor.vue';
import CoordinatorDashboard from '@/views/CoordinatorDashboard.vue';
import DashboardStudent from '@/views/DashboardStudent.vue';
import DiaryView from '@/views/DiaryView.vue';
import StudentReportView from '@/views/StudentReportView.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: 'Login', requiresAuth: false },
  },
  {
    path: '/students-by-teacher-test',
    name: 'StudentsByTeacher',
    component: StudentsByTeacher,
    meta: { title: 'Meus Estudantes', requiresAuth: true },
  },
  {
    path: '/diary-test',
    name: 'AttendanceDiary',
    component: AttendanceDiary,
    props: (route) => ({ studentId: Number(route.query.studentId) }),
    meta: { title: 'Diário de Presença', requiresAuth: true },
  },
  {
    path: '/diary-view',
    name: 'DiaryView',
    component: DiaryView,
    meta: { title: 'Meu Diário de Presença', requiresAuth: true },
  },
  {
    path: '/student-report',
    name: 'StudentReportView',
    component: StudentReportView,
    meta: { title: 'Meus Relatórios', requiresAuth: true },
  },
  {
    path: '/dashboard-professor',
    name: 'DashboardProfessor',
    component: DashboardProfessor,
    meta: { title: 'Painel do Professor', requiresAuth: true },
  },
  {
    path: '/dashboard-coordinator',
    name: 'CoordinatorDashboard',
    component: CoordinatorDashboard,
    meta: { title: 'Painel do Coordenador', requiresAuth: true },
  },
  {
    path: '/dashboard-coordinator-faculty',
    name: 'CoordinatorDashboardFaculty',
    component: CoordinatorDashboard,
    meta: { title: 'Painel do Coordenador (Faculdade)', requiresAuth: true },
  },
  {
    path: '/dashboard-coordinator-school',
    name: 'CoordinatorDashboardSchool',
    component: CoordinatorDashboard,
    meta: { title: 'Painel do Coordenador (Escola)', requiresAuth: true },
  },
  {
    path: '/dashboard-student',
    name: 'DashboardStudent',
    component: DashboardStudent,
    meta: { title: 'Painel do Estudante', requiresAuth: true },
  },
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: 'Painel Principal', requiresAuth: true },
  },
  {
    path: '/faculties',
    name: 'FacultiesPage',
    component: FacultiesPage,
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
    name: 'StudentsPage',
    component: StudentsPage,
    meta: { title: 'Gerenciamento de Estudantes', requiresAuth: true },
  },
  {
    path: '/professors',
    name: 'ProfessorsPage',
    component: ProfessorsPage,
    meta: { title: 'Gerenciamento de Professores', requiresAuth: true },
  },
  {
    path: '/schools',
    name: 'SchoolsPage',
    component: SchoolsPage,
    meta: { title: 'Gerenciamento de Escolas', requiresAuth: true },
  },
  {
    path: '/reports',
    name: 'ReportsPage',
    component: ReportsPage,
    meta: { title: 'Relatórios', requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'Sistema de Gerenciamento';

  const token = localStorage.getItem('token');
  const isAuthenticated = token && token !== 'null' && token !== 'undefined';

  if (to.meta.requiresAuth && !isAuthenticated) {
    return next('/login');
  }

  if (to.path === '/login' && isAuthenticated) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const role =
        payload.role ||
        (payload.authorities && payload.authorities[0]?.authority);

      const roleRouteMap = {
        ROLE_ADMIN: '/dashboard',
        ROLE_COORDINATOR_FACULTY: '/dashboard-coordinator-faculty',
        ROLE_COORDINATOR_SCHOOL: '/dashboard-coordinator-school',
        ROLE_TEACHER: '/dashboard-professor',
        ROLE_STUDENT: '/dashboard-student',
      };

      const redirectPath = roleRouteMap[role] || '/dashboard';
      return next(redirectPath);
    } catch (error) {
      console.error('[Router Guard] Erro ao decodificar token:', error);
      localStorage.removeItem('token');
      localStorage.removeItem('userEmail');
      return next('/login');
    }
  }

  return next();
});

export default router;
