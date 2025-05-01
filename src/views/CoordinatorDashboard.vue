<template>
  <div class="coordinator-dashboard">
    <div class="content-wrapper">
      <!-- Cabeçalho com título centralizado e botão à esquerda -->
      <div class="header-bar">
        <button class="home-button" @click="goToHome">
          <i class="fas fa-home"></i>
        </button>
        <h1 class="dashboard-title">Painel do Coordenador</h1>
      </div>

      <!-- Dados do coordenador -->
      <div class="coordinator-info" v-if="coordinator">
        <p><strong>Nome:</strong> {{ coordinator.name }}</p>
        <p><strong>CPF:</strong> {{ coordinator.cpf }}</p>
        <p><strong>Email:</strong> {{ coordinator.email }}</p>
        <p><strong>Instituição:</strong> {{ institutionName }}</p>
      </div>

      <!-- Cards principais -->
      <div class="dashboard-cards">
        <div class="card" @click="goToStudents">
          <i class="fas fa-user-graduate icon"></i>
          <p>Gerenciar Estudantes</p>
        </div>

        <div class="card" v-if="isSchoolCoordinator" @click="goToProfessors">
          <i class="fas fa-chalkboard-teacher icon"></i>
          <p>Gerenciar Professores</p>
        </div>

        <div class="card" @click="goToReports">
          <i class="fas fa-chart-line icon"></i>
          <p>Relatórios</p>
        </div>

        <div class="card" @click="goToSettings">
          <i class="fas fa-cogs icon"></i>
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
  name: 'CoordinatorDashboard',
  components: { Footer },
  data() {
    return {
      coordinator: null,
    };
  },
  computed: {
    institutionName() {
      if (!this.coordinator) return '';
      if (this.coordinator.faculty) {
        return this.coordinator.faculty.name;
      } else if (this.coordinator.school) {
        return this.coordinator.school.name;
      } else {
        return 'Instituição não vinculada';
      }
    },
    isSchoolCoordinator() {
      return this.coordinator && this.coordinator.school;
    },
  },
  methods: {
    goToHome() {
      this.$router.push('/dashboard-coordinator');
    },
    goToStudents() {
      if (this.coordinator && this.coordinator.faculty) {
        this.$router.push('/students');
      } else if (this.coordinator && this.coordinator.school) {
        this.$router.push({
          path: '/students',
          query: { schoolId: this.coordinator.school.id },
        });
      } else {
        alert('Nenhuma instituição vinculada.');
      }
    },
    goToProfessors() {
      if (this.coordinator && this.coordinator.school) {
        this.$router.push({
          path: '/professors',
          query: { schoolId: this.coordinator.school.id },
        });
      } else {
        alert('Apenas coordenadores de escola gerenciam professores.');
      }
    },
    goToReports() {
      if (
        this.coordinator &&
        (this.coordinator.school || this.coordinator.faculty)
      ) {
        this.$router.push('/coordinator/reports');
      } else {
        alert('Nenhuma instituição vinculada.');
      }
    },
    goToSettings() {
      this.$router.push('/coordinator/settings');
    },
    async fetchAuthenticatedCoordinator() {
      try {
        const response = await api.get('/coordinators/me');
        this.coordinator = response.data;
      } catch (error) {
        console.error(
          'Erro ao carregar dados do coordenador autenticado:',
          error
        );
        alert('Erro ao buscar informações do coordenador. Tente novamente.');
      }
    },
  },
  mounted() {
    this.fetchAuthenticatedCoordinator();
  },
};
</script>

<style scoped>
/* SEUS ESTILOS ATUAIS (mantidos exatamente) */
.coordinator-dashboard {
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
  position: relative;
  margin-bottom: 20px;
}

h1 {
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
  position: absolute;
  left: 0;
}

.home-button:hover {
  background-color: #007a33;
}

.coordinator-info {
  background-color: #f0f8ff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 30px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.08);
  font-size: 15px;
  line-height: 1.6;
}

.dashboard-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  align-items: stretch;
  margin-top: 20px;
}

.dashboard-title {
  font-size: 22px;
  font-weight: bold;
  margin: 0 auto;
  text-align: center;
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
