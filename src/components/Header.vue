<template>
  <header class="header">
    <div class="logo-container">
      <img
        @click="goHome"
        :src="require('@/assets/seduc.png')"
        alt="Logo Governo do Ceará"
        class="logo"
        style="cursor: pointer"
      />
      <div class="title">
        <h1>Sistema de Gerenciamento de Estágio Curricular</h1>
      </div>
    </div>

    <div class="user-menu" @click="toggleMenu">
      <div class="avatar">{{ userInitial }}</div>
      <div class="dropdown" v-if="menuOpen">
        <ul>
          <li v-if="canViewStudents" @click="$router.push('/students')">
            Estudantes
          </li>
          <li @click="logout">Sair do sistema</li>
        </ul>
      </div>
    </div>
  </header>
</template>

<script>
import jwt_decode from 'jwt-decode';

export default {
  name: 'Header',
  data() {
    return {
      menuOpen: false,
    };
  },
  computed: {
    userInitial() {
      const email = localStorage.getItem('userEmail') || 'U';
      return email.charAt(0).toUpperCase();
    },
    canViewStudents() {
      const token = localStorage.getItem('token');
      if (!token) return false;

      try {
        const decoded = jwt_decode(token);
        let role = decoded.role;

        if (!role && Array.isArray(decoded.authorities)) {
          const firstAuth = decoded.authorities[0];
          role =
            typeof firstAuth === 'object' ? firstAuth.authority : firstAuth;
        }

        const normalizedRole = (role || '').toUpperCase();

        // Define quem pode ver o menu de Estudantes:
        return [
          'ROLE_ADMIN',
          'ROLE_COORDINATOR_FACULTY',
          'ROLE_COORDINATOR_SCHOOL',
          'ROLE_TEACHER',
        ].includes(normalizedRole);
      } catch (e) {
        return false;
      }
    },
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('userEmail');
      this.$router.push('/login');
    },
    goHome() {
      const token = localStorage.getItem('token');

      if (!token) {
        console.warn('[goHome] Token ausente.');
        return this.$router.push('/login');
      }

      try {
        const decoded = jwt_decode(token);

        let role = decoded.role;

        if (!role && Array.isArray(decoded.authorities)) {
          const firstAuth = decoded.authorities[0];
          role =
            typeof firstAuth === 'object' ? firstAuth.authority : firstAuth;
        }

        const normalizedRole = (role || '').toUpperCase();
        let targetRoute = '/login';

        switch (normalizedRole) {
          case 'ROLE_ADMIN':
            targetRoute = '/dashboard';
            break;
          case 'ROLE_COORDINATOR_FACULTY':
            targetRoute = '/dashboard-coordinator-faculty';
            break;
          case 'ROLE_COORDINATOR_SCHOOL':
            targetRoute = '/dashboard-coordinator-school';
            break;
          case 'ROLE_TEACHER':
            targetRoute = '/dashboard-professor';
            break;
          case 'ROLE_STUDENT':
            targetRoute = '/dashboard-student';
            break;
        }

        if (this.$route.path !== targetRoute) {
          console.log('[goHome] Redirecionando para:', targetRoute);
          this.$router.push(targetRoute);
        }
      } catch (error) {
        console.error('[goHome] Erro ao decodificar token:', error);
        this.$router.push('/login');
      }
    },
  },
};
</script>

<style scoped>
.header {
  background-color: #f5f5f5;
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  flex: 1;
}

.logo {
  height: 80px;
  margin-bottom: 10px;
}

.title h1 {
  font-size: 20px;
  color: #333;
  margin: 0;
  font-weight: bold;
}

.user-menu {
  position: relative;
  cursor: pointer;
}

.avatar {
  background-color: #3d4f5c;
  color: white;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dropdown {
  position: absolute;
  right: 0;
  top: 50px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 100;
}

.dropdown ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.dropdown li {
  padding: 10px 20px;
  cursor: pointer;
}

.dropdown li:hover {
  background-color: #f2f2f2;
}
</style>
