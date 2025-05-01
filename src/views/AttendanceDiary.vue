<template>
  <div class="attendance-diary">
    <Header />

    <div class="content-wrapper">
      <!-- Cabeçalho com botão home e título -->
      <div class="header-actions">
        <button class="home-button" @click="goToStudentsPage">
          <i class="fas fa-home"></i>
        </button>
        <h1>Diário de Presença</h1>
      </div>

      <!-- Informações do Estudante -->
      <div class="student-summary" v-if="selectedStudent">
        <h2>Informações do Estudante</h2>
        <ul class="student-info-grid">
          <li><span>Nome:</span> {{ selectedStudent.name }}</li>
          <li><span>Semestre:</span> {{ selectedStudent.semester || '—' }}</li>
          <li><span>Faculdade:</span> {{ selectedStudent.college || '—' }}</li>
          <li>
            <span>Coordenador:</span> {{ selectedStudent.coordinator || '—' }}
          </li>
          <li>
            <span>Escola de Estágio:</span> {{ selectedStudent.school || '—' }}
          </li>
          <li>
            <span>Professor Orientador:</span>
            {{ selectedStudent.teacher || '—' }}
          </li>
          <li><span>Carga Horária Total:</span> {{ totalHours }} horas</li>
          <li>
            <span>Horas Cumpridas:</span>
            {{ selectedStudent.hoursCompleted || 0 }} horas
          </li>
          <li>
            <span>Horas Restantes:</span>
            {{ selectedStudent.hoursRemaining || 0 }} horas
          </li>
        </ul>
      </div>

      <!-- Filtros -->
      <div class="filter-bar">
        <label>
          Selecionar Aluno:
          <select v-model="selectedStudent">
            <option disabled value="">Selecione</option>
            <option
              v-for="student in students"
              :key="student.id"
              :value="student"
            >
              {{ student.name }}
            </option>
          </select>
        </label>

        <label>
          Mês de Referência:
          <input type="month" v-model="selectedMonth" />
        </label>

        <button @click="loadDiary">Carregar Diário</button>
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
                <input type="checkbox" v-model="entry.attendance[slot]" />
              </td>
            </tr>
          </tbody>
        </table>

        <button class="save-button" @click="saveDiary">Salvar Presenças</button>
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

export default {
  name: 'AttendanceDiary',
  components: { Header, Footer },
  data() {
    return {
      students: [],
      selectedStudent: '',
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
    };
  },
  computed: {
    totalHours() {
      if (!this.selectedStudent) return 0;
      return (
        (this.selectedStudent.hoursCompleted || 0) +
        (this.selectedStudent.hoursRemaining || 0)
      );
    },
  },
  methods: {
    loadDiary() {
      const [year, month] = this.selectedMonth.split('-');
      const daysInMonth = new Date(year, month, 0).getDate();
      this.diary = [];

      for (let day = 1; day <= daysInMonth; day++) {
        const date = `${this.selectedMonth}-${String(day).padStart(2, '0')}`;
        const attendance = {};
        this.timeSlots.forEach((slot) => {
          attendance[slot] = false;
        });
        this.diary.push({ date, attendance });
      }
    },
    hasAttendance(entry) {
      return Object.values(entry.attendance).some((p) => p === true);
    },
    saveDiary() {
      console.log('Diário salvo:', this.diary);
      alert('Presenças salvas com sucesso! (Simulado)');
    },

    // ✅ Redireciona corretamente para a página de estudantes do professor
    goToStudentsPage() {
      this.$router.push('/students-by-teacher-test');
    },
  },
  mounted() {
    // Mock de alunos (será substituído por API futuramente)
    this.students = [
      {
        id: 1,
        name: 'André Silva',
        semester: '2º',
        college: 'Faculdade Estadual',
        coordinator: 'Prof. Carla Mendes',
        school: 'Escola Modelo A',
        teacher: 'Prof. João Lima',
        hoursCompleted: 50,
        hoursRemaining: 30,
      },
      {
        id: 2,
        name: 'Maria Souza',
        semester: '1º',
        college: 'Faculdade Federal',
        coordinator: 'Prof. Marcos Leal',
        school: 'Escola Municipal B',
        teacher: 'Profª. Ana Bezerra',
        hoursCompleted: 20,
        hoursRemaining: 60,
      },
    ];
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

/* Título */
h1 {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 20px;
}

/* Cabeçalho com botão home */
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

/* Barra de filtros */
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

.filter-bar select,
.filter-bar input[type='month'] {
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
  min-width: 220px;
}

/* Bloco de resumo do estudante */
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

/* Grade de informações do estudante em 3 colunas */
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

/* Tabela de presença */
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

/* Linha com presença marcada */
.highlighted {
  background-color: #e6fbe6; /* Verde suave */
}

/* Botão de salvar */
.save-button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.save-button:hover {
  background-color: #218838;
}

/* Nenhum diário */
.no-data {
  text-align: center;
  color: #999;
  font-style: italic;
  margin-top: 30px;
}
</style>
