import axios from 'axios';

const API_URL = 'http://localhost:8081/api/professores'; // Ajuste conforme necessário

export default {
  async listarProfessores() {
    return axios.get(`${API_URL}/listar`);
  },
  async cadastrarProfessor(professor) {
    return axios.post(`${API_URL}/cadastrar`, professor);
  },
  async obterProfessor(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  async atualizarProfessor(id, professor) {
    return axios.put(`${API_URL}/editar/${id}`, professor);
  },
  async deletarProfessor(id) {
    return axios.delete(`${API_URL}/excluir/${id}`);
  },
  async obterEstatisticasProfessor(id) {
    return axios.get(`${API_URL}/${id}/estatisticas`);
  },
};
