<template>
  <div class="reports-page">
    <div class="content-wrapper">
      <div class="content">
        <!-- Cabeçalho -->
        <div class="header-actions">
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Relatórios Gerais das Faculdades e Escolas</h1>

          <div class="search-bar">
            <i class="fas fa-search search-icon"></i>
            <input type="text" v-model="searchQuery" placeholder="Buscar..." />
            <button class="search-button" @click="applySearch">Buscar</button>
          </div>
        </div>

        <!-- Cards principais -->
        <div class="cards-container">
          <div class="stat-card" v-for="(value, key) in dashboard" :key="key">
            <h2>{{ formatKey(key) }}</h2>
            <p class="stat-value">{{ value }}</p>
          </div>
        </div>

        <!-- Bloco de Gráficos -->
        <div class="charts-container">
          <div class="chart-box" v-if="statusChartData">
            <h2>Status dos Estagiários</h2>
            <BarChart
              :chart-data="statusChartData"
              :chart-options="statusChartOptions"
            />
          </div>
          <div class="chart-box" v-if="courseChartData">
            <h2>Estagiários por Curso</h2>
            <PieChart
              :chart-data="courseChartData"
              :chart-options="courseChartOptions"
            />
          </div>
        </div>

        <!-- Mapa -->
        <div class="map-box">
          <h2>🗺️ Mapa Interativo do Ceará</h2>
          <div id="map" class="map-container"></div>
        </div>

        <!-- Destaques -->
        <div class="highlight-box">
          <h2>🏆 Escolas com Melhor IDEB</h2>
          <ul class="info-list">
            <li v-for="school in topIdebSchools" :key="school.name">
              {{ school.name }} - IDEB {{ school.ideb }}
            </li>
          </ul>
        </div>

        <div class="highlight-box">
          <h2>📍 Estagiários</h2>
          <ul class="info-list">
            <li v-for="school in schoolInternships" :key="school.schoolName">
              {{ school.schoolName }} - {{ school.internCount }} estagiário(s)
            </li>
          </ul>
        </div>
      </div>
    </div>

    <Footer class="footer-fixed" />
  </div>
</template>
<script>
import { api } from '@/services/api';
import BarChart from '@/components/charts/BarChart.vue';
import PieChart from '@/components/charts/PieChart.vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// ✅ Corrige os ícones dos marcadores do Leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

export default {
  name: 'ReportsPage',
  components: {
    BarChart,
    PieChart,
  },
  data() {
    return {
      dashboard: {
        totalFaculties: 0,
        totalCoordinators: 0,
        totalTeachers: 0,
        totalStudents: 0,
        totalSchools: 0,
      },
      statusChartData: null,
      courseChartData: null,
      statusChartOptions: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
          title: { display: true, text: 'Estagiários por Status' },
        },
      },
      courseChartOptions: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom' },
          title: { display: true, text: 'Estagiários por Curso' },
        },
      },
    };
  },
  methods: {
    goToDashboard() {
      this.$router.push('/dashboard');
    },
    formatKey(key) {
      return key
        .replace(/([A-Z])/g, ' $1')
        .replace(/^./, (str) => str.toUpperCase());
    },
    async fetchData() {
      try {
        const response = await api.get('/reports');
        this.dashboard = response.data;

        // 🟦 Exemplo: substituir por dados reais futuramente
        this.statusChartData = {
          labels: ['Em andamento', 'Concluído', 'Reprovado'],
          datasets: [
            {
              label: 'Estagiários',
              data: [12, 7, 3],
              backgroundColor: ['#36A2EB', '#4BC0C0', '#FF6384'],
            },
          ],
        };

        this.courseChartData = {
          labels: ['Pedagogia', 'História', 'Matemática'],
          datasets: [
            {
              data: [18, 6, 5],
              backgroundColor: ['#f87979', '#7cb5ec', '#90ed7d'],
            },
          ],
        };
      } catch (error) {
        console.error('Erro ao carregar relatórios:', error);
      }
    },
    initMap() {
      const map = L.map('map').setView([-5.2, -39.3], 7);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Mapa © OpenStreetMap',
      }).addTo(map);

      // ✅ Marcadores estáticos por enquanto
      L.marker([-3.71722, -38.5433])
        .addTo(map)
        .bindPopup('Faculdade Fortaleza');
      L.marker([-4.9707, -39.0169]).addTo(map).bindPopup('Escola Quixadá');
    },
  },
  mounted() {
    this.fetchData();
    this.$nextTick(() => this.initMap());
  },
};
</script>
<style scoped>
/* === ESTRUTURA GERAL === */
.content-wrapper {
  min-height: 100vh;
}

.content {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

/* === CABEÇALHO === */
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
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

  /* 👇 Novos estilos adicionados */
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.home-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* === BARRA DE BUSCA === */
.search-bar {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #fff;
  overflow: hidden;
  height: 42px;
  max-width: 380px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.05);
}

.search-bar input {
  flex: 1;
  border: none;
  padding: 10px 14px;
  font-size: 14px;
  outline: none;
  background-color: transparent;
}

.search-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0 18px;
  height: 100%;
  font-weight: 500;
  font-size: 14px;
  border-left: 1px solid #ccc;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #0056b3;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

/* === CARDS DE ESTATÍSTICA === */
.cards-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  flex: 1 1 200px;
  background: #f8f8f8;
  border-radius: 8px;
  text-align: center;
  padding: 16px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.06);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}

/* === GRÁFICOS === */
.charts-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
}

.chart-box {
  flex: 1 1 45%;
  background: #ffffff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
}

.chart-box h2 {
  text-align: center;
  font-size: 18px;
  margin-bottom: 12px;
}

/* === MAPA INTERATIVO === */
.map-box {
  flex: 1 1 100%;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 12px;
  border: 1px dashed #ccc;
  margin-top: 20px;
}

#map {
  height: 400px;
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.08);
  z-index: 1;
  margin-top: 12px;
}

/* === DESTAQUES === */
.bottom-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 30px;
}

.highlight-box {
  flex: 1 1 30%;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 12px;
  border: 1px dashed #ccc;
  text-align: center;
}

.highlight-box .info {
  font-style: italic;
  margin-top: 8px;
}

/* === LISTA DE INFORMAÇÕES === */
.info-list {
  padding-left: 20px;
  list-style: none;
  font-size: 15px;
  line-height: 1.6;
  text-align: left;
}

.info-list li::before {
  content: '• ';
  color: #00923f;
  font-weight: bold;
  margin-right: 4px;
}

/* === FOOTER === */
.footer-fixed {
  margin-top: auto;
}
</style>
