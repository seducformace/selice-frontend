import axios from 'axios';

// ✅ Criação da instância Axios para chamadas à API do backend
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 🔗 URL base do backend
  headers: {
    'Content-Type': 'application/json', // 🧾 Todas as requisições enviam JSON
  },
});

// ✅ Interceptor que anexa o token JWT a todas as requisições
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // 🔒 Recupera o token armazenado localmente
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // 🔗 Adiciona o token no cabeçalho
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Serviço de autenticação com login e logout
const authService = {
  // 🔐 Login: envia email e senha e salva o token
  login: async (email, password) => {
    try {
      const response = await api.post('/authentication/login', {
        email,
        password,
      });

      const token = response.data?.token;

      if (token) {
        localStorage.setItem('token', token); // 💾 Salva token no navegador
        return response.data; // 🔁 Retorna dados completos (mensagem + token)
      } else {
        throw new Error('Token não recebido do servidor.');
      }
    } catch (error) {
      console.error(
        'Erro ao fazer login:',
        error.response?.data || error.message
      );

      // 🧱 Tratamento de erros mais claros
      if (error.response?.status === 401) {
        throw new Error('Credenciais inválidas.');
      } else if (error.response?.status === 403) {
        throw new Error('Acesso negado.');
      } else {
        throw new Error(error.response?.data?.message || 'Erro desconhecido.');
      }
    }
  },

  // 🚪 Logout: remove o token do armazenamento local
  logout: () => {
    localStorage.removeItem('token');
  },
};

// 🔁 Exporta API para chamadas gerais e serviço de autenticação
export { api, authService };
