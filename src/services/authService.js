// src/services/api.js

import axios from 'axios';

const API_URL = 'http://localhost:8080/api/authentication';

export const authService = {
  /**
   * Realiza login com email e senha.
   * Retorna um objeto contendo o token se o login for bem-sucedido.
   */
  async login(email, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, {
        email,
        password,
      });

      const { token } = response.data;

      if (!token) {
        throw new Error('Token não recebido do backend.');
      }

      // Salva o token localmente para sessões futuras
      localStorage.setItem('token', token);
      localStorage.setItem('userEmail', email);

      return { token };
    } catch (error) {
      console.error('[authService] Erro ao fazer login:', error);

      // Mensagem mais clara com fallback
      throw new Error(
        error?.response?.data?.message ||
          error.message ||
          'Erro inesperado ao fazer login.'
      );
    }
  },

  /**
   * Limpa o token do armazenamento local e efetua logout.
   */
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
  },

  /**
   * Retorna o token JWT salvo localmente.
   */
  getToken() {
    return localStorage.getItem('token');
  },

  /**
   * Verifica se o usuário está autenticado.
   */
  isAuthenticated() {
    return !!this.getToken();
  },
};
