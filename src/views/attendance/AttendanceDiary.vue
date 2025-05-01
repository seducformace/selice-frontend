<template>
  <div class="attendance-diary">
    <Header />

    <div class="content-wrapper">
      <h1>Diário de Presença</h1>

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

      <div v-if="diary.length" class="diary-table">
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th v-for="slot in timeSlots" :key="slot">{{ slot }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="entry in diary" :key="entry.date">
              <td>{{ entry.date }}</td>
              <td v-for="slot in timeSlots" :key="slot">
                <input type="checkbox" v-model="entry.attendance[slot]" />
              </td>
            </tr>
          </tbody>
        </table>

        <button class="save-button" @click="saveDiary">Salvar Presenças</button>
      </div>

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
        '07:00',
        '08:00',
        '09:00',
        '10:00',
        '11:00',
        '13:00',
        '14:00',
        '15:00',
        '16:00',
        '18:00',
        '19:00',
        '20:00',
        '21:00',
      ],
      diary: [],
    };
  },
  methods: {
    loadDiary() {
      // Gerar mock de dias letivos do mês com presenças por hora
      const daysInMonth = new Date(
        this.selectedMonth.split('-')[0],
        this.selectedMonth.split('-')[1],
        0
      ).getDate();
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
    saveDiary() {
      console.log('Diário salvo:', this.diary);
      alert('Presenças salvas com sucesso! (Simulado)');
    },
  },
  mounted() {
    this.students = [
      { id: 1, name: 'André Silva' },
      { id: 2, name: 'Maria Souza' },
      { id: 3, name: 'João Oliveira' },
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
h1 {
  text-align: center;
  margin-bottom: 20px;
}
.filter-bar {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 20px;
}
.filter-bar label {
  display: flex;
  flex-direction: column;
  font-weight: bold;
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
}
.save-button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.no-data {
  text-align: center;
  color: #999;
  font-style: italic;
  margin-top: 30px;
}
</style>
