<template>
  <div class="login-container">
    <!-- Logo e Sigla do Projeto -->
    <div class="header-logo">
      <img :src="require('@/assets/seduc.png')" alt="Logo Governo do Ceará" />
      <h1>SELICE</h1>
      <p>Sistema de Estágio e Licenciatura do Ceará</p>
    </div>

    <!-- Cartão de Login -->
    <div class="login-card">
      <h2>Login</h2>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="email">Email:</label>
          <input id="email" type="email" v-model="email" required />
        </div>

        <div class="form-group">
          <label for="password">Senha:</label>
          <div class="password-wrapper">
            <input
              id="password"
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              required
            />
            <span
              class="toggle-password"
              @click="togglePassword"
              title="Mostrar/ocultar senha"
            >
              {{ showPassword ? '🙈' : '👁️' }}
            </span>
          </div>
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </div>
</template>
<script>
import { authService } from '@/services/api';
import jwt_decode from 'jwt-decode';
import { getDashboardRouteByRole } from '@/utils/redirectByRole';

export default {
  data() {
    return {
      email: '',
      password: '',
      showPassword: false,
      loading: false,
      errorMessage: '',
    };
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    async login() {
      this.loading = true;
      this.errorMessage = '';

      try {
        console.log('[LOGIN] Iniciando login...');

        const response = await authService.login(this.email, this.password);
        console.log('[LOGIN] Resposta recebida:', response);

        const token = response.token;
        if (!token) throw new Error('Token não recebido');

        localStorage.setItem('token', token);
        localStorage.setItem('userEmail', this.email);

        const decoded = jwt_decode(token);
        console.log('[LOGIN] Token decodificado:', decoded);

        // 🎯 Aqui garantimos que funcione tanto com `role` direto quanto com `authorities`
        let role = decoded.role;
        if (!role && Array.isArray(decoded.authorities)) {
          const first = decoded.authorities[0];
          role = typeof first === 'string' ? first : first.authority;
        }

        console.log('[LOGIN] Role final:', role);

        if (!role) throw new Error('Papel não encontrado no token.');

        const route = getDashboardRouteByRole(role);
        console.log('[LOGIN] Redirecionando para:', route);

        // Redireciona de forma segura
        this.$router.push(route);
      } catch (error) {
        console.error('[LOGIN] Erro geral:', error);
        this.errorMessage =
          error?.response?.data?.message ||
          error.message ||
          'Erro ao realizar login.';
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  background-color: #f3f3f3;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 30px;
}

/* Área da logo e sigla */
.header-logo {
  text-align: center;
  margin-bottom: 20px;
}

.header-logo img {
  height: 80px;
  margin-bottom: 10px;
}

.header-logo h1 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #2d862d;
}

.header-logo p {
  margin: 0;
  font-size: 14px;
  color: #333;
}

/* Cartão de login */
.login-card {
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  width: 320px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-card h2 {
  margin-bottom: 1.5rem;
  font-weight: bold;
  color: #000;
}

.form-group {
  margin-bottom: 1rem;
  text-align: left;
}

input {
  width: 100%;
  padding: 8px 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 14px;
  box-sizing: border-box;
}

.password-wrapper {
  position: relative;
}

.password-wrapper input {
  padding-right: 36px; /* espaço para o botão 👁️ */
}

.toggle-password {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 16px;
  user-select: none;
  color: #555;
  padding: 2px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #2d862d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>
