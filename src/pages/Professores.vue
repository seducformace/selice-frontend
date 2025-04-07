<template>
  <div class="professors-page">
    <!-- Cabeçalho -->
    <Header />
    <div class="content-wrapper">
      <div class="content">
        <!-- Título da Página com Ícone Home -->
        <div class="header-actions">
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Professores</h1>
        </div>

        <!-- Ações -->
        <div class="actions">
          <button class="add-button" @click="openModal">
            Adicionar Novo Professor
          </button>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar professor..."
            class="search-input"
          />
        </div>

        <!-- Tabela de Professores -->
        <div class="table-container">
          <table class="professors-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Matrícula</th>
                <th>Certificação</th>
                <th>Disciplinas</th>
                <th>Escolas</th>
                <th>Orientados</th>
                <th>Em Andamento</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(professor, index) in filteredProfessors" :key="index">
                <td>{{ professor.name }}</td>
                <td>{{ professor.registration }}</td>
                <td>{{ professor.certification }}</td>
                <td>{{ professor.subjects.join(', ') }}</td>
                <td>{{ professor.schools.join(', ') }}</td>
                <td>{{ professor.orientedStudents }}</td>
                <td>{{ professor.studentsInProgress }}</td>
                <td class="actions-buttons">
                  <button class="edit-button" @click="editProfessor(index)">
                    Editar
                  </button>
                  <button class="delete-button" @click="deleteProfessor(index)">
                    Excluir
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Modal -->
      <div v-if="isModalOpen" class="modal-overlay">
        <div class="modal modal-aligned">
          <h2>{{ isEditing ? 'Editar Professor' : 'Adicionar Professor' }}</h2>
          <form @submit.prevent="saveProfessor">
            <div class="form-group">
              <label for="name">Nome</label>
              <input
                id="name"
                v-model="currentProfessor.name"
                type="text"
                required
              />
            </div>

            <div class="form-group">
              <label for="registration">Matrícula</label>
              <input
                id="registration"
                v-model="currentProfessor.registration"
                type="text"
                required
              />
            </div>

            <div class="form-group">
              <label for="certification">Certificação</label>
              <select v-model="currentProfessor.certification">
                <option value="Mestrado">Mestrado</option>
                <option value="Doutorado">Doutorado</option>
              </select>
            </div>

            <button type="submit" class="save-button">Salvar</button>
            <button type="button" class="cancel-button" @click="closeModal">
              Cancelar
            </button>
          </form>
        </div>
      </div>
    </div>

    <!-- Rodapé -->
    <Footer class="footer-fixed" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api'; // ✅ Importa a instância configurada do Axios

export default {
  name: 'ProfessorsPage',
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      currentProfessor: {
        name: '',
        registration: '',
        certification: 'Mestrado',
        subjects: [],
        schools: [],
        orientedStudents: 0,
        studentsInProgress: 0,
      },
      professors: [], // ✅ Agora a lista começa vazia e será preenchida via API
    };
  },
  computed: {
    filteredProfessors() {
      return this.professors.filter((professor) =>
        professor.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.resetCurrentProfessor();
    },
    closeModal() {
      this.isModalOpen = false;
    },
    editProfessor(index) {
      this.isEditing = true;
      this.currentProfessor = { ...this.professors[index] };
      this.isModalOpen = true;
    },
    saveProfessor() {
      if (this.isEditing) {
        const index = this.professors.findIndex(
          (p) => p.registration === this.currentProfessor.registration
        );
        this.professors[index] = { ...this.currentProfessor };
      } else {
        this.professors.push({ ...this.currentProfessor });
      }
      this.closeModal();
    },
    deleteProfessor(index) {
      this.professors.splice(index, 1);
    },
    goToDashboard() {
      this.$router.push('/dashboard');
    },
    resetCurrentProfessor() {
      this.currentProfessor = {
        name: '',
        registration: '',
        certification: 'Mestrado',
        subjects: [],
        schools: [],
        orientedStudents: 0,
        studentsInProgress: 0,
      };
    },
    async fetchProfessors() {
      try {
        const response = await api.get('/professors');
        this.professors = response.data; // ✅ Lista atualizada com o que vem do backend
      } catch (error) {
        console.error('Erro ao buscar professores:', error);
      }
    },
  },
  mounted() {
    this.fetchProfessors(); // ✅ Busca professores ao carregar a página
  },
};
</script>

<style scoped>
.content-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  max-width: 1200px;
  margin: 20px auto;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.home-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.add-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.search-input {
  padding: 10px;
  width: 300px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.professors-table {
  width: 100%;
  border-collapse: collapse;
}

.professors-table th,
.professors-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.professors-table th {
  background-color: #00923f;
  color: white;
  font-weight: bold;
}

.actions-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.save-button {
  background-color: #00923f;
  color: white;
}

.cancel-button {
  background-color: #d9534f;
  color: white;
}
</style>
