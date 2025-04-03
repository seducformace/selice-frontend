<template>
  <div class="modal-overlay">
    <div class="modal">
      <h2>Detalhes do Professor</h2>

      <p><strong>Nome:</strong> {{ professor.nome }}</p>
      <p><strong>Matrícula:</strong> {{ professor.matricula }}</p>
      <p><strong>Certificação:</strong> {{ professor.certificacao }}</p>
      <p><strong>Disciplinas:</strong> {{ professor.disciplinas }}</p>
      <p><strong>Escolas:</strong> {{ professor.escolas }}</p>
      <p>
        <strong>Alunos Orientados:</strong> {{ professor.alunosOrientados }}
      </p>
      <p>
        <strong>Alunos em Andamento:</strong> {{ professor.alunosEmAndamento }}
      </p>
      <p><strong>Início da orientação:</strong> {{ professor.dataInicio }}</p>
      <p><strong>Fim da orientação:</strong> {{ professor.dataFim }}</p>

      <button @click="$emit('fechar')">Fechar</button>
    </div>
  </div>
</template>

<script>
import professorService from '@/services/professorService';

export default {
  props: ['professorId'],
  data() {
    return {
      professor: {},
    };
  },
  async created() {
    try {
      const response = await professorService.obterProfessor(this.professorId);
      this.professor = response.data;
    } catch (error) {
      console.error('Erro ao carregar detalhes:', error);
    }
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
}
</style>
