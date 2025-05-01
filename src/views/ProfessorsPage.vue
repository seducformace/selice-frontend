<template>
  <div class="professors-page">
    <div class="content-wrapper">
      <div class="content">
        <!-- Cabeçalho -->
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
                <th>CPF</th>
                <th>E-mail</th>
                <th>Matrícula</th>
                <th>Certificação</th>
                <th>Disciplina</th>
                <th>Escolas</th>
                <th>Orientados (0-10)</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(professor, index) in filteredProfessors" :key="index">
                <td>{{ professor.name }}</td>
                <td>{{ professor.cpf }}</td>
                <td>{{ professor.email }}</td>
                <td>{{ professor.registration }}</td>
                <td>{{ professor.qualification }}</td>
                <td>{{ professor.discipline }}</td>
                <td>
                  <ul v-if="professor.schools && professor.schools.length">
                    <li v-for="s in professor.schools" :key="s.id">
                      {{ s.name }}
                    </li>
                  </ul>
                  <span v-else>—</span>
                </td>
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

      <!-- Modal de Cadastro -->
      <div v-if="isModalOpen" class="modal-overlay">
        <div class="modal-form-box">
          <h2>{{ isEditing ? 'Editar Professor' : 'Adicionar Professor' }}</h2>
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

            <!-- CPF -->
            <div class="form-group">
              <label for="cpf">CPF</label>
              <input
                id="cpf"
                v-model="currentProfessor.cpf"
                type="text"
                required
              />
            </div>

            <!-- E-mail -->
            <div class="form-group">
              <label for="email">E-mail</label>
              <input
                id="email"
                v-model="currentProfessor.email"
                type="email"
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

            <!-- Certificação -->
            <div class="form-group">
              <label for="qualification">Certificação</label>
              <select
                id="qualification"
                v-model="currentProfessor.qualification"
                required
              >
                <option value="" disabled>Selecione</option>
                <option value="Especialista">Especialista</option>
                <option value="Graduado">Graduado</option>
                <option value="Mestrado">Mestrado</option>
                <option value="Doutorado">Doutorado</option>
              </select>
            </div>

            <!-- Disciplina -->
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

            <!-- Escolas -->
            <div class="form-group">
              <label for="selectedSchool">Escolas (Públicas CE)</label>
              <div class="school-select-wrapper">
                <select id="selectedSchool" v-model="selectedSchool">
                  <option value="" disabled>Selecione uma Escola</option>
                  <option
                    v-for="school in cearaPublicSchools"
                    :key="school.id"
                    :value="school"
                  >
                    {{ school.name }}
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

              <!-- Escolas Adicionadas -->
              <ul class="school-list" v-if="currentProfessor.schools.length">
                <li
                  v-for="(sch, idx) in currentProfessor.schools"
                  :key="'added-' + idx"
                >
                  {{ sch.name }}
                  <button
                    type="button"
                    class="remove-school"
                    @click="removeSchool(idx)"
                  >
                    Remover
                  </button>
                </li>
              </ul>
            </div>

            <!-- Orientados -->
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

            <!-- Botões -->
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

    <Footer class="footer-fixed" />
  </div>
</template>
<script>
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

export default {
  name: 'ProfessorsPage',
  components: { Footer },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      currentProfessor: {
        name: '',
        cpf: '',
        email: '',
        registration: '',
        qualification: '',
        discipline: '',
        schools: [],
        orientedStudents: 0,
        studentsInProgress: 0,
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
      cearaPublicSchools: [],
      selectedSchool: null,
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
      const token = localStorage.getItem('token');
      const role = this.parseJwt(token)?.role;

      switch (role) {
        case 'ADMIN':
          this.$router.push('/dashboard-admin');
          break;
        case 'COORDINATOR_FACULTY':
          this.$router.push('/dashboard-coordinator-faculty');
          break;
        case 'COORDINATOR_SCHOOL':
          this.$router.push('/dashboard-coordinator-school');
          break;
        case 'TEACHER':
          this.$router.push('/dashboard-professor');
          break;
        case 'STUDENT':
          this.$router.push('/dashboard-student');
          break;
        default:
          this.$router.push('/');
      }
    },
    parseJwt(token) {
      if (!token) return {};
      try {
        return JSON.parse(atob(token.split('.')[1]));
      } catch (e) {
        return {};
      }
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
      this.currentProfessor = JSON.parse(
        JSON.stringify(this.professors[index])
      );
      this.isEditing = true;
      this.isModalOpen = true;
    },
    async saveProfessor() {
      try {
        if (!this.currentProfessor.schools.length) {
          alert('Por favor, selecione ao menos uma escola.');
          return;
        }

        const payload = {
          name: this.currentProfessor.name,
          cpf: this.currentProfessor.cpf,
          email: this.currentProfessor.email,
          registration: this.currentProfessor.registration,
          qualification: this.currentProfessor.qualification,
          discipline: this.currentProfessor.discipline,
          schools: this.currentProfessor.schools.map((s) => ({ id: s.id })),
          orientedStudents: this.currentProfessor.orientedStudents,
          studentsInProgress: this.currentProfessor.studentsInProgress,
        };

        if (this.isEditing) {
          await api.put(`/teachers/${this.currentProfessor.id}`, payload);
        } else {
          await api.post('/teachers', payload);
        }

        this.closeModal();
        await this.fetchProfessors();
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
        cpf: '',
        email: '',
        registration: '',
        qualification: '',
        discipline: '',
        schools: [],
        orientedStudents: 0,
        studentsInProgress: 0,
      };
      this.selectedSchool = null;
    },
    async fetchProfessors() {
      try {
        const token = localStorage.getItem('token');
        const role = this.parseJwt(token)?.role;
        const params = {};

        if (role === 'COORDINATOR_SCHOOL') {
          if (this.$route.query.schoolId) {
            params.schoolId = this.$route.query.schoolId;
          }
        }

        if (role === 'COORDINATOR_FACULTY') {
          if (this.$route.query.facultyId) {
            params.facultyId = this.$route.query.facultyId;
          }
        }

        const response = await api.get('/teachers', { params });
        this.professors = response.data;
      } catch (error) {
        console.error('Erro ao carregar professores:', error);
        alert('Erro ao carregar dados dos professores.');
      }
    },
    async fetchSchools() {
      try {
        const response = await api.get('/schools');
        this.cearaPublicSchools = response.data;
      } catch (error) {
        console.error('Erro ao carregar escolas:', error);
      }
    },
    addSchool() {
      if (
        this.selectedSchool &&
        !this.currentProfessor.schools.some(
          (s) => s.id === this.selectedSchool.id
        )
      ) {
        this.currentProfessor.schools.push(this.selectedSchool);
      }
      this.selectedSchool = null;
    },
    removeSchool(index) {
      this.currentProfessor.schools.splice(index, 1);
    },
    async deleteProfessor(index) {
      const professor = this.professors[index];
      if (confirm(`Deseja realmente excluir ${professor.name}?`)) {
        try {
          await api.delete(`/teachers/${professor.id}`);
          await this.fetchProfessors();
        } catch (error) {
          console.error('Erro ao excluir professor:', error);
          alert('Erro ao excluir professor.');
        }
      }
    },
  },
  mounted() {
    this.fetchProfessors();
    this.fetchSchools();
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
/* Alinhar os botões de ação ao centro da célula */
.actions-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  align-items: center; /* <- ESSENCIAL para alinhamento vertical */
  height: 100%;
}
.professors-table td {
  vertical-align: middle;
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
