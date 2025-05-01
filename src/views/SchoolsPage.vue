<template>
  <div class="schools-page">
    <div class="content-wrapper">
      <div class="content">
        <!-- Barra Superior: Botão Home + Título -->
        <div class="header-actions">
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
                  <th class="col-inep">INEP</th>
                  <th class="col-city">Município</th>
                  <th class="col-address">Endereço</th>
                  <th class="col-type">Tipo</th>
                  <th class="col-status">Status</th>
                  <th class="col-phone">Telefone</th>
                  <th class="col-email">E-mail</th>
                  <th class="col-colleges">Faculdades</th>
                  <th class="col-actions">Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(school, index) in filteredSchools" :key="index">
                  <td :title="school.name" class="expandable-cell">
                    {{ school.name }}
                  </td>
                  <td :title="school.inepCode">{{ school.inepCode || '—' }}</td>
                  <td>{{ school.city }}</td>
                  <td :title="school.address" class="expandable-cell">
                    {{ school.address || '—' }}
                  </td>
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
                  <td :title="school.phone" class="expandable-cell">
                    {{ school.phone }}
                  </td>
                  <td :title="school.email" class="expandable-cell">
                    {{ school.email }}
                  </td>
                  <td>
                    <span v-if="school.colleges?.length">
                      {{ school.colleges.map((c) => c.name).join(', ') }}
                    </span>
                    <span v-else>—</span>
                  </td>
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

            <div
              v-if="formMessage"
              :class="{
                'message-success': formSuccess,
                'message-error': !formSuccess,
              }"
              class="form-message"
            >
              {{ formMessage }}
            </div>

            <div class="form-group">
              <label for="inep">Código INEP</label>
              <input
                id="inep"
                type="text"
                v-model="currentSchool.inepCode"
                class="form-control"
                placeholder="Digite o código INEP..."
                required
              />
            </div>

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

            <div class="form-group">
              <label for="address">Endereço</label>
              <input
                id="address"
                type="text"
                v-model="currentSchool.address"
                class="form-control"
                placeholder="Digite o endereço da escola..."
              />
            </div>

            <input type="hidden" v-model="currentSchool.state" />

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

            <div class="form-group">
              <label for="colleges">Faculdades Vinculadas</label>
              <div style="display: flex; gap: 10px; align-items: center">
                <select
                  id="colleges"
                  v-model="selectedCollegeId"
                  class="form-control"
                >
                  <option disabled value="">Selecione uma faculdade</option>
                  <option
                    v-for="college in collegesList"
                    :key="college.id"
                    :value="college.id"
                    :disabled="currentSchool.collegeIds.includes(college.id)"
                  >
                    {{ college.name }}
                  </option>
                </select>
                <button
                  type="button"
                  class="add-button"
                  style="height: 38px"
                  @click="addCollege"
                >
                  Adicionar
                </button>
              </div>
              <ul style="margin-top: 10px; padding-left: 20px">
                <li
                  v-for="id in currentSchool.collegeIds"
                  :key="id"
                  style="margin-bottom: 5px"
                >
                  {{ getCollegeName(id) }}
                  <button
                    type="button"
                    style="margin-left: 10px; color: red"
                    @click="removeCollege(id)"
                  >
                    Remover
                  </button>
                </li>
              </ul>
              <small class="text-muted"
                >Você pode deixar em branco ou selecionar múltiplas
                faculdades.</small
              >
            </div>

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
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

