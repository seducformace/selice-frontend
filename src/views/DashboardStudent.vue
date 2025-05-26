<template>
  <div class="student-dashboard">
    <div class="content-wrapper">
      <!-- Cabeçalho -->
      <div class="header-bar">
        <div class="left">
          <button class="home-button" @click="goToHome">
            <i class="fas fa-home"></i>
          </button>
        </div>
        <h1 class="title">Painel do Estudante</h1>
      </div>

      <!-- Informações em duas colunas -->
      <div class="student-info-box" v-if="student">
        <div class="student-info-grid">
          <div class="info-item">
            <strong>Aluno:</strong> {{ student.name }}
          </div>
          <div class="info-item"><strong>CPF:</strong> {{ student.cpf }}</div>
          <div class="info-item">
            <strong>Curso:</strong> {{ student.course }}
          </div>
          <div class="info-item">
            <strong>Faculdade:</strong>
            {{ student.college?.name || 'Não vinculada' }}
          </div>
          <div class="info-item">
            <strong>Escola de Estágio:</strong>
            {{ student.school?.name || 'Não vinculada' }}
          </div>
        </div>
      </div>

      <!-- Cards -->
      <div class="dashboard-cards">
        <div class="card" @click="goToDiary">
          <i class="fas fa-calendar-alt icon"></i>
          <p>Espelho de Presença</p>
        </div>
        <div class="card" @click="goToReports">
          <i class="fas fa-file-alt icon"></i>
          <p>Meus Relatórios</p>
        </div>
        <div class="card" @click="goToSettings">
          <i class="fas fa-cog icon"></i>
          <p>Configurações</p>
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';
import { getDashboardRouteByRole } from '@/utils/redirectByRole';
import * as jwtDecode from 'jwt-decode';

export default {
  name: 'DashboardStudent',
  components: { Footer },
  data() {
    return {
      student: null,
    };
  },
  methods: {
    goToHome() {
      const token = localStorage.getItem('token');
      if (token) {
        const { role } = jwtDecode.default(token);
        const route = getDashboardRouteByRole(role);
        this.$router.push(route);
      } else {
        this.$router.push('/login');
      }
    },
    goToDiary() {
      this.$router.push('/diary-view');
    },
    goToReports() {
      this.$router.push('/student-reports');
    },
    goToSettings() {
      alert('Área de configurações em construção.');
    },
    async fetchStudentData() {
      try {
        const response = await api.get('/students/me');
        this.student = response.data;
      } catch (error) {
        console.error('Erro ao buscar estudante autenticado:', error);
        alert('Erro ao carregar os dados do estudante.');
      }
    },
  },
  mounted() {
    this.fetchStudentData();
  },
};
</script>

<style scoped>
/* (todo seu estilo original mantido abaixo) */
.student-dashboard {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
}
.content-wrapper {
  max-width: 1200px;
  margin: auto;
  flex: 1;
}
.header-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
  position: relative;
}
.header-bar .left {
  position: absolute;
  left: 0;
}
.title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  text-align: center;
}
.home-button {
  background-color: #00923f;
  color: white;
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.home-button:hover {
  background-color: #007a33;
}
.student-info-box {
  background-color: #f0f8ff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 30px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.08);
}
.student-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px 30px;
  font-size: 15px;
  line-height: 1.6;
}
.info-item {
  text-align: left;
}
.dashboard-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  align-items: stretch;
  margin-bottom: 40px;
}
.card {
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 30px 20px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
  width: 240px;
  height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.card:hover {
  transform: translateY(-5px);
  background-color: #f7faff;
}
.icon {
  font-size: 30px;
  margin-bottom: 12px;
  color: #007bff;
}
.card p {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}
</style>
