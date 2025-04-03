<template>
  <aside class="sidebar">
    <ul class="menu">
      <li v-for="(item, index) in menuItems" :key="index" class="menu-item">
        <router-link :to="item.route" class="menu-link">
          <i :class="item.icon" class="menu-icon"></i>
          <span class="menu-text">{{ item.label }}</span>
        </router-link>
      </li>
    </ul>
  </aside>
</template>

<script>
export default {
  name: 'Sidebar',
  data() {
    return {
      menuItems: [
        {
          label: 'Dashboard',
          icon: 'fas fa-home',
          route: '/',
        },
        {
          label: 'Faculdades',
          icon: 'fas fa-university',
          route: '/faculties',
        },
        {
          label: 'Coordenadores',
          icon: 'fas fa-chalkboard-teacher',
          route: '/coordinators',
        },
        {
          label: 'Alunos',
          icon: 'fas fa-user-graduate',
          route: '/students',
        },
        {
          label: 'Professores',
          icon: 'fas fa-user-tie',
          route: '/professors',
        },
        {
          label: 'Escolas',
          icon: 'fas fa-school',
          route: '/schools',
        },
        {
          label: 'Relatórios',
          icon: 'fas fa-chart-bar',
          route: '/reports',
        },
      ],
    };
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
