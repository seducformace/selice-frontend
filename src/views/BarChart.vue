<template>
  <div class="chart-container">
    <h3 v-if="title" class="chart-title">{{ title }}</h3>
    <Bar v-if="chartData" :data="chartConfig" :options="chartOptions" />
  </div>
</template>

<script>
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';
import { Bar } from 'vue-chartjs';

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);

export default {
  name: 'BarChart',
  components: {
    Bar,
  },
  props: {
    chartData: {
      type: Array,
      required: true,
    },
    chartLabels: {
      type: Array,
      required: true,
    },
    title: {
      type: String,
      default: '',
    },
  },
  computed: {
    chartConfig() {
      return {
        labels: this.chartLabels,
        datasets: [
          {
            label: this.title,
            data: this.chartData,
            backgroundColor: '#36A2EB',
          },
        ],
      };
    },
    chartOptions() {
      return {
        responsive: true,
        plugins: {
          legend: {
            display: false,
          },
          tooltip: {
            enabled: true,
          },
        },
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      };
    },
  },
};
</script>

<style scoped>
.chart-container {
  width: 100%;
  margin-top: 20px;
}
.chart-title {
  text-align: center;
  font-size: 18px;
  margin-bottom: 12px;
}
</style>
