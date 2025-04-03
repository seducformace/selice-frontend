<template>
  <div class="faculties-page">
    <!-- Cabeçalho -->
    <Header />

    <div class="content-wrapper">
      <div class="content">
        <!-- Botão Home e Título da Página -->
        <div class="header-actions">
          <button class="home-button" @click="goHome">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Faculdades</h1>
        </div>

        <!-- Ações: Botão para adicionar nova faculdade e campo de busca -->
        <div class="actions">
          <button class="add-button" @click="openModal">
            Adicionar Nova Faculdade
          </button>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar faculdade..."
            class="search-input"
          />
        </div>

        <!-- Tabela de Faculdades -->
        <div class="table-container">
          <table class="faculties-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Código MEC</th>
                <th style="width: 220px">CNPJ</th>
                <th>Reitor</th>
                <th>Responsável pela Parceria</th>
                <th style="width: 200px">Contato</th>
                <th>Cidade</th>
                <th>Estado</th>
                <th>Cursos Oferecidos</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(faculty, index) in filteredFaculties" :key="index">
                <td>{{ faculty.name }}</td>
                <td>{{ faculty.mecCode }}</td>
                <td>{{ faculty.cnpj }}</td>
                <td>{{ faculty.deanName }}</td>
                <td>{{ faculty.partnershipResponsible }}</td>
                <td>{{ faculty.contactPhone }}</td>
                <td>{{ faculty.city }}</td>
                <td>{{ faculty.state }}</td>
                <td>
                  {{
                    Array.isArray(faculty.offeredCourses)
                      ? faculty.offeredCourses.join(', ')
                      : ''
                  }}
                </td>
                <td class="action-buttons">
                  <button
                    class="action-button edit-button"
                    @click="editFaculty(index)"
                  >
                    Editar
                  </button>
                  <button
                    class="action-button delete-button"
                    @click="deleteFaculty(index)"
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Modal para Adicionar/Editar Faculdades (2 colunas) -->
      <div v-if="isModalOpen" class="modal-overlay">
        <div class="modal-form-box">
          <h2>{{ isEditing ? 'Editar Faculdade' : 'Adicionar Faculdade' }}</h2>

          <!-- Formulário em Grid, 2 colunas -->
          <form @submit.prevent="saveFaculty" class="faculty-form">
            <!-- Código MEC -->
            <div class="form-group">
              <label for="mecCode">Código MEC</label>
              <input
                id="mecCode"
                v-model="currentFaculty.mecCode"
                type="text"
                placeholder="Código MEC"
              />
            </div>

            <!-- Nome da Faculdade -->
            <div class="form-group">
              <label for="name">Nome da Faculdade</label>
              <input
                id="name"
                v-model="currentFaculty.name"
                type="text"
                placeholder="Nome da Faculdade"
                required
              />
            </div>

            <!-- CNPJ -->
            <div class="form-group">
              <label for="cnpj">CNPJ</label>
              <input
                id="cnpj"
                v-model="currentFaculty.cnpj"
                type="text"
                placeholder="CNPJ"
                required
              />
            </div>

            <!-- Nome do Reitor -->
            <div class="form-group">
              <label for="rector">Nome do Reitor</label>
              <input
                id="rector"
                v-model="currentFaculty.rector"
                type="text"
                placeholder="Nome do Reitor"
                required
              />
            </div>

            <!-- Responsável pela Parceria -->
            <div class="form-group">
              <label for="partnershipResponsible"
                >Responsável pela Parceria</label
              >
              <input
                id="partnershipResponsible"
                v-model="currentFaculty.partnershipResponsible"
                type="text"
                placeholder="Responsável pela Parceria"
                required
              />
            </div>

            <!-- Contato (Telefone) -->
            <div class="form-group">
              <label for="contact">Contato (Telefone)</label>
              <input
                id="contact"
                v-model="currentFaculty.contact"
                type="text"
                placeholder="Contato (Telefone)"
                required
              />
            </div>

            <!-- Cidade -->
            <div class="form-group">
              <label for="city">Cidade</label>
              <select id="city" v-model="currentFaculty.city" required>
                <option value="" disabled>Selecione a Cidade</option>
                <option v-for="city in cities" :key="city" :value="city">
                  {{ city }}
                </option>
              </select>
            </div>

            <!-- Estado (fixo) -->
            <div class="form-group">
              <label for="state">Estado</label>
              <input
                id="state"
                v-model="currentFaculty.state"
                type="text"
                value="Ceará"
                readonly
              />
            </div>

            <!-- Cursos Oferecidos (vários) -->
            <div class="form-group courses-group">
              <label for="courses">Cursos Oferecidos</label>
              <div class="courses-multiple">
                <select id="courses" v-model="newCourse">
                  <option value="" disabled>Selecione um Curso</option>
                  <option
                    v-for="course in courses"
                    :key="course"
                    :value="course"
                  >
                    {{ course }}
                  </option>
                </select>
                <button type="button" class="add-course-btn" @click="addCourse">
                  Adicionar Curso
                </button>
                <ul>
                  <li
                    v-for="(course, idx) in currentFaculty.courses"
                    :key="idx"
                  >
                    {{ course }}
                    <button @click="removeCourse(idx)">Remover</button>
                  </li>
                </ul>
              </div>
            </div>

            <!-- Botões Salvar/Cancelar -->
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

    <!-- Rodapé -->
    <Footer class="footer-fixed" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { api } from '@/services/api';

