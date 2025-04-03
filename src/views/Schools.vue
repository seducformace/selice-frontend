<template>
  <div class="schools-page">
    <!-- Cabeçalho (Header) -->
    <Header />

    <div class="content-wrapper">
      <div class="content">
        <!-- Barra Superior: Botão Home + Título -->
        <div class="header-actions">
          <!-- Botão Home com ícone de casa FontAwesome -->
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Escolas</h1>
        </div>

        <!-- Barra de Ações: Botão "Adicionar" + Campo de Busca -->
        <div class="actions">
          <button class="add-button" @click="openModal">
            Adicionar Escola
          </button>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar escola..."
            class="search-input"
          />
        </div>

        <!-- Tabela de Escolas -->
        <div class="table-form-wrapper">
          <div class="table-container">
            <table class="schools-table">
              <thead>
                <tr>
                  <th class="col-name">Nome</th>
                  <th class="col-city">Município</th>
                  <th class="col-type">Tipo</th>
                  <th class="col-status">Status</th>
                  <th class="col-phone">Telefone</th>
                  <th class="col-email">E-mail</th>
                  <th class="col-actions">Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(school, index) in filteredSchools" :key="index">
                  <td>{{ school.name }}</td>
                  <td>{{ school.city }}</td>
                  <td>{{ school.type }}</td>
                  <td>
                    <span
                      :class="{
                        'active-status': school.status === 'Ativa',
                        'inactive-status': school.status === 'Inativa',
                      }"
                    >
                      {{ school.status }}
                    </span>
                  </td>
                  <td>{{ school.phone }}</td>
                  <td>{{ school.email }}</td>
                  <td class="action-buttons">
                    <button class="edit-button" @click="editSchool(index)">
                      Editar
                    </button>
                    <button class="delete-button" @click="deleteSchool(index)">
                      Excluir
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Modal para Adicionar/Editar Escola -->
      <div v-if="isModalOpen" class="modal-overlay">
        <div class="modal modal-centered">
          <h2>{{ isEditing ? 'Editar Escola' : 'Adicionar Escola' }}</h2>

          <form @submit.prevent="saveSchool">
            <!-- Nome da Escola -->
            <div class="form-group">
              <label for="nome">Nome da Escola</label>
              <input
                id="nome"
                type="text"
                v-model="currentSchool.name"
                class="form-control"
                placeholder="Digite o nome da escola..."
                required
              />
            </div>

            <!-- Município -->
            <div class="form-group">
              <label for="city">Município</label>
              <select
                id="city"
                v-model="currentSchool.city"
                class="form-control"
                required
              >
                <option disabled value="">Selecione o município</option>
                <option
                  v-for="option in municipalities"
                  :key="option"
                  :value="option"
                >
                  {{ option }}
                </option>
              </select>
            </div>

            <!-- Tipo -->
            <div class="form-group">
              <label for="type">Tipo</label>
              <select
                id="type"
                v-model="currentSchool.type"
                class="form-control"
                required
              >
                <option>Pública</option>
                <option>Privada</option>
              </select>
            </div>

            <!-- Status -->
            <div class="form-group">
              <label for="status">Status</label>
              <select
                id="status"
                v-model="currentSchool.status"
                class="form-control"
                required
              >
                <option>Ativa</option>
                <option>Inativa</option>
              </select>
            </div>

            <!-- Faculdade (dropdown) -->
            <div class="form-group">
              <label for="college">Faculdade</label>
              <select
                id="college"
                v-model="currentSchool.collegeId"
                class="form-control"
                required
              >
                <option disabled value="">Selecione a faculdade</option>
                <option
                  v-for="college in collegesList"
                  :key="college.id"
                  :value="college.id"
                >
                  {{ college.name }}
                </option>
              </select>
            </div>

            <!-- Escola onde o aluno deve estagiar -->
            <div class="form-group">
              <label for="stageSchool">Escola para Estágio</label>
              <select
                id="stageSchool"
                v-model="currentSchool.stageSchoolId"
                class="form-control"
                required
              >
                <option disabled value="">Selecione a escola de estágio</option>
                <option
                  v-for="school in schoolsList"
                  :key="school.id"
                  :value="school.id"
                >
                  {{ school.name }}
                </option>
              </select>
            </div>

            <!-- Telefone -->
            <div class="form-group">
              <label for="phone">Telefone</label>
              <input
                id="phone"
                type="text"
                v-model="currentSchool.phone"
                class="form-control"
                placeholder="Ex.: (85) 99999-9999"
              />
            </div>

            <!-- E-mail -->
            <div class="form-group">
              <label for="email">E-mail</label>
              <input
                id="email"
                type="email"
                v-model="currentSchool.email"
                class="form-control"
                placeholder="exemplo@escola.gov.br"
              />
            </div>

            <!-- Botões -->
            <div class="button-group">
              <button type="submit" class="save-button">Salvar</button>
              <button type="button" class="cancel-button" @click="closeModal">
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!-- Rodapé -->
    <Footer class="footer-fixed" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

