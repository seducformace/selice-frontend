<template>
  <aside class="sidebar">
    <ul class="menu">
      <li
        v-for="(item, index) in filteredMenuItems"
        :key="index"
        class="menu-item"
      >
        <router-link :to="item.route" class="menu-link">
          <i :class="item.icon" class="menu-icon"></i>
          <span class="menu-text">{{ item.label }}</span>
        </router-link>
      </li>
    </ul>
  </aside>
</template>

<script>
import jwt_decode from 'jwt-decode';

export default {
  name: 'Sidebar',
  data() {
    return {
      menuItems: [
        {
          label: 'Dashboard',
          icon: 'fas fa-home',
          route: '/dashboard',
          roles: ['ROLE_ADMIN'],
        },
        {
          label: 'Faculdades',
          icon: 'fas fa-university',
          route: '/faculties',
          roles: ['ROLE_ADMIN'],
        },
        {
          label: 'Coordenadores',
          icon: 'fas fa-chalkboard-teacher',
          route: '/coordinators',
          roles: ['ROLE_ADMIN'],
        },
        {
          label: 'Alunos',
          icon: 'fas fa-user-graduate',
          route: '/students',
          roles: [
            'ROLE_ADMIN',
            'ROLE_COORDINATOR_FACULTY',
            'ROLE_COORDINATOR_SCHOOL',
          ],
        },
        {
          label: 'Professores',
          icon: 'fas fa-user-tie',
          route: '/professors',
          roles: ['ROLE_ADMIN'],
        },
        {
          label: 'Escolas',
          icon: 'fas fa-school',
          route: '/schools',
          roles: ['ROLE_ADMIN'],
        },
        {
          label: 'Relatórios',
          icon: 'fas fa-chart-bar',
          route: '/reports',
          roles: [
            'ROLE_ADMIN',
            'ROLE_COORDINATOR_FACULTY',
            'ROLE_COORDINATOR_SCHOOL',
          ],
        },
      ],
    };
  },
  computed: {
    userRole() {
      try {
        const token = localStorage.getItem('token');
        if (!token) return null;
        const decoded = jwt_decode(token);
        const role =
          decoded.role ||
          (decoded.authorities && decoded.authorities[0]?.authority);
        return role ? role.toUpperCase() : null;
      } catch (e) {
        console.error('[Sidebar] Erro ao decodificar token:', e);
        return null;
      }
    },
    filteredMenuItems() {
      return this.menuItems.filter((item) =>
        item.roles.includes(this.userRole)
      );
    },
  },
};
</script>

<style scoped>
.sidebar {
  width: 240px;
  background-color: #00923f; /* Verde Escuro */
  height: 100vh;
  padding: 20px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin-bottom: 20px;
}

.menu-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white; /* Texto Branco */
  font-weight: bold;
  padding: 10px 15px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.menu-link:hover {
  background-color: #007a33; /* Verde Claro */
}

.menu-icon {
  margin-right: 10px;
  font-size: 20px;
}

.menu-text {
  font-size: 16px;
}
</style>
