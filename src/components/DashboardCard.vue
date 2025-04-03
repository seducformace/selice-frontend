<template>
  <div class="dashboard-card" @click="$router.push(route)">
    <img :src="icon" alt="Icon" class="card-icon" />
    <h3>{{ title }}</h3>
    <p>{{ count }}</p>
  </div>
</template>
<script>
export default {
  name: 'DashboardCard',
  props: {
    title: { type: String, required: true },
    icon: { type: String, required: true },
    count: { type: [String, Number], required: true },
    route: { type: String, required: true },
  },
};
</script>
<style scoped>
.dashboard-card {
  background-color: #fff; /* Fundo branco */
  border-radius: 8px; /* Cantos arredondados */
  padding: 20px; /* Espaçamento interno */
  text-align: center; /* Centraliza textos e imagens */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Sombra sutil */
  transition: transform 0.2s ease, box-shadow 0.2s ease; /* Animação suave */
  cursor: pointer; /* Mostra que é clicável */
} /* Efeito de 'zoom' ao passar o mouse */
.dashboard-card:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
} /* Ajuste de imagem no card */
.card-icon {
  width: 50px; /* Largura da imagem */
  height: 50px; /* Altura da imagem */
  object-fit: contain; /* Evita distorções */
  margin-bottom: 10px; /* Espaçamento abaixo da imagem */
} /* Título do card */
.dashboard-card h3 {
  margin: 0;
  font-size: 18px;
  color: #333; /* Cor do texto */
} /* Texto principal do card (count) */
.dashboard-card p {
  margin: 5px 0 0;
  font-size: 16px;
  color: #555;
}
</style>
