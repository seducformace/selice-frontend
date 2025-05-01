<template>
  <div class="coordinators-page">
    <div class="content-wrapper">
      <div class="content">
        <div class="header-actions">
          <button class="home-button" @click="goHome">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Coordenadores</h1>
        </div>

        <div class="actions">
          <button class="add-button" @click="openModal">
            Adicionar Coordenador
          </button>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar coordenador..."
            class="search-input"
          />
        </div>

        <div class="table-container">
          <table class="coordinators-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Telefone</th>
                <th>CPF</th>
                <th>Instituição</th>
                <th>Curso Responsável</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(coordinator, index) in paginatedCoordinators"
                :key="index"
              >
                <td>{{ coordinator.name }}</td>
                <td>{{ coordinator.email }}</td>
                <td>{{ coordinator.phone }}</td>
                <td>{{ coordinator.cpf }}</td>
                <td>
                  {{
                    coordinator.college?.name || coordinator.school?.name || '—'
                  }}
                </td>
                <td>{{ coordinator.course }}</td>
                <td>
                  <span
                    :class="{
                      'active-status': coordinator.status === 'Ativo',
                      'inactive-status': coordinator.status === 'Inativo',
                    }"
                  >
                    {{ coordinator.status }}
                  </span>
                </td>
                <td class="action-buttons">
                  <button
                    class="edit-button"
                    @click="
                      editCoordinator(index + (currentPage - 1) * itemsPerPage)
                    "
                  >
                    Editar
                  </button>
                  <button
                    class="delete-button"
                    @click="
                      deleteCoordinator(
                        index + (currentPage - 1) * itemsPerPage
                      )
                    "
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="pagination" v-if="totalPages > 1">
            <button
              v-for="page in totalPages"
              :key="page"
              @click="currentPage = page"
              :class="{ active: currentPage === page }"
            >
              {{ page }}
            </button>
          </div>
        </div>

        <div v-if="isModalOpen" class="modal-overlay">
          <div class="modal-aligned modal-form-box">
            <h2>
              {{ isEditing ? 'Editar Coordenador' : 'Adicionar Coordenador' }}
            </h2>

            <form @submit.prevent="saveCoordinator" class="coordinator-form">
              <div class="form-group">
                <label for="name">Nome</label>
                <input
                  id="name"
                  v-model="currentCoordinator.name"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="email">E-mail</label>
                <input
                  id="email"
                  v-model="currentCoordinator.email"
                  type="email"
                  required
                />
              </div>

              <div class="form-group">
                <label for="cpf">CPF</label>
                <input
                  id="cpf"
                  v-model="currentCoordinator.cpf"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="phone">Telefone</label>
                <input
                  id="phone"
                  v-model="currentCoordinator.phoneNumber"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="institution">Faculdade / Escola</label>
                <select
                  id="institution"
                  v-model="currentCoordinator.institutionId"
                  @change="handleInstitutionChange"
                  required
                >
                  <option disabled value="">Selecione uma instituição</option>
                  <option
                    v-for="inst in institutionsList"
                    :key="`${inst.type}-${inst.id}`"
                    :value="inst.id"
                  >
                    {{ inst.name }} ({{
                      inst.type === 'college' ? 'Faculdade' : 'Escola'
                    }})
                  </option>
                </select>
              </div>

              <div class="form-group">
                <label for="department">Curso Responsável</label>
                <input
                  id="department"
                  v-model="currentCoordinator.department"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="status">Status</label>
                <select
                  id="status"
                  v-model="currentCoordinator.status"
                  required
                >
                  <option value="Ativo">Ativo</option>
                  <option value="Inativo">Inativo</option>
                </select>
              </div>

              <div class="form-buttons">
                <button type="submit" class="save-button">Salvar</button>
                <button type="button" class="cancel-button" @click="closeModal">
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <Footer class="footer-fixed" />
  </div>
</template>
<script>
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

