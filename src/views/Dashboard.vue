<template>
  <div class="dashboard">
    <!-- Sidebar fixado na esquerda -->
    <Sidebar />
    <div class="main">
      <!-- Cabeçalho posicionado na parte superior -->
      <Header />
      <div class="dashboard-content">
        <!-- Título principal da página -->
        <h1>Bem-vindo, Administrador!</h1>
        <!-- Grid de cartões -->
        <section class="dashboard-cards">
          <div
            v-for="(card, index) in cards"
            :key="index"
            class="card-container"
          >
            <div class="card">
              <!-- Frente do cartão -->
              <div class="card-front">
                <i :class="card.icon" class="card-icon"></i>
                <h2>{{ card.title }}</h2>
              </div>
              <!-- Verso do cartão -->
              <div class="card-back">
                <p>Total: {{ card.count }}</p>
                <button @click="navigateTo(card.route)" class="details-button">
                  Ver Detalhes
                </button>
              </div>
            </div>
          </div>
        </section>
      </div>
      <!-- Rodapé fixado na parte inferior -->
      <Footer />
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

export default {
  name: 'Dashboard',
  components: {
    Sidebar,
    Header,
    Footer,
  },
  data() {
    return {
      cards: [
        {
          title: 'Faculdades',
          icon: 'fas fa-university',
          count: 36,
          route: '/faculties',
        },
        {
          title: 'Coordenadores',
          icon: 'fas fa-chalkboard-teacher',
          count: 84,
          route: '/coordinators',
        },
        {
          title: 'Alunos',
          icon: 'fas fa-user-graduate',
          count: 60,
          route: '/students',
        },
        {
          title: 'Professores',
          icon: 'fas fa-user-tie',
          count: 86,
          route: '/professors',
        },
        {
          title: 'Escolas',
          icon: 'fas fa-school',
          count: 55,
          route: '/schools',
        },
        {
          title: 'Relatórios',
          icon: 'fas fa-chart-bar',
          count: 64,
          route: '/reports',
        },
      ],
    };
  },
  methods: {
    navigateTo(route) {
      // Navega para a rota especificada
      this.$router.push(route);
    },
  },
};
</script>

<style scoped>
/* Estilo geral da página */
.dashboard {
  display: flex;
  background-color: #f5f5f5; /* Fundo cinza claro */
}

/* Área principal com conteúdo */
.main {
  flex-grow: 1;
  display: flex;
  flex-direction: column; /* Alinha elementos verticalmente */
  min-height: 100vh;
}

/* Conteúdo principal */
.dashboard-content {
  flex-grow: 1;
  max-width: 1200px;
  margin: 20px auto; /* Centraliza o conteúdo horizontalmente */
  text-align: center; /* Centraliza o texto */
  padding-top: 20px;
}

/* Grid de cartões */
.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* Três colunas fixas */
  gap: 30px; /* Espaçamento entre os cartões */
  justify-content: center; /* Centraliza os cartões horizontalmente */
  margin-top: 20px;
}

@media (max-width: 1024px) {
  .dashboard-cards {
    grid-template-columns: repeat(2, 1fr); /* Duas colunas em telas médias */
    gap: 20px; /* Reduz o espaçamento */
  }
}

@media (max-width: 768px) {
  .dashboard-cards {
    grid-template-columns: repeat(
      1,
      1fr
    ); /* Um cartão por linha em telas menores */
    gap: 15px; /* Reduz o espaçamento em telas pequenas */
  }
}

/* Contêiner que habilita o efeito 3D */
.card-container {
  perspective: 1000px; /* Ativa o efeito 3D */
}

/* Estilo de cada cartão */
.card {
  position: relative;
  width: 100%;
  max-width: 360px; /* Largura máxima ajustada */
  height: 180px; /* Altura reduzida */
  text-align: center;
  transition: transform 0.6s; /* Suaviza a rotação ao girar */
  transform-style: preserve-3d; /* Necessário para criar o efeito 3D */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px; /* Espaçamento interno */
  border-radius: 8px; /* Borda levemente arredondada */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); /* Sombra leve */
}

/* Efeito de rotação ao passar o mouse */
.card-container:hover .card {
  transform: rotateY(180deg); /* Gira o cartão ao passar o mouse */
}

/* Frente do cartão */
.card-front {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden; /* Esconde a face oposta */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); /* Sombra */
  background-color: #ffffff; /* Fundo branco */
}

/* Verso do cartão */
.card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden; /* Esconde a face oposta */
  transform: rotateY(180deg); /* Inicialmente escondido */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #00923f; /* Fundo verde */
  color: white;
}

/* Ícone dentro do cartão */
.card-icon {
  font-size: 48px; /* Tamanho do ícone */
  color: #00923f; /* Verde principal */
  margin-bottom: 10px;
}

/* Texto dentro do cartão */
.card h2 {
  font-size: 18px; /* Tamanho do texto */
  color: #333333; /* Texto cinza escuro */
  margin: 0;
}

/* Botão de detalhes */
.details-button {
  background-color: white;
  color: #00923f;
  border: none;
  padding: 8px 12px; /* Botão maior */
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.details-button:hover {
  background-color: #007a33; /* Verde escuro no hover */
  color: white;
}

/* Rodapé */
.footer {
  background-color: #00923f; /* Verde escuro */
  color: white;
  text-align: center;
  padding: 15px 0;
  font-size: 14px;
  box-shadow: 0px -2px 8px rgba(0, 0, 0, 0.1);
}
</style>