export default {
  name: 'SchoolsPage',
  components: { Header, Footer },
  data() {
    return {
      // Filtro de busca
      searchQuery: '',
      // Controle do modal
      isModalOpen: false,
      isEditing: false,
      // Escola atual (formulário)
      currentSchool: {
        name: '',
        inepCode: '',
        city: '',
        type: 'Pública',
        status: 'Ativa',
        director: '',
        phone: '',
        email: '',
      },
      // Lista de escolas exibidas na tabela
      schools: [
        {
          name: 'EEFM Paulo Sarasate',
          inepCode: '23000001',
          city: 'Fortaleza',
          type: 'Pública',
          status: 'Ativa',
          director: 'Fulano de Tal',
          phone: '(85) 99999-9999',
          email: 'fulano@escola.gov.br',
        },
      ],
      // Lista para autocomplete
      publicSchoolsCE: [
        {
          inepCode: '23000001',
          name: 'EEFM Paulo Sarasate',
          city: 'Fortaleza',
        },
        {
          inepCode: '23000002',
          name: 'EEFM Governador Adauto Bezerra',
          city: 'Crato',
        },
        {
          inepCode: '23000003',
          name: 'EEFM Liceu do Ceará',
          city: 'Fortaleza',
        },
        { inepCode: '23000004', name: 'EEFM Domingos Sávio', city: 'Sobral' },
      ],
      showSuggestions: false,
      // Municípios (exemplo)
      municipalities: [
        'Abaiara',
        'Acarapé',
        'Acaraú',
        'Acopiara',
        'Aiuaba',
        // ...
        'Viçosa do Ceará',
      ],
    };
  },
  computed: {
    // Filtra escolas pela busca
    filteredSchools() {
      return this.schools.filter((school) =>
        school.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    // Filtra sugestões (autocomplete) pelo nome
    filteredSuggestions() {
      if (!this.currentSchool.name) return [];
      const searchName = this.currentSchool.name.toLowerCase();
      return this.publicSchoolsCE.filter((school) =>
        school.name.toLowerCase().includes(searchName)
      );
    },
  },
  methods: {
    // Atualiza as sugestões
    onNameInput() {
      this.showSuggestions = !!this.currentSchool.name;
    },
    // Seleciona uma sugestão
    selectSchool(school) {
      this.currentSchool.name = school.name;
      this.currentSchool.inepCode = school.inepCode;
      this.currentSchool.city = school.city;
      this.showSuggestions = false;
    },
    // Navegação (botão Home)
    goToDashboard() {
      this.$router.push('/dashboard');
    },
    // Abre modal para adicionar
    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.currentSchool = {
        name: '',
        inepCode: '',
        city: '',
        type: 'Pública',
        status: 'Ativa',
        director: '',
        phone: '',
        email: '',
      };
    },
    // Fecha modal
    closeModal() {
      this.isModalOpen = false;
      this.showSuggestions = false;
    },
    // Edita escola
    editSchool(index) {
      this.isEditing = true;
      this.currentSchool = { ...this.schools[index] };
      this.isModalOpen = true;
    },
    // Salva (adiciona/atualiza)
    saveSchool() {
      if (this.isEditing) {
        const index = this.schools.findIndex(
          (s) => s.inepCode === this.currentSchool.inepCode
        );
        if (index !== -1) {
          this.schools[index] = { ...this.currentSchool };
        } else {
          // Tenta achar pelo nome, caso o INEP não bata
          const idxByName = this.schools.findIndex(
            (s) => s.name === this.currentSchool.name
          );
          if (idxByName !== -1) {
            this.schools[idxByName] = { ...this.currentSchool };
          } else {
            // Se não achar, adiciona
            this.schools.push({ ...this.currentSchool });
          }
        }
      } else {
        // Nova escola
        this.schools.push({ ...this.currentSchool });
      }
      this.closeModal();
    },
    // Exclui escola
    deleteSchool(index) {
      this.schools.splice(index, 1);
    },
  },
};
</script>

<style scoped>
/* ===== LAYOUT GERAL ===== */
.content-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  max-width: 1200px;
  margin: 20px auto;
}

