<template>
  <div class="professors-page">
    <!-- Cabeçalho (Header) -->
    <Header />

    <div class="content-wrapper">
      <div class="content">
        <!-- Barra Superior: Botão Home + Título -->
        <div class="header-actions">
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Professores</h1>
        </div>

        <!-- Ações: Botão para adicionar professor e campo de busca -->
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
                <th>Disciplina</th>
                <th>Escolas</th>
                <th>Orientados (0-10)</th>
                <!-- Removemos o campo “Em Andamento” -->
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(professor, index) in filteredProfessors" :key="index">
                <td>{{ professor.name }}</td>
                <td>{{ professor.registration }}</td>
                <td>{{ professor.certification }}</td>
                <td>{{ professor.discipline }}</td>
                <!-- Exibe array de escolas separadas por vírgula -->
                <td>{{ professor.schools.join(', ') }}</td>
                <td>{{ professor.orientedStudents }}</td>
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

      <!-- Modal (2 colunas) para adicionar/editar professor -->
      <div v-if="isModalOpen" class="modal-overlay">
        <div class="modal-form-box">
          <h2>{{ isEditing ? 'Editar Professor' : 'Adicionar Professor' }}</h2>
          <!-- Formulário em grid de 2 colunas -->
          <form @submit.prevent="saveProfessor" class="professor-form">
            <!-- Nome -->
            <div class="form-group">
              <label for="name">Nome</label>
              <input
                id="name"
                v-model="currentProfessor.name"
                type="text"
                required
              />
            </div>

            <!-- Matrícula -->
            <div class="form-group">
              <label for="registration">Matrícula</label>
              <input
                id="registration"
                v-model="currentProfessor.registration"
                type="text"
                required
              />
            </div>

            <!-- Certificação (Especialista, Graduado, Mestrado, Doutorado) -->
            <div class="form-group">
              <label for="certification">Certificação</label>
              <select
                id="certification"
                v-model="currentProfessor.certification"
              >
                <option value="Especialista">Especialista</option>
                <option value="Graduado">Graduado</option>
                <option value="Mestrado">Mestrado</option>
                <option value="Doutorado">Doutorado</option>
              </select>
            </div>

            <!-- Disciplina (licenciaturas) -->
            <div class="form-group">
              <label for="discipline">Disciplina</label>
              <select
                id="discipline"
                v-model="currentProfessor.discipline"
                required
              >
                <option value="" disabled>Selecione uma Licenciatura</option>
                <option v-for="lic in licensureCourses" :key="lic" :value="lic">
                  {{ lic }}
                </option>
              </select>
            </div>

            <!-- Escolas: múltiplas seleções via select + botão “Adicionar” -->
            <div class="form-group">
              <label for="selectedSchool">Escolas (Públicas CE)</label>
              <div class="school-select-wrapper">
                <select id="selectedSchool" v-model="selectedSchool">
                  <option value="" disabled>Selecione uma Escola</option>
                  <option
                    v-for="school in cearaPublicSchools"
                    :key="school"
                    :value="school"
                  >
                    {{ school }}
                  </option>
                </select>
                <button
                  type="button"
                  class="add-school-button"
                  @click="addSchool"
                >
                  Adicionar
                </button>
              </div>
              <!-- Exibe lista de escolas já adicionadas -->
              <ul class="school-list" v-if="currentProfessor.schools.length">
                <li v-for="(sch, idx) in currentProfessor.schools" :key="idx">
                  {{ sch }}
                </li>
              </ul>
            </div>

            <!-- Orientados: caixa suspensa de 0..10 -->
            <div class="form-group">
              <label for="orientedStudents">Orientados (0-10)</label>
              <select
                id="orientedStudents"
                v-model.number="currentProfessor.orientedStudents"
              >
                <option v-for="n in 11" :key="n" :value="n - 1">
                  {{ n - 1 }}
                </option>
              </select>
            </div>

            <!-- Botões (Salvar/Cancelar) -->
            <div class="form-buttons">
              <button type="submit" class="save-button">Salvar</button>
              <button type="button" class="cancel-button" @click="closeModal">
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Rodapé fixo -->
    <Footer class="footer-fixed" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

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
        certification: '',
        discipline: '',
        schools: [],
        orientedStudents: 0,
      },
      professors: [],
      licensureCourses: [
        'Pedagogia',
        'Matemática',
        'Física',
        'Química',
        'Biologia',
        'História',
        'Geografia',
        'Português',
        'Inglês',
        'Espanhol',
        'Educação Física',
      ],
      cearaPublicSchools: [
        'EEFM Paulo Sarasate',
        'EEFM Governador Adauto Bezerra',
        'EEFM Liceu do Ceará',
        'EEFM Domingos Sávio',
        'EEFM César Cals',
      ],
      selectedSchool: '',
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
    goToDashboard() {
      this.$router.push('/dashboard');
    },
    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.resetCurrentProfessor();
    },
    closeModal() {
      this.isModalOpen = false;
    },
    editProfessor(index) {
      this.currentProfessor = { ...this.professors[index] };
      this.isEditing = true;
      this.isModalOpen = true;
    },
    async saveProfessor() {
      try {
        const payload = {
          name: this.currentProfessor.name,
          registration: this.currentProfessor.registration,
          degree: this.currentProfessor.certification,
          discipline: this.currentProfessor.discipline,
          schools: this.currentProfessor.schools,
          orientedStudents: this.currentProfessor.orientedStudents,
          // Adicione outros campos aqui se necessário
        };

        if (this.isEditing) {
          // PUT para atualizar professor
          await api.put(`/professors/${this.currentProfessor.id}`, payload);
        } else {
          // POST para criar novo professor
          await api.post('/professors', payload);
        }

        this.closeModal();
        this.fetchProfessors(); // Recarrega a lista do banco
      } catch (error) {
        console.error('Erro ao salvar professor:', error);
        alert(
          'Erro ao salvar professor. Verifique os dados e tente novamente.'
        );
      }
    },
    resetCurrentProfessor() {
      this.currentProfessor = {
        name: '',
        registration: '',
        certification: '',
        discipline: '',
        schools: [],
        orientedStudents: 0,
      };
      this.selectedSchool = '';
    },
    async fetchProfessors() {
      try {
        const response = await api.get('/professors');
        this.professors = response.data;
      } catch (error) {
        console.error('Erro ao carregar professores:', error);
      }
    },
    addSchool() {
      if (
        this.selectedSchool &&
        !this.currentProfessor.schools.includes(this.selectedSchool)
      ) {
        this.currentProfessor.schools.push(this.selectedSchool);
      }
      this.selectedSchool = '';
    },
    deleteProfessor(index) {
      this.professors.splice(index, 1);
    },
  },
  mounted() {
    this.fetchProfessors();
  },
};
</script>

