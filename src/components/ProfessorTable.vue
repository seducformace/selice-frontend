<template>
  <div class="container">
    <h2>Gestão de Professores</h2>
    <button @click="$emit('adicionarProfessor')">Adicionar Professor</button>

    <table>
      <thead>
        <tr>
          <th>Nome</th>
          <th>Matrícula</th>
          <th>Certificação</th>
          <th>Disciplinas</th>
          <th>Escolas</th>
          <th>Orientados</th>
          <th>Em Andamento</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="professor in professores" :key="professor.id">
          <td>{{ professor.nome }}</td>
          <td>{{ professor.matricula }}</td>
          <td>{{ professor.certificacao }}</td>
          <td>{{ professor.disciplinas.join(', ') }}</td>
          <td>{{ professor.escolas.join(', ') }}</td>
          <td>{{ professor.alunosOrientados }}</td>
          <td>{{ professor.alunosEmAndamento }}</td>
          <td>
            <button @click="$emit('editarProfessor', professor)">Editar</button>
            <button @click="$emit('verDetalhes', professor.id)">
              Detalhes
            </button>
            <button @click="$emit('excluirProfessor', professor.id)">
              Excluir
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: ['professores'],
};
</script>

<style scoped>
/* Estilos básicos para a tabela */
.container {
  padding: 20px;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
button {
  margin: 5px;
}
</style>