/*
  header-actions: Botão Home + Título ao lado
  actions: Botão Adicionar + Campo de Busca
*/
.header-actions,
.actions {
  display: flex;
  align-items: center; /* Alinha verticalmente ao centro */
  gap: 10px; /* Espaço entre os itens */
  margin-bottom: 20px;
}

/* HOME-BUTTON estilo circular verde */
.home-button {
  background-color: #00923f; /* verde */
  color: white; /* icone branco */
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%; /* formato circular */
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px; /* Tamanho do ícone */
}

/* Botão "Adicionar Escola" */
.add-button {
  background-color: #00923f;
  color: white;
  padding: 8px 12px;
  border: none;
  cursor: pointer;
}

/* Campo de busca */
.search-input {
  width: 250px;
  padding: 6px;
}

/* ===== TABELA ===== */
.table-container {
  margin: 20px 0;
}

/* Mantemos layout fixo e definimos larguras em %.
   A soma das larguras das colunas dá 100% */
.schools-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

/*
  Larguras em % (9 colunas):
  12 + 10 + 12 + 10 + 10 + 12 + 10 + 12 + 12 = 100%
*/
.col-name {
  width: 12%;
}
.col-inep {
  width: 10%;
}
.col-city {
  width: 12%;
}
.col-type {
  width: 10%;
}
.col-status {
  width: 10%;
}
.col-director {
  width: 12%;
}
.col-phone {
  width: 10%;
}
.col-email {
  width: 12%;
}
.col-actions {
  width: 12%;
}

/* Células: truncamos texto para não estufar a linha */
.schools-table th,
.schools-table td {
  border: 1px solid #ccc;
  padding: 12px 16px;
  vertical-align: middle;
  height: 60px;
  line-height: 1.2;

  /* Impede quebra de linha; se exceder, aparece "..." */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.schools-table th {
  background-color: #00923f;
  color: white;
  text-align: center;
}

/* Centraliza a coluna "Ações" */
.schools-table td.col-actions {
  text-align: center;
  vertical-align: middle;
}

/* Botões de ação (Editar/Excluir) */
.action-buttons {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.edit-button {
  background-color: #ccc;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  margin-right: 8px; /* Espaço entre "Editar" e "Excluir" */
  border-radius: 4px;
}
.delete-button {
  background-color: #ff6565;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
}

/* ===== MODAL ===== */
.modal-overlay {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.modal-centered {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
}

/* Formulário do modal */
.form-group {
  margin-bottom: 12px;
}
.form-group label {
  display: block;
  margin-bottom: 4px;
}
.form-control {
  width: 100%;
  padding: 6px;
  box-sizing: border-box;
}

/* Botões do formulário (Salvar e Cancelar) */
.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
}
.save-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
}
.cancel-button {
  background-color: #ccc;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
}

/* ===== AUTOCOMPLETE ===== */
.autocomplete-wrapper {
  position: relative;
}
.suggestions {
  position: absolute;
  z-index: 10;
  background: white;
  margin: 0;
  padding: 0;
  list-style: none;
  border: 1px solid #ccc;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
}
.suggestions li {
  padding: 8px;
  cursor: pointer;
}
.suggestions li:hover {
  background-color: #eee;
}

/* Status Ativa/Inativa */
.active-status {
  color: green;
  font-weight: bold;
}
.inactive-status {
  color: red;
  font-weight: bold;
}

/* Rodapé fixo, se necessário */
.footer-fixed {
  margin-top: auto;
}
</style>