export default {
  name: 'CoordinatorPage',
  components: {
    Footer,
  },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      currentPage: 1,
      itemsPerPage: 5,
      currentCoordinator: {
        id: null,
        name: '',
        email: '',
        cpf: '',
        phoneNumber: '',
        department: '',
        status: 'Ativo',
        institutionId: null,
        institutionType: '',
      },
      coordinators: [],
      institutionsList: [],
      collegesList: [],
      schoolsList: [],
    };
  },

  computed: {
    filteredCoordinators() {
      return this.coordinators.filter((c) =>
        c.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    paginatedCoordinators() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredCoordinators.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredCoordinators.length / this.itemsPerPage);
    },
  },

  methods: {
    goHome() {
      this.$router.push('/dashboard');
    },

    async fetchCoordinators() {
      try {
        const response = await api.get('/coordinators');
        this.coordinators = response.data.map((c) => ({
          ...c,
          college: c.faculty || null,
          school: c.school || null,
          course: c.department || '—',
          phone: c.phoneNumber || '',
          cpf: c.cpf || '',
          status: c.status || 'Ativo',
        }));
      } catch (error) {
        console.error('Erro ao buscar coordenadores:', error);
      }
    },

    async fetchInstitutions() {
      try {
        const [collegesResponse, schoolsResponse] = await Promise.all([
          api.get('/faculties'),
          api.get('/schools'),
        ]);

        this.collegesList = collegesResponse.data;
        this.schoolsList = schoolsResponse.data;

        this.institutionsList = [
          ...this.collegesList.map((c) => ({ ...c, type: 'college' })),
          ...this.schoolsList.map((s) => ({ ...s, type: 'school' })),
        ];
      } catch (error) {
        console.error('Erro ao carregar instituições:', error);
      }
    },

    handleInstitutionChange() {
      const selected = this.institutionsList.find(
        (inst) => inst.id === this.currentCoordinator.institutionId
      );

      this.currentCoordinator.institutionType = selected?.type || '';
    },

    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.resetCurrentCoordinator();
    },

    closeModal() {
      this.isModalOpen = false;
    },

    editCoordinator(index) {
      const selected = this.coordinators[index];
      this.isEditing = true;

      let institutionId = null;
      let institutionType = '';

      if (selected.college) {
        institutionId = selected.college.id;
        institutionType = 'college';
      } else if (selected.school) {
        institutionId = selected.school.id;
        institutionType = 'school';
      }

      this.currentCoordinator = {
        id: selected.id,
        name: selected.name,
        email: selected.email,
        cpf: selected.cpf,
        phoneNumber: selected.phone,
        department: selected.course,
        status: selected.status,
        institutionId,
        institutionType,
      };

      this.isModalOpen = true;
    },

    async saveCoordinator() {
      try {
        if (
          !this.currentCoordinator.institutionType ||
          !this.currentCoordinator.institutionId
        ) {
          alert('Selecione uma Faculdade ou Escola antes de salvar.');
          return;
        }

        const payload = {
          name: this.currentCoordinator.name?.trim(),
          email: this.currentCoordinator.email?.trim(),
          cpf: this.currentCoordinator.cpf?.trim(),
          phoneNumber: this.currentCoordinator.phoneNumber?.trim(),
          department: this.currentCoordinator.department?.trim(),
          status: this.currentCoordinator.status,
          faculty: null,
          school: null,
        };

        if (this.currentCoordinator.institutionType === 'college') {
          payload.faculty = { id: this.currentCoordinator.institutionId };
        } else if (this.currentCoordinator.institutionType === 'school') {
          payload.school = { id: this.currentCoordinator.institutionId };
        }

        if (!payload.faculty && !payload.school) {
          alert('Erro: Nenhuma instituição vinculada.');
          return;
        }

        if (this.isEditing && this.currentCoordinator.id) {
          await api.put(`/coordinators/${this.currentCoordinator.id}`, payload);
        } else {
          await api.post('/coordinators', payload);
        }

        await this.fetchCoordinators();
        this.closeModal();
      } catch (error) {
        console.error(
          'Erro ao salvar coordenador:',
          error?.response?.data || error
        );
        alert(
          'Erro ao salvar coordenador. Verifique os dados e tente novamente.'
        );
      }
    },

    async deleteCoordinator(index) {
      const id = this.coordinators[index].id;
      if (confirm('Tem certeza que deseja excluir este coordenador?')) {
        try {
          await api.delete(`/coordinators/${id}`);
          await this.fetchCoordinators();
        } catch (error) {
          console.error('Erro ao excluir coordenador:', error);
          alert('Erro ao excluir coordenador.');
        }
      }
    },

    resetCurrentCoordinator() {
      this.currentCoordinator = {
        id: null,
        name: '',
        email: '',
        cpf: '',
        phoneNumber: '',
        department: '',
        status: 'Ativo',
        institutionId: null,
        institutionType: '',
      };
    },
  },

  mounted() {
    this.fetchCoordinators();
    this.fetchInstitutions();
  },
};
</script>

<style scoped>
/* ======== Layout Geral ======== */
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

/* Header + Título */
.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}
.home-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

/* Ações (Adicionar, Buscar) */
.actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.add-button {
  background-color: #00923f;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}
.search-input {
  padding: 10px;
  width: 300px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Tabela de Coordenadores */
.table-container {
  overflow-x: auto;
}
.coordinators-table {
  width: 100%;
  border-collapse: collapse;
}
.coordinators-table th,
.coordinators-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}
.coordinators-table th {
  background-color: #00923f;
  color: white;
  font-weight: bold;
}
.action-buttons {
  display: flex;
  gap: 8px;
}
.edit-button {
  background-color: #ccc;
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
.delete-button {
  background-color: #ff6565;
  color: white;
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

/* Status (Ativo/Inativo) */
.active-status {
  color: green;
  font-weight: bold;
}
.inactive-status {
  color: red;
  font-weight: bold;
}

/* ======== Modal ======== */
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
  z-index: 999;
  padding: 20px;
  overflow-y: auto;
}

/*
  AUMENTAMOS A ÁREA DO MODAL
  -> max-width: 900px;
  -> width: 90%;
*/
.modal-aligned.modal-form-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 900px; /* Maior largura */
  width: 90%; /* Ocupa 90% da tela */
  box-sizing: border-box;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

/* Layout em 2 colunas, gap maior (20px) */
.coordinator-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 10px;
}

/* Cada campo */
.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}
.form-group input,
.form-group select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Botões do Formulário */
.form-buttons {
  display: flex;
  justify-content: space-between;
  grid-column: span 2;
  margin-top: 15px;
}
.save-button,
.cancel-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}
.save-button {
  background-color: #00923f;
  color: white;
}
.cancel-button {
  background-color: #d9534f;
  color: white;
}

/* Responsivo: se < 600px, 1 coluna */
@media (max-width: 600px) {
  .coordinator-form {
    grid-template-columns: 1fr;
  }
  .form-buttons {
    flex-direction: column;
    gap: 10px;
  }
}

/* Rodapé fixo (opcional) */
.footer-fixed {
  width: 100%;
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 999;
}
</style>