export default {
  name: 'SchoolsPage',
  components: { Footer },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      formMessage: '',
      formSuccess: false,
      selectedCollegeId: null,
      currentSchool: {
        id: null,
        name: '',
        inepCode: '',
        city: '',
        state: 'CE',
        type: 'Pública',
        status: 'Ativa',
        address: '',
        phone: '',
        email: '',
        collegeIds: [],
      },
      schools: [],
      collegesList: [],
      municipalities: [
        'Abaiara',
        'Acarapé',
        'Acaraú',
        'Acopiara',
        'Aiuaba',
        'Fortaleza',
        'Crato',
        'Sobral',
        'Viçosa do Ceará',
      ],
    };
  },
  computed: {
    filteredSchools() {
      const query = this.searchQuery.toLowerCase();
      return this.schools.filter(
        (school) =>
          (school.name ?? '').toLowerCase().includes(query) ||
          (school.inepCode ?? '').toLowerCase().includes(query)
      );
    },
  },
  methods: {
    goToDashboard() {
      this.$router.push('/dashboard');
    },
    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.selectedCollegeId = null;
      this.currentSchool = {
        id: null,
        name: '',
        inepCode: '',
        city: '',
        state: 'CE',
        type: 'Pública',
        status: 'Ativa',
        address: '',
        phone: '',
        email: '',
        collegeIds: [],
      };
    },
    closeModal() {
      this.isModalOpen = false;
    },
    async fetchSchools() {
      try {
        const response = await api.get('/schools');
        this.schools = response.data.map((school) => ({
          ...school,
          inepCode: school.inepCode ?? '—',
          address: school.address ?? '',
          phone: school.phone ?? '',
          email: school.email ?? '',
          type: school.type ?? '',
          status: school.status ?? '',
          colleges: school.colleges ?? [],
        }));
      } catch (error) {
        console.error('Erro ao carregar escolas:', error);
      }
    },
    async fetchFaculties() {
      try {
        const response = await api.get('/faculties');
        this.collegesList = response.data;
      } catch (error) {
        console.error('Erro ao carregar faculdades:', error);
      }
    },
    editSchool(index) {
      const selected = this.schools[index];
      this.isEditing = true;
      this.currentSchool = {
        id: selected.id,
        name: selected.name,
        inepCode: selected.inepCode,
        city: selected.city,
        state: selected.state,
        type: selected.type,
        status: selected.status,
        address: selected.address,
        phone: selected.phone,
        email: selected.email,
        collegeIds: selected.colleges?.map((c) => c.id) || [],
      };
      this.selectedCollegeId = null;
      this.isModalOpen = true;
    },
    async saveSchool() {
      const payload = {
        name: this.currentSchool.name,
        inepCode: this.currentSchool.inepCode,
        city: this.currentSchool.city,
        state: this.currentSchool.state,
        type: this.currentSchool.type,
        status: this.currentSchool.status,
        address: this.currentSchool.address,
        phone: this.currentSchool.phone,
        email: this.currentSchool.email,
        colleges: this.currentSchool.collegeIds.map((id) => ({ id })),
      };

      try {
        if (this.isEditing && this.currentSchool.id) {
          await api.put(`/schools/${this.currentSchool.id}`, payload);
          this.formMessage = 'Escola atualizada com sucesso!';
        } else {
          await api.post('/schools', payload);
          this.formMessage = 'Escola cadastrada com sucesso!';
        }

        this.formSuccess = true;

        setTimeout(() => {
          this.formMessage = '';
          this.closeModal();
          this.fetchSchools();
        }, 3000);
      } catch (error) {
        console.error('Erro ao salvar escola:', error);
        this.formMessage = 'Erro ao salvar escola. Verifique os dados.';
        this.formSuccess = false;

        setTimeout(() => {
          this.formMessage = '';
        }, 5000);
      }
    },
    async deleteSchool(index) {
      const id = this.schools[index].id;
      if (confirm('Deseja excluir esta escola?')) {
        try {
          await api.delete(`/schools/${id}`);
          await this.fetchSchools();
        } catch (error) {
          console.error('Erro ao excluir escola:', error);
        }
      }
    },
    getCollegeName(id) {
      const college = this.collegesList.find((c) => c.id === id);
      return college ? college.name : 'Faculdade não encontrada';
    },
    addCollege() {
      if (
        this.selectedCollegeId &&
        !this.currentSchool.collegeIds.includes(this.selectedCollegeId)
      ) {
        this.currentSchool.collegeIds.push(this.selectedCollegeId);
        this.selectedCollegeId = null;
      }
    },
    removeCollege(id) {
      this.currentSchool.collegeIds = this.currentSchool.collegeIds.filter(
        (cid) => cid !== id
      );
    },
  },
  mounted() {
    this.fetchSchools();
    this.fetchFaculties();
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
.col-address {
  padding: 8px;
  text-align: left;
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
.form-message {
  margin-top: 10px;
  padding: 8px;
  border-radius: 4px;
  font-weight: bold;
  font-size: 0.9rem;
  text-align: center;
}

.message-success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.message-error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
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
.col-address {
  width: 12%;
} /* <- ADICIONADO AGORA */
.col-type {
  width: 10%;
}
.col-status {
  width: 10%;
}
.col-phone {
  width: 10%;
}
.col-email {
  width: 12%;
}
.col-actions {
  width: 15%;
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
.form-message {
  margin-top: 10px;
  padding: 8px;
  border-radius: 4px;
  font-weight: bold;
  font-size: 0.9rem;
  text-align: center;
}
.expandable-cell {
  white-space: normal !important;
  word-break: break-word;
  overflow-wrap: anywhere;
}
.message-success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}
.expandable-cell {
  max-width: 180px;
  white-space: normal;
  word-break: break-word;
  overflow-wrap: break-word;
  cursor: help;
}
.message-error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
.form-control {
  width: 100%;
  padding: 8px 12px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #fff;
  box-sizing: border-box;
  margin-bottom: 10px;
  transition: border-color 0.3s;
}

.form-control:focus {
  border-color: #007bff; /* azul leve no foco */
  outline: none;
}
</style>