export default {
  name: 'FacultiesPage',
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      currentFaculty: {
        name: '',
        mecCode: '',
        cnpj: '',
        rector: '',
        partnershipResponsible: '',
        contact: '',
        city: '',
        state: 'Ceará',
        courses: [],
      },
      newCourse: '',
      faculties: [],
      cities: [
        'Abaiara',
        'Acarape',
        'Acaraú',
        'Acopiara',
        'Aiuaba',
        'Alcântaras',
        'Altaneira',
        'Alto Santo',
        'Amontada',
        'Antonina do Norte',
        'Apuiarés',
        'Aquiraz',
        'Aracati',
        'Aracoiaba',
        'Ararendá',
        'Araripe',
        'Aratuba',
        'Arneiroz',
        'Assaré',
        'Aurora',
        'Baixio',
        'Banabuiú',
        'Barbalha',
        'Barreira',
        'Barro',
        'Barroquinha',
        'Baturité',
        'Beberibe',
        'Bela Cruz',
        'Boa Viagem',
        'Brejo Santo',
        'Camocim',
        'Campos Sales',
        'Canindé',
        'Capistrano',
        'Caridade',
        'Cariré',
        'Caririaçu',
        'Cariús',
        'Carnaubal',
        'Cascavel',
        'Catarina',
        'Catunda',
        'Caucaia',
        'Cedro',
        'Chaval',
        'Choró',
        'Chorozinho',
        'Coreaú',
        'Crateús',
        'Crato',
        'Croatá',
        'Cruz',
        'Deputado Irapuan Pinheiro',
        'Ererê',
        'Eusébio',
        'Farias Brito',
        'Forquilha',
        'Fortaleza',
        'Fortim',
        'Frecheirinha',
        'General Sampaio',
        'Graça',
        'Granja',
        'Granjeiro',
        'Groaíras',
        'Guaiúba',
        'Guaraciaba do Norte',
        'Guaramiranga',
        'Hidrolândia',
        'Horizonte',
        'Ibaretama',
        'Ibiapina',
        'Ibicuitinga',
        'Icapuí',
        'Icó',
        'Iguatu',
        'Independência',
        'Ipaporanga',
        'Ipaumirim',
        'Ipu',
        'Ipueiras',
        'Iracema',
        'Irauçuba',
        'Itaiçaba',
        'Itaitinga',
        'Itapajé',
        'Itapipoca',
        'Itapiúna',
        'Itarema',
        'Itatira',
        'Jaguaretama',
        'Jaguaribara',
        'Jaguaribe',
        'Jaguaruana',
        'Jardim',
        'Jati',
        'Jijoca de Jericoacoara',
        'Juazeiro do Norte',
        'Jucás',
        'Lavras da Mangabeira',
        'Limoeiro do Norte',
        'Madalena',
        'Maracanaú',
        'Maranguape',
        'Marco',
        'Martinópole',
        'Massapê',
        'Mauriti',
        'Meruoca',
        'Milagres',
        'Milhã',
        'Miraíma',
        'Missão Velha',
        'Mombaça',
        'Monsenhor Tabosa',
        'Morada Nova',
        'Moraújo',
        'Morrinhos',
        'Mucambo',
        'Mulungu',
        'Nova Olinda',
        'Nova Russas',
        'Novo Oriente',
        'Ocara',
        'Orós',
        'Pacajus',
        'Pacatuba',
        'Pacoti',
        'Pacujá',
        'Palhano',
        'Palmácia',
        'Paracuru',
        'Paraipaba',
        'Parambu',
        'Paramoti',
        'Pedra Branca',
        'Penaforte',
        'Pentecoste',
        'Pereiro',
        'Pindoretama',
        'Piquet Carneiro',
        'Pires Ferreira',
        'Poranga',
        'Porteiras',
        'Potengi',
        'Potiretama',
        'Quiterianópolis',
        'Quixadá',
        'Quixelô',
        'Quixeramobim',
        'Quixeré',
        'Redenção',
        'Reriutaba',
        'Russas',
        'Saboeiro',
        'Salitre',
        'Santa Quitéria',
        'Santana do Acaraú',
        'Santana do Cariri',
        'São Benedito',
        'São Gonçalo do Amarante',
        'São João do Jaguaribe',
        'São Luís do Curu',
        'Senador Pompeu',
        'Senador Sá',
        'Sobral',
        'Solonópole',
        'Tabuleiro do Norte',
        'Tamboril',
        'Tarrafas',
        'Tauá',
        'Tejuçuoca',
        'Tianguá',
        'Trairi',
        'Tururu',
        'Ubajara',
        'Umari',
        'Umirim',
        'Uruburetama',
        'Uruoca',
        'Varjota',
        'Várzea Alegre',
        'Viçosa do Ceará',
      ],

      courses: [
        'Artes Visuais',
        'Artes Cênicas',
        'Música',
        'Teatro',
        'Dança',
        'Biologia',
        'Matemática',
        'Física',
        'Química',
        'História',
        'Português',
        'Geografia',
        'Educação Física',
        'Computação',
      ],
    };
  },
  computed: {
    filteredFaculties() {
      return this.faculties.filter((faculty) =>
        faculty.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },

  methods: {
    goHome() {
      this.$router.push('/dashboard');
    },
    openModal() {
      this.isModalOpen = true;
      this.isEditing = false;
      this.resetCurrentFaculty();
    },
    closeModal() {
      this.isModalOpen = false;
    },

    editFaculty(index) {
      this.isEditing = true;
      const selected = this.faculties[index];

      this.currentFaculty = {
        id: selected.id,
        name: selected.name,
        mecCode: selected.mecCode,
        cnpj: selected.cnpj,
        rector: selected.deanName, // Mapeando corretamente
        partnershipResponsible: selected.partnershipResponsible,
        contact: selected.contactPhone, // Mapeando corretamente
        city: selected.city,
        state: selected.state,
        courses: Array.isArray(selected.offeredCourses)
          ? [...selected.offeredCourses]
          : [],
      };

      this.isModalOpen = true;
    },

    async saveFaculty() {
      try {
        const requiredFields = {
          'Código MEC': this.currentFaculty.mecCode,
          'Nome da Faculdade': this.currentFaculty.name,
          CNPJ: this.currentFaculty.cnpj,
          'Nome do Reitor': this.currentFaculty.rector,
          'Responsável pela Parceria':
            this.currentFaculty.partnershipResponsible,
          Contato: this.currentFaculty.contact,
          Cidade: this.currentFaculty.city,
        };

        for (const [label, value] of Object.entries(requiredFields)) {
          if (!value || value.trim() === '') {
            alert(`Por favor, preencha o campo obrigatório: ${label}`);
            return;
          }
        }

        const payload = {
          mecCode: this.currentFaculty.mecCode,
          name: this.currentFaculty.name,
          cnpj: this.currentFaculty.cnpj,
          deanName: this.currentFaculty.rector,
          partnershipResponsible: this.currentFaculty.partnershipResponsible,
          contactPhone: this.currentFaculty.contact,
          city: this.currentFaculty.city,
          state: this.currentFaculty.state,
          offeredCourses: this.currentFaculty.courses,
        };

        if (this.isEditing && this.currentFaculty.id) {
          await api.put(`/faculties/${this.currentFaculty.id}`, payload);
          alert('Faculdade atualizada com sucesso!');
        } else {
          await api.post('/faculties', payload);
          alert('Faculdade cadastrada com sucesso!');
        }

        this.loadFaculties();
        this.closeModal();
      } catch (error) {
        console.error('Erro ao salvar faculdade:', error);
        if (
          error.response &&
          error.response.data &&
          error.response.data.message
        ) {
          alert(`Erro: ${error.response.data.message}`);
        } else {
          alert(
            'Erro ao salvar faculdade. Verifique os dados e tente novamente.'
          );
        }
      }
    },

    async deleteFaculty(index) {
      try {
        const faculty = this.faculties[index];
        await api.delete(`/faculties/${faculty.id}`);
        alert('Faculdade excluída com sucesso.');
        this.loadFaculties();
      } catch (error) {
        console.error('Erro ao excluir faculdade:', error);
        alert('Erro ao excluir faculdade.');
      }
    },
    async loadFaculties() {
      try {
        const response = await api.get('/faculties');
        this.faculties = response.data.map((f) => ({
          ...f,
          // Normaliza os nomes dos campos para o que o template espera
          deanName: f.dean_name,
          contactPhone: f.contact_phone,
          partnershipResponsible: f.partnership_responsible,
          courses: f.offeredCourses || [],
        }));
      } catch (error) {
        console.error('Erro ao carregar faculdades:', error);
      }
    },
    resetCurrentFaculty() {
      this.currentFaculty = {
        name: '',
        mecCode: '',
        cnpj: '',
        rector: '',
        partnershipResponsible: '',
        contact: '',
        city: '',
        state: 'Ceará',
        courses: [],
      };
      this.newCourse = '';
    },
    addCourse() {
      if (
        this.newCourse &&
        !this.currentFaculty.courses.includes(this.newCourse)
      ) {
        this.currentFaculty.courses.push(this.newCourse);
      }
      this.newCourse = '';
    },
    removeCourse(courseIndex) {
      this.currentFaculty.courses.splice(courseIndex, 1);
    },
  },
  mounted() {
    this.loadFaculties();
  },
};
</script>

<style scoped>
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

/* Barra Superior (Home + Título) */
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

/* Ações (Add + Busca) */
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

/* Tabela */
.table-container {
  overflow-x: auto;
}
.faculties-table {
  width: 100%;
  border-collapse: collapse;
}
.faculties-table th,
.faculties-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}
.faculties-table th {
  background-color: #00923f;
  color: white;
  font-weight: bold;
}
.faculties-table tbody tr:nth-child(even) {
  background-color: #f5f5f5;
}
.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
.action-button {
  text-align: center;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
}
.edit-button {
  background-color: #007a33;
  color: white;
}
.delete-button {
  background-color: #d9534f;
  color: white;
}
.action-button:hover {
  opacity: 0.9;
}

