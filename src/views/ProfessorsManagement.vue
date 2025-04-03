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

      <!-- Formulário para adicionar/editar professor -->
      <div v-if="isModalOpen" class="form-container">
        <div class="form-box">
          <h2>{{ isEditing ? 'Editar Professor' : 'Adicionar Professor' }}</h2>
          <form @submit.prevent="saveProfessor" class="professor-form">
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

            <div class="form-group">
              <label for="subjects">Disciplinas</label>
              <input
                id="subjects"
                v-model="currentProfessor.subjects"
                type="text"
                placeholder="Ex: Matemática, Física"
                required
              />
            </div>

            <div class="form-group">
              <label for="schools">Escolas</label>
              <input
                id="schools"
                v-model="currentProfessor.schools"
                type="text"
                placeholder="Ex: Escola A, Escola B"
                required
              />
            </div>

            <div class="form-group">
              <label for="orientedStudents">Orientados</label>
              <input
                id="orientedStudents"
                v-model="currentProfessor.orientedStudents"
                type="number"
                min="0"
                required
              />
            </div>

            <div class="form-group">
              <label for="studentsInProgress">Em Andamento</label>
              <input
                id="studentsInProgress"
                v-model="currentProfessor.studentsInProgress"
                type="number"
                min="0"
                required
              />
            </div>

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

    <!-- Rodapé -->
    <Footer class="footer-fixed" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

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
      professors: [
        {
          name: 'Carlos Souza',
          registration: 'PROF001',
          certification: 'Doutorado',
          subjects: ['Matemática', 'Física'],
          schools: ['Escola A', 'Escola B'],
          orientedStudents: 8,
          studentsInProgress: 2,
        },
        {
          name: 'Mariana Lima',
          registration: 'PROF002',
          certification: 'Mestrado',
          subjects: ['História'],
          schools: ['Escola C'],
          orientedStudents: 10,
          studentsInProgress: 0,
        },
      ],
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
  },
};
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 80px; /* Ajusta a altura para evitar sobreposição com o rodapé */
}

.footer-fixed {
  width: 100%;
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  left: 0;
}

.form-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 80%;
  max-width: 600px;
}

.professor-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
}

input,
select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-buttons {
  display: flex;
  justify-content: space-between;
  grid-column: span 2;
  margin-top: 15px;
}

/* Ajuste na tabela para garantir que as linhas fiquem visíveis */
.table-container {
  overflow-x: auto;
}

.professors-table {
  width: 100%;
  border-collapse: collapse;
}

.professors-table th,
.professors-table td {
  border: 1px solid #ccc; /* Adiciona borda visível em todas as células */
  padding: 10px;
  text-align: left;
}

.professors-table th {
  background-color: #00923f;
  color: white;
  font-weight: bold;
}

/* Alternância de cor para facilitar a leitura */
.professors-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.professors-table tbody tr:hover {
  background-color: #e0e0e0;
}

/* Estilo das ações (Botão adicionar e campo de busca) */
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

.actions-buttons {
  display: flex;
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

/* Ajuste no botão de voltar para home */
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

.home-button:hover {
  background-color: #007a33;
  opacity: 0.9;
}

/* Layout da página com rodapé fixo */
.content-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* Garante que o conteúdo ocupe toda a altura da tela */
}

/* Faz com que o conteúdo principal ocupe o espaço restante e empurre o rodapé para baixo */
.content {
  flex: 1;
  max-width: 1200px;
  margin: 20px auto;
}

/* Rodapé fixo na parte inferior da página */
.footer-fixed {
  width: 100%;
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px;
  position: absolute; /* Corrigindo de fixed para absolute */
  bottom: 0;
  left: 0;
}

/* Se o conteúdo da página for menor que a tela, força o rodapé a permanecer no final */
@media (min-height: 600px) {
  .footer-fixed {
    position: fixed;
  }
}
</style>
