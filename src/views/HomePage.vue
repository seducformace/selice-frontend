<template>
  <div class="login-page">
    <div class="logo-container">
      <img
        src="@/assets/seduc.png"
        alt="Ceará Governo do Estado"
        class="logo"
      />
    </div>
    <h1>GOVERNO DO ESTADO DO CEARÁ</h1>
    <h2>SISTEMA DE GERENCIAMENTO DE ESTÁGIO CURRICULAR</h2>

    <div class="login-form">
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="email">Email</label>
          <input
            id="email"
            type="text"
            v-model="email"
            placeholder="Digite seu email"
            required
          />
        </div>

        <div class="input-group">
          <label for="password">Senha</label>
          <div class="password-wrapper">
            <input
              id="password"
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              placeholder="Digite sua senha"
              required
            />
            <button
              type="button"
              class="toggle-password"
              @click="togglePassword"
            >
              {{ showPassword ? '👁️' : '👁️‍🗨️' }}
            </button>
          </div>
        </div>

        <div class="button-group">
          <button type="submit" class="primary-button">Entrar</button>
        </div>
        <div class="helper-links">
          <a href="#">Gerar nova senha?</a>
        </div>
      </form>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    </div>

    <footer class="footer">
      <p>Central de Atendimento</p>
      <p>(85) 3101-7801 / 4541 / 3847</p>
      <p>atendimento@seduc.ce.gov.br</p>
    </footer>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HomePage',
  data() {
    return {
      email: '',
      password: '',
      showPassword: false, // Estado para alternar a visualização da senha
      errorMessage: '',
    };
  },
  methods: {
    async handleLogin() {
      if (!this.email || !this.password) {
        this.errorMessage = 'Preencha todos os campos.';
        return;
      }

      try {
        const response = await axios.post('http://localhost:8080/api/login', {
          email: this.email,
          password: this.password,
        });

        if (response.data && response.data.token) {
          localStorage.setItem('token', response.data.token);
          this.$router.push('/dashboard');
        } else {
          this.errorMessage = 'Erro ao realizar o login. Tente novamente.';
        }
      } catch (error) {
        this.errorMessage =
          error.response && error.response.data && error.response.data.message
            ? error.response.data.message
            : 'Erro ao realizar o login. Verifique suas credenciais.';
      }
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
    },
  },
};
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f4f4;
  font-family: Arial, sans-serif;
}

.logo-container {
  margin-bottom: 20px;
}

.logo {
  width: 150px;
  height: auto;
}

h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

h2 {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.login-form {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 14px;
  margin-bottom: 5px;
  color: #333;
}

.password-wrapper {
  display: flex;
  align-items: center;
  position: relative;
}

.password-wrapper input {
  flex: 1;
  padding-right: 35px;
}

.toggle-password {
  background: none;
  border: none;
  position: absolute;
  right: 10px;
  cursor: pointer;
  font-size: 18px;
  color: #006600;
}

input {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #006600;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
}

.primary-button {
  background-color: #006600;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.primary-button:hover {
  background-color: #004d00;
}

.helper-links {
  margin-top: 20px;
  text-align: center;
}

.helper-links a {
  color: #006600;
  text-decoration: none;
  font-size: 14px;
}

.helper-links a:hover {
  text-decoration: underline;
}

.footer {
  text-align: center;
  margin-top: 30px;
  font-size: 14px;
  color: #666;
}

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
