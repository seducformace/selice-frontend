<template>
  <div>
    <canvas ref="barCanvas"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
  name: 'BarChart',
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
    // Quando o componente montar, cria o Chart
    new Chart(this.$refs.barCanvas, {
      type: 'bar',
      data: this.chartData,
      options: this.options,
    });
  },
};
</script>

<style scoped>
/* Ajuste de estilo, se quiser */
</style>
