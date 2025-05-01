<template>
  <div class="student-report">
    <Header />

    <div class="content-wrapper">
      <div class="header-bar">
        <div class="left">
          <button class="home-button" @click="goToStudentDashboard">
            <i class="fas fa-home"></i>
          </button>
        </div>
        <h1 class="title">Relatório do Estudante</h1>
      </div>

      <div class="report-a4" ref="reportContent">
        <img src="@/assets/seduc.png" alt="Brasão da SEDUC" class="seal" />
        <h2>Secretaria da Educação do Estado do Ceará</h2>
        <h3>Relatório de Atividades de Estágio</h3>

        <div class="student-section">
          <p><strong>Nome:</strong> {{ student.name }}</p>
          <p><strong>CPF:</strong> {{ student.cpf }}</p>
          <p><strong>Curso:</strong> {{ student.course }}</p>
          <p><strong>Faculdade:</strong> {{ student.college }}</p>
          <p><strong>Escola de Estágio:</strong> {{ student.school }}</p>
          <p><strong>Período:</strong> {{ internshipPeriod }}</p>
        </div>

        <div class="status-section">
          <p>
            <strong>Horas Concluídas:</strong> {{ student.hoursCompleted }}h
          </p>
          <p><strong>Horas Restantes:</strong> {{ student.hoursPending }}h</p>
        </div>

        <div class="summary-section">
          <p>{{ internshipMessage }}</p>
        </div>

        <div class="signature-section">
          <div>
            <p>______________________________</p>
            <p>Professor Orientador</p>
          </div>
          <div>
            <p>______________________________</p>
            <p>Coordenador da Escola</p>
          </div>
        </div>

        <div class="date-section">
          <p>Emitido em {{ currentDate }}</p>
        </div>
      </div>

      <div class="actions">
        <button @click="printReport">🖨️ Imprimir</button>
        <button @click="downloadPDF">📥 Download PDF</button>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import html2pdf from 'html2pdf.js';

export default {
  name: 'StudentReportView',
  components: { Header, Footer },
  data() {
    return {
      student: {
        name: 'Maria Estagiária',
        cpf: '123.456.789-00',
        course: 'Letras',
        college: 'Faculdade Estadual',
        school: 'Escola Modelo B',
        hoursCompleted: 60,
        hoursPending: 0,
      },
    };
  },
  computed: {
    internshipMessage() {
      const total = this.student.hoursCompleted + this.student.hoursPending;
      if (this.student.hoursPending === 0) {
        return `O(A) estudante concluiu com êxito as ${total} horas obrigatórias de estágio, estando apto(a) a seguir com os trâmites de validação institucional.`;
      } else {
        return `O(A) estudante está em processo de cumprimento das ${total} horas obrigatórias de estágio, com progresso registrado neste relatório.`;
      }
    },
    internshipPeriod() {
      return 'Março de 2024 a Novembro de 2024';
    },
    currentDate() {
      return new Date().toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
      });
    },
  },
  methods: {
    goToStudentDashboard() {
      this.$router.push('/dashboard-student');
    },
    printReport() {
      window.print();
    },
    downloadPDF() {
      const content = this.$refs.reportContent;
      const options = {
        margin: 0,
        filename: 'relatorio-estudante.pdf',
        image: { type: 'jpeg', quality: 1 },
        html2canvas: { scale: 2, useCORS: true },
        jsPDF: { unit: 'cm', format: 'a4', orientation: 'portrait' },
      };
      html2pdf().set(options).from(content).save();
    },
  },
};
</script>

<style scoped>
.student-report {
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-wrapper {
  max-width: 21cm;
  margin: auto;
  flex: 1;
}

.header-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  margin-bottom: 10px;
}

.header-bar .left {
  position: absolute;
  left: 0;
}

.title {
  font-size: 20px;
  font-weight: bold;
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
}
.home-button:hover {
  background-color: #007a33;
}

.report-a4 {
  width: 21cm;
  height: 29.7cm;
  padding: 2cm;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  line-height: 1.6;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  overflow: hidden;
  position: relative;
  text-align: justify;
}

.seal {
  width: 80px;
  display: block;
  margin: 0 auto 10px auto;
}

.report-a4 h2,
.report-a4 h3 {
  text-align: center;
  margin: 0;
  padding: 0;
}

h2 {
  font-size: 16px;
  margin-bottom: 4px;
}
h3 {
  font-size: 14px;
  margin-bottom: 20px;
}

.student-section,
.status-section,
.summary-section {
  margin-bottom: 12px;
}

.signature-section {
  display: flex;
  justify-content: space-between;
  margin-top: 40px;
  font-size: 13px;
  padding: 0 30px;
}

.signature-section div {
  text-align: center;
  width: 45%;
}

.signature-section p:first-child {
  margin-bottom: 5px;
}

.date-section {
  text-align: right;
  font-size: 13px;
  margin-top: 20px;
  color: #333;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 25px;
}

.actions button {
  padding: 10px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.actions button:hover {
  background-color: #0056b3;
}
</style>
