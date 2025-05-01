<template>
  <div class="students-by-teacher">
    <Header />

    <div class="content-wrapper">
      <!-- Cabeçalho com ícone e busca -->
      <div class="header-bar">
        <div class="left">
          <button class="home-button" @click="goToProfessorDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Meus Estudantes</h1>
        </div>
        <div class="right search-bar">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar por nome, curso ou escola..."
          />
          <button @click="applySearch">Buscar</button>
        </div>
      </div>

      <!-- Info do Professor -->
      <p class="user-info" v-if="currentTeacher">
        Professor: {{ currentTeacher.name }} – CPF: {{ currentTeacher.cpf }}
      </p>

      <!-- Indicadores -->
      <div class="stats-cards">
        <div class="card">
          <h3>Total de Alunos</h3>
          <p>{{ filteredStudents.length }}</p>
        </div>
        <div class="card">
          <h3>Total de Horas Pendentes</h3>
          <p>{{ totalPendingHours }}h</p>
        </div>
      </div>

      <!-- Tabela de alunos -->
      <div v-if="filteredStudents.length" class="student-table">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>CPF</th>
              <th>Curso</th>
              <th>Faculdade</th>
              <th>Escola</th>
              <th>Horas Concluídas</th>
              <th>Horas Pendentes</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="student in filteredStudents" :key="student.id">
              <td>{{ student.name }}</td>
              <td>{{ student.cpf }}</td>
              <td>{{ student.course }}</td>
              <td>{{ student.college }}</td>
              <!-- NOVO CAMPO -->
              <td>{{ student.school }}</td>
              <td>{{ student.hoursCompleted }}h</td>
              <td>{{ student.hoursPending }}h</td>
              <td>
                <button class="presence-button" @click="goToDiary(student)">
                  Lançar Presença
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-else class="no-results">Nenhum estudante encontrado.</div>
    </div>

    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

export default {
  name: 'StudentsByTeacher',
  components: { Header, Footer },
  data() {
    return {
      searchQuery: '',
      students: [],
      currentTeacher: {
        name: 'João da Silva',
        cpf: '123.456.789-00',
      },
    };
  },
  computed: {
    filteredStudents() {
      const query = this.searchQuery.toLowerCase();
      return this.students.filter(
        (s) =>
          s.name.toLowerCase().includes(query) ||
          s.cpf.includes(query) ||
          s.course.toLowerCase().includes(query) ||
          s.school.toLowerCase().includes(query)
      );
    },
    totalPendingHours() {
      return this.filteredStudents.reduce((acc, s) => acc + s.hoursPending, 0);
    },
  },
  methods: {
    goToProfessorDashboard() {
      this.$router.push('/dashboard-professor');
    },
    applySearch() {
      // A busca já é reativa
    },
    goToDiary(student) {
      this.$router.push({
        name: 'AttendanceDiary', // nome correto da rota no router
        query: { studentId: student.id },
      });
    },
  },
  mounted() {
    this.students = [
      {
        id: 1,
        name: 'Lucas Andrade',
        cpf: '123.456.789-00',
        course: 'Pedagogia',
        college: 'Faculdade Estadual',
        school: 'Escola Estadual A',
        hoursCompleted: 40,
        hoursPending: 20,
      },
      {
        id: 2,
        name: 'Juliana Mota',
        cpf: '987.654.321-00',
        course: 'História',
        college: 'Faculdade Federal',
        school: 'Escola Modelo B',
        hoursCompleted: 30,
        hoursPending: 30,
      },
    ];
  },
};
</script>

<style scoped>
.students-by-teacher {
  padding: 20px;
}
.content-wrapper {
  max-width: 1200px;
  margin: auto;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
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
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
}
.search-bar input {
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  width: 300px;
}
.search-bar button {
  background-color: #007bff;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.search-bar button:hover {
  background-color: #0056b3;
}
.stats-cards {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-bottom: 20px;
}
.card {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.05);
}
.card h3 {
  margin-bottom: 8px;
  font-size: 16px;
}
.student-table table {
  width: 100%;
  border-collapse: collapse;
}
.student-table th,
.student-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
}
.presence-button {
  background-color: #ff9800;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}
.presence-button:hover {
  background-color: #e68900;
}
.no-results {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 30px;
}
.user-info {
  text-align: center;
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 10px;
}
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.header-bar .left {
  display: flex;
  align-items: center;
  gap: 10px;
}
.header-bar h1 {
  margin: 0;
  font-size: 22px;
}

.header-bar .search-bar {
  display: flex;
  gap: 10px;
}
</style>
