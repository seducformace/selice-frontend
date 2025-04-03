<template>
  <footer class="footer">
    <div class="footer-content">
      <p>© 2025 Governo do Estado do Ceará. Todos os direitos reservados.</p>
      <p>
        Secretaria da Educação - Sistema de Gerenciamento de Estágio Curricular
      </p>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
};
</script>

<style scoped>
.footer {
  background-color: #00923f; /* Verde Escuro */
  color: white;
  text-align: center;
  padding: 15px 0;
  font-size: 14px;
  box-shadow: 0px -2px 8px rgba(0, 0, 0, 0.1);
}

.footer-content p {
  margin: 5px 0;
}
</style>
