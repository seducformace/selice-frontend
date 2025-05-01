<template>
  <div class="chart-container">
    <h3 v-if="title" class="chart-title">{{ title }}</h3>
    <Pie v-if="chartData" :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Pie } from 'vue-chartjs';

ChartJS.register(Title, Tooltip, Legend, ArcElement);

export default {
  name: 'PieChart',
  components: {
    Pie,
  },
  props: {
    chartData: {
      type: Object,
      required: true,
    },
    title: {
      type: String,
      default: '',
    },
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom',
          },
          tooltip: {
            enabled: true,
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
