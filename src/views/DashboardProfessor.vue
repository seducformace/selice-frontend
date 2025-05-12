<template>
  <div class="dashboard-professor">
    <!-- Header é global, não precisa ser incluído aqui -->

    <div class="content-wrapper">
      <!-- Cabeçalho com botão home e título -->
      <div class="header-bar">
        <div class="left">
          <button class="home-button" @click="goToHome">
            <i class="fas fa-home"></i>
          </button>
        </div>
        <h1 class="title">Painel do Professor</h1>
      </div>

      <!-- Info do professor (modelo coordenador faculdade) -->
      <div
        v-if="professor"
        style="
          background-color: #f2f9ff;
          padding: 15px;
          border-radius: 8px;
          margin: 20px;
          line-height: 1.8;
        "
      >
        <p><strong>Nome:</strong> {{ professor.name }}</p>
        <p><strong>CPF:</strong> {{ professor.cpf }}</p>
        <p><strong>Email:</strong> {{ professor.email }}</p>
        <p v-if="professor.college?.name">
          <strong>Faculdade:</strong> {{ professor.college.name }}
        </p>
        <p v-if="professor.schools?.length">
          <strong>Escola(s):</strong>
          {{ professor.schools.map((s) => s.name).join(', ') }}
        </p>
      </div>

      <!-- Cards -->
      <div class="dashboard-cards">
        <div class="card" @click="goTo('students-by-teacher-test')">
          <i class="fas fa-users icon"></i>
          <p>Meus Estudantes</p>
        </div>

        <div class="card" @click="goTo('diary-test')">
          <i class="fas fa-calendar-check icon"></i>
          <p>Diário de Presença</p>
        </div>

        <div class="card" @click="goTo('reports')">
          <i class="fas fa-chart-line icon"></i>
          <p>Relatórios</p>
        </div>

        <div class="card" @click="goTo('settings')">
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

export default {
  name: 'DashboardProfessor',
  components: { Footer },
  data() {
    return {
      professor: null,
    };
  },
  methods: {
    goTo(routeName) {
      this.$router.push(`/${routeName}`);
    },
    goToHome() {
      this.$router.push('/dashboard-professor');
    },
    async fetchProfessor() {
      try {
        const response = await api.get('/teachers/me');
        this.professor = response.data;
      } catch (err) {
        console.error('Erro ao carregar dados do professor:', err);
      }
    },
  },
  created() {
    this.fetchProfessor();
  },
};
</script>

<style scoped>
.dashboard-professor {
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
  color: #1e1e1e;
}

.home-button {
  background-color: #00923f;
  color: white;
  width: 42px;
  height: 42px;
  border: none;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s;
}
.home-button:hover {
  background-color: #007a33;
}

.professor-info {
  background-color: #f0f8ff;
  border-radius: 10px;
  padding: 20px 30px;
  margin-bottom: 30px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.06);
  font-size: 15px;
  color: #333;
  line-height: 1.6;
  text-align: center;
  font-weight: 500;
}

.dashboard-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  align-items: stretch;
  margin-top: 20px;
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
  font-size: 15px;
  font-weight: 600;
  color: #333;
}
.info-box {
  background-color: #f0f8ff;
  padding: 15px;
  border-radius: 6px;
  margin: 20px 0;
}
</style>
