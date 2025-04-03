<template>
  <header class="header">
    <!-- Área da Logo e Título -->
    <div class="logo-container">
      <img
        :src="require('@/assets/seduc.png')"
        alt="Logo Governo do Ceará"
        class="logo"
      />
      <div class="title">
        <h1>Sistema de Gerenciamento de Estágio Curricular</h1>
        <h2></h2>
      </div>
    </div>

    <!-- Área do Usuário à Direita -->
    <div class="user-menu" @click="toggleMenu">
      <div class="avatar">{{ userInitial }}</div>
      <div class="dropdown" v-if="menuOpen">
        <ul>
          <li @click="logout">Sair do sistema</li>
        </ul>
      </div>
    </div>
  </header>
</template>

<script>
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
  },
};
</script>

<style scoped>
.header {
  background-color: #f5f5f5;
  padding: 20px 30px;
  display: flex;
  justify-content: space-between; /* Agora divide logo e botão */
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
  color: #333333;
  margin: 0;
  font-weight: bold;
}

.title h2 {
  font-size: 16px;
  color: #666666;
  margin: 0;
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
