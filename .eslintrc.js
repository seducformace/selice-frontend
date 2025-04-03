module.exports = {
  root: true, // Define que esta é a configuração raiz do ESLint
  env: {
    node: true, // Ambiente Node.js
  },
  extends: [
    'plugin:vue/vue3-essential', // Regras essenciais do Vue 3
    'eslint:recommended', // Regras recomendadas do ESLint
    'plugin:prettier/recommended', // Integração do Prettier com ESLint
  ],
  parserOptions: {
    parser: '@babel/eslint-parser', // Utiliza o parser Babel para suporte avançado a JavaScript
  },
  rules: {
    'vue/multi-word-component-names': 'off', // Desativa a regra de nomes multi-palavra
  },
};