/* Modal */
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

/* Formulário no estilo 2 colunas (grid) */
.modal-form-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 900px; /* Deixamos amplo */
  width: 90%;
  box-sizing: border-box;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.modal-form-box h2 {
  margin-bottom: 15px;
  text-align: center;
}

.faculty-form {
  display: grid;
  grid-template-columns: 1fr 1fr; /* 2 colunas */
  gap: 20px;
}
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

/* Sessão de cursos (múltiplos) */
.courses-group {
  grid-column: span 2; /* Este campo pode ocupar as 2 colunas se quiser */
}
.courses-multiple {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.courses-multiple select {
  width: 100%;
}
.courses-multiple button {
  align-self: flex-start;
  background-color: #00923f;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 5px;
  cursor: pointer;
}
.courses-multiple ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
.courses-multiple li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
}
.courses-multiple li button {
  background: none;
  border: none;
  color: #d9534f;
  cursor: pointer;
  font-size: 14px;
}
.courses-multiple li button:hover {
  text-decoration: underline;
}

/* Botões de ação (Salvar/Cancelar) */
.form-buttons {
  display: flex;
  justify-content: center;
  grid-column: span 2; /* Ocupa 2 colunas */
  gap: 15px;
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
.save-button:hover,
.cancel-button:hover {
  opacity: 0.9;
}

/* Responsivo: 1 coluna em telas menores */
@media (max-width: 600px) {
  .faculty-form {
    grid-template-columns: 1fr; /* vira uma só coluna */
  }
  .form-buttons {
    flex-direction: column;
  }
}

/* Rodapé fixo (se necessário) */
.footer-fixed {
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px 0;
  position: sticky; /* ou fixed se preferir */
  bottom: 0;
  width: 100%;
  z-index: 999;
}
</style>
