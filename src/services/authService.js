// src/services/api.js

import axios from 'axios';
import jwt_decode from 'jwt-decode';

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

      // Decodifica o token para extrair a role
      let role = null;
      try {
        const decoded = jwt_decode(token);
        role =
          decoded.role ||
          (decoded.authorities && decoded.authorities[0]?.authority);
      } catch (decodeErr) {
        console.warn(
          '[authService] Não foi possível decodificar o token:',
          decodeErr
        );
      }

      // Salva o token, email e role localmente
      localStorage.setItem('token', token);
      localStorage.setItem('userEmail', email);
      if (role) localStorage.setItem('role', role.toUpperCase());

      return { token };
    } catch (error) {
      console.error('[authService] Erro ao fazer login:', error);

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
    localStorage.removeItem('role');
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