<style scoped>
/* ======== Layout Geral ======== */
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
/* Header (Home + Título) */
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

/* Ações (Adicionar + Busca) */
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-bottom: 20px;
}
.add-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}
.search-input {
  padding: 10px;
  width: 250px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Tabela de Professores */
.table-container {
  overflow-x: auto;
  margin-bottom: 80px; /* espaço pro rodapé */
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
  gap: 8px;
}
.edit-button {
  background-color: #ccc;
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
.delete-button {
  background-color: #ff6565;
  color: white;
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

/* ======== MODAL (2 colunas) ======== */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
  padding: 20px;
  overflow-y: auto;
}
.modal-form-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 900px;
  width: 90%;
  box-sizing: border-box;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

/* Formulário grid 2 colunas */
.professor-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 10px;
}

/* Campos do formulário */
.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-weight: bold;
  margin-bottom: 5px;
}
.form-group input,
.form-group select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Sessão de Escolas: select + botão “Adicionar” + lista */
.school-select-wrapper {
  display: flex;
  gap: 8px;
  align-items: center;
}
.add-school-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
.school-list {
  margin-top: 8px;
  list-style: disc inside;
}

/* Botões Salvar/Cancelar */
.form-buttons {
  display: flex;
  justify-content: space-between;
  grid-column: span 2; /* ocupa 2 colunas */
  margin-top: 15px;
}
.save-button,
.cancel-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}
.save-button {
  background-color: #00923f;
  color: white;
}
.cancel-button {
  background-color: #d9534f;
  color: white;
}

/* Responsivo (1 coluna abaixo de 600px) */
@media (max-width: 600px) {
  .professor-form {
    grid-template-columns: 1fr;
  }
  .form-buttons {
    flex-direction: column;
    gap: 10px;
  }
}

/* ======== Rodapé fixo ======== */
.footer-fixed {
  width: 100%;
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 999;
}
</style>
