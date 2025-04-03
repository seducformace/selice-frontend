<template>
  <div>
    <canvas ref="pieCanvas"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
  name: 'PieChart',
  props: {
    chartData: {
      type: Object,
      required: true,
    },
    options: {
      type: Object,
      default: () => ({}),
    },
  },
  mounted() {
    new Chart(this.$refs.pieCanvas, {
      type: 'pie', // ou 'doughnut' se quiser estilo donut
      data: this.chartData,
      options: this.options,
    });
  },
};
</script>

<style scoped>
/* Ajuste de estilo, se quiser */
</style>
