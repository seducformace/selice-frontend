<template>
  <div class="attendance-diary">
    <Header />

    <div class="content-wrapper">
      <!-- Cabeçalho com botão home e título -->
      <div class="header-actions">
        <button class="home-button" @click="goToDashboard">
          <i class="fas fa-home"></i>
        </button>
        <h1>Espelho do Diário de Presença</h1>
      </div>

      <!-- Informações do Estudante -->
      <div class="student-summary" v-if="student">
        <h2>Minhas Informações</h2>
        <ul class="student-info-grid">
          <li><span>Nome:</span> {{ student.name }}</li>
          <li><span>Semestre:</span> {{ student.semester }}</li>
          <li><span>Faculdade:</span> {{ student.college }}</li>
          <li><span>Coordenador:</span> {{ student.coordinator }}</li>
          <li><span>Escola de Estágio:</span> {{ student.school }}</li>
          <li><span>Professor Orientador:</span> {{ student.teacher }}</li>
          <li><span>Carga Horária Total:</span> {{ totalHours }} horas</li>
          <li>
            <span>Horas Cumpridas:</span> {{ student.hoursCompleted }} horas
          </li>
          <li>
            <span>Horas Restantes:</span> {{ student.hoursRemaining }} horas
          </li>
        </ul>
      </div>

      <!-- Filtros -->
      <div class="filter-bar">
        <label>
          Mês de Referência:
          <input type="month" v-model="selectedMonth" />
        </label>

        <button @click="loadDiary">Carregar</button>
      </div>

      <!-- Tabela do Diário de Presença -->
      <div v-if="diary.length" class="diary-table">
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th v-for="slot in timeSlots" :key="slot">{{ slot }}</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="entry in diary"
              :key="entry.date"
              :class="{ highlighted: hasAttendance(entry) }"
            >
              <td>{{ entry.date }}</td>
              <td v-for="slot in timeSlots" :key="slot">
                <input
                  type="checkbox"
                  :checked="entry.attendance[slot]"
                  disabled
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Mensagem quando nenhum diário está carregado -->
      <div v-else class="no-data">Nenhum diário carregado.</div>
    </div>

    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

export default {
  name: 'DiaryView',
  components: { Header, Footer },
  data() {
    return {
      selectedMonth: '',
      timeSlots: [
        '07:00 às 07:50',
        '08:00 às 08:50',
        '09:00 às 09:50',
        '10:00 às 10:50',
        '11:00 às 11:50',
        '13:00 às 13:50',
        '14:00 às 14:50',
        '15:00 às 15:50',
        '16:00 às 16:50',
        '18:00 às 18:50',
        '19:00 às 19:50',
        '20:00 às 20:50',
        '21:00 às 21:50',
      ],
      diary: [],
      student: null, // será preenchido com dados da API
    };
  },
  computed: {
    totalHours() {
      return this.student
        ? this.student.hoursCompleted + this.student.hoursRemaining
        : 0;
    },
  },
  methods: {
    goToDashboard() {
      this.$router.push('/dashboard-student');
    },
    async fetchStudentData() {
      try {
        const response = await api.get('/students/me');
        this.student = {
          name: response.data.name,
          semester: response.data.semester || 'Não informado',
          college: response.data.college?.name || 'Não vinculado',
          coordinator: response.data.coordinator?.name || 'Não vinculado',
          school: response.data.school?.name || 'Não vinculado',
          teacher: response.data.teacher?.name || 'Não vinculado',
          hoursCompleted: response.data.hoursCompleted || 0,
          hoursRemaining: response.data.hoursRemaining || 0,
        };
      } catch (error) {
        console.error('Erro ao carregar dados do aluno autenticado:', error);
        alert('Erro ao carregar seus dados. Tente novamente.');
      }
    },
    loadDiary() {
      const [year, month] = this.selectedMonth.split('-');
      const daysInMonth = new Date(year, month, 0).getDate();
      this.diary = [];

      for (let day = 1; day <= daysInMonth; day++) {
        const date = `${this.selectedMonth}-${String(day).padStart(2, '0')}`;
        const attendance = {};
        this.timeSlots.forEach(
          (slot) => (attendance[slot] = Math.random() < 0.3)
        );
        this.diary.push({ date, attendance });
      }
    },
    hasAttendance(entry) {
      return Object.values(entry.attendance).some((present) => present);
    },
  },
  mounted() {
    this.fetchStudentData();
  },
};
</script>

<style scoped>
.attendance-diary {
  padding: 20px;
}
.content-wrapper {
  max-width: 1200px;
  margin: auto;
}
.header-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
  margin-bottom: 20px;
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
  transition: background-color 0.3s;
}
.home-button:hover {
  background-color: #007a33;
}
.student-summary {
  background-color: #f0f8ff;
  border-radius: 8px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.08);
  padding: 20px 30px;
  margin-bottom: 30px;
}
.student-summary h2 {
  margin-bottom: 16px;
  font-size: 17px;
  font-weight: bold;
  color: #0d47a1;
}
.student-info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px 20px;
  list-style: none;
  padding: 0;
  margin: 0;
}
.student-info-grid li {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}
.student-info-grid li span {
  font-weight: bold;
  color: #000;
}
.filter-bar {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 25px;
}
.filter-bar label {
  display: flex;
  flex-direction: column;
  font-weight: bold;
  font-size: 14px;
}
.filter-bar input[type='month'] {
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
  min-width: 220px;
}
.diary-table table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}
.diary-table th,
.diary-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: center;
  font-size: 13px;
}
.highlighted {
  background-color: #e6fbe6;
}
.no-data {
  text-align: center;
  color: #999;
  font-style: italic;
  margin-top: 30px;
}
</style>
