<template>
  <div class="reports-page">
    <!-- Cabeçalho (Header) -->
    <Header />

    <div class="content-wrapper">
      <div class="content">
        <!-- Barra Superior: Ícone Home + Título da Página -->
        <div class="header-actions">
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Página de Faculties (Relatórios)</h1>
        </div>

        <!-- Seção de Indicadores (cards) -->
        <div class="cards-container">
          <div class="stat-card" v-for="card in statCards" :key="card.title">
            <h2>{{ card.title }}</h2>
            <p class="stat-value">{{ card.value }}</p>
          </div>
        </div>

        <!-- Seção de Gráficos -->
        <div class="charts-container">
          <!-- Exemplo de gráfico de barras com vue-chartjs -->
          <div class="chart-box">
            <h2>Faculdades por Tipo</h2>
            <BarChart :chartData="chartDataByType" :options="chartOptions" />
          </div>

          <!-- Exemplo de gráfico de pizza -->
          <div class="chart-box">
            <h2>Faculdades por Status (Ativo/Inativo)</h2>
            <PieChart :chartData="chartDataByStatus" :options="chartOptions" />
          </div>
        </div>

        <!-- Seção do Mapa (exemplo) -->
        <div class="map-section">
          <h2>Mapa do Ceará</h2>
          <!-- Aqui você pode inserir um componente de mapa, por exemplo: -->
          <!-- <CearaMap /> -->
          <!-- Ou um iframe do Google Maps, ou qualquer solução de mapa. -->
          <div class="map-placeholder">
            <!-- Apenas um placeholder, substitua com a implementação real do mapa -->
            <p>Futuro mapa interativo do Ceará virá aqui!</p>
          </div>
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
import BarChart from '@/components/charts/BarChart.vue';
import PieChart from '@/components/charts/PieChart.vue';
// Se tiver um componente de Mapa, descomente:
// import CearaMap from '@/components/CearaMap.vue';

export default {
  name: 'ReportsPage',
  components: {
    Header,
    Footer,
    BarChart,
    PieChart,
    // CearaMap,
  },
  data() {
    return {
      // Exemplo de dados para os cards (indicadores)
      statCards: [
        { title: 'Faculdades Ativas', value: 0 },
        { title: 'Faculdades Inativas', value: 0 },
      ],

      // Exemplo de chartData
      chartDataByType: null,
      chartDataByStatus: null,

      // Opções do chart (Chart.js, por exemplo)
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
  mounted() {
    // Chamar funções para buscar dados do backend
    this.fetchReportsData();
  },
  methods: {
    goToDashboard() {
      // Ajuste o caminho para a rota do seu dashboard
      this.$router.push('/dashboard');
    },
    async fetchReportsData() {
      try {
        // Exemplo de requisição ao backend (ajuste a rota)
        const response = await this.$axios.get('/api/faculties/reports-data');
        // Supondo que response.data seja algo como:
        // {
        //   activeCount: 25,
        //   inactiveCount: 5,
        //   byType: { "Pública": 15, "Privada": 10 },
        //   byStatus: { "Ativo": 25, "Inativo": 5 }
        // }

        // Preenche indicadores
        this.statCards[0].value = response.data.activeCount;
        this.statCards[1].value = response.data.inactiveCount;

        // Monta chart data para "Faculdades por Tipo"
        this.chartDataByType = {
          labels: Object.keys(response.data.byType),
          datasets: [
            {
              label: 'Quantidade',
              backgroundColor: ['#00923f', '#ccc'],
              data: Object.values(response.data.byType),
            },
          ],
        };

        // Monta chart data para "Faculdades por Status"
        this.chartDataByStatus = {
          labels: Object.keys(response.data.byStatus),
          datasets: [
            {
              backgroundColor: ['green', 'red'],
              data: Object.values(response.data.byStatus),
            },
          ],
        };
      } catch (err) {
        console.error('Erro ao buscar dados de relatórios:', err);
      }
    },
  },
};
</script>

<style scoped>
/* Layout geral */
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

/* Barra superior: Home + Título */
.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}
.home-button {
  background-color: #00923f;
  color: white;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* Cards de estatísticas */
.cards-container {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}
.stat-card {
  background-color: #f8f8f8;
  padding: 16px;
  border-radius: 8px;
  flex: 1;
  text-align: center;
}
.stat-card h2 {
  margin-bottom: 10px;
  font-size: 18px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
}

/* Gráficos */
.charts-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.chart-box {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  flex: 1 1 400px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  height: 300px;
  display: flex;
  flex-direction: column;
}
.chart-box h2 {
  margin-bottom: 10px;
  font-size: 16px;
  text-align: center;
  font-weight: bold;
}
.chart-box canvas {
  flex: 1;
}

/* Seção do mapa (placeholder) */
.map-section {
  margin-top: 30px;
}
.map-placeholder {
  background-color: #f9f9f9;
  border: 1px dashed #ccc;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  font-style: italic;
}

/* Rodapé fixo */
.footer-fixed {
  margin-top: auto;
}
</style>
