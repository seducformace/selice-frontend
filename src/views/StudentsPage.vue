<template>
  <div class="students-page">
    <div class="content-wrapper">
      <div class="content">
        <div class="header-actions">
          <button class="home-button" @click="goToDashboard">
            <i class="fas fa-home"></i>
          </button>
          <h1>Gestão de Estudantes</h1>
        </div>

        <div class="actions">
          <button class="add-button" @click="openModal" v-if="canAddStudents">
            {{
              role === 'ROLE_SCHOOL_COORDINATOR'
                ? 'Localizar Aluno'
                : 'Adicionar Estudante'
            }}
          </button>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar estudante..."
            class="search-input"
          />
        </div>

        <div class="table-container">
          <table class="students-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>E-mail</th>
                <th>Curso</th>
                <th>Faculdade</th>
                <th>Escola</th>
                <th>Professor</th>
                <th v-if="role !== 'ROLE_SCHOOL_COORDINATOR'">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(student, index) in filteredStudents" :key="index">
                <td>{{ student.name }}</td>
                <td>{{ student.cpf }}</td>
                <td>{{ student.email }}</td>
                <td>{{ student.course }}</td>
                <td>{{ student.collegeName || '—' }}</td>
                <td>{{ student.schoolName || '—' }}</td>
                <td>{{ student.teacherName || '—' }}</td>
                <td
                  v-if="role !== 'ROLE_SCHOOL_COORDINATOR'"
                  class="actions-buttons"
                >
                  <button class="edit-button" @click="editStudent(index)">
                    Editar
                  </button>
                  <button class="delete-button" @click="deleteStudent(index)">
                    Excluir
                  </button>
                </td>
              </tr>

              <tr v-if="filteredStudents.length === 0">
                <td colspan="8" style="text-align: center; padding: 12px">
                  Nenhum estudante encontrado para sua instituição.
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- MODAL -->
        <div v-if="isModalOpen" class="modal-overlay">
          <div class="modal-form-box">
            <h2>
              {{
                isLinkMode
                  ? 'Localizar Aluno'
                  : isEditing
                  ? 'Editar Estudante'
                  : 'Adicionar Estudante'
              }}
            </h2>

            <!-- LOCALIZAR ALUNO (Coordenador Escolar) -->
            <div v-if="isLinkMode">
              <div class="form-group">
                <input
                  v-model="searchQueryModal"
                  type="text"
                  placeholder="Buscar aluno por nome..."
                />
                <button @click="fetchStudentsPaginated(1)">Buscar</button>
              </div>

              <table class="students-table">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>E-mail</th>
                    <th>Curso</th>
                    <th>Faculdade</th>
                    <th>Escola</th>
                    <th>Professor</th>
                    <th>Ação</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="student in paginatedStudents"
                    :key="student.id"
                    :class="{
                      'selected-row': selectedStudent?.id === student.id,
                    }"
                  >
                    <td>{{ student.name }}</td>
                    <td>{{ student.cpf }}</td>
                    <td>{{ student.email }}</td>
                    <td>{{ student.course }}</td>
                    <td>{{ student.collegeName || '—' }}</td>
                    <td>{{ student.schoolName || '—' }}</td>
                    <td>{{ student.teacherName || '—' }}</td>
                    <td>
                      <button @click="selectStudent(student)">
                        Selecionar
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div class="pagination">
                <button @click="prevPage" :disabled="currentPage === 1">
                  Anterior
                </button>
                <span>Página {{ currentPage }}</span>
                <button @click="nextPage" :disabled="!hasMore">Próxima</button>
              </div>

              <div class="form-buttons">
                <button
                  v-if="selectedStudent"
                  class="save-button"
                  @click="confirmLinkStudent"
                >
                  Confirmar Vinculação
                </button>
                <button type="button" class="cancel-button" @click="closeModal">
                  Fechar
                </button>
              </div>
            </div>

            <!-- FORMULÁRIO DE CADASTRO PADRÃO -->
            <form v-else @submit.prevent="saveStudent" class="student-form">
              <div class="form-group">
                <label for="name">Nome</label>
                <input
                  id="name"
                  v-model="currentStudent.name"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="cpf">CPF</label>
                <input
                  id="cpf"
                  v-model="currentStudent.cpf"
                  type="text"
                  required
                />
              </div>

              <div class="form-group">
                <label for="email">E-mail</label>
                <input
                  id="email"
                  v-model="currentStudent.email"
                  type="email"
                  required
                />
              </div>

              <div class="form-group">
                <label for="college">Faculdade</label>
                <select
                  id="college"
                  v-model="currentStudent.college"
                  @change="loadCourses"
                  required
                >
                  <option disabled value="">Selecione</option>
                  <option
                    v-for="college in colleges"
                    :key="college.id"
                    :value="{ id: college.id }"
                  >
                    {{ college.name }}
                  </option>
                </select>
              </div>

              <div class="form-group">
                <label for="course">Curso</label>
                <select id="course" v-model="currentStudent.course" required>
                  <option disabled value="">Selecione</option>
                  <option
                    v-for="course in availableCourses"
                    :key="course"
                    :value="course"
                  >
                    {{ course }}
                  </option>
                </select>
              </div>

              <div class="form-group">
                <label for="school">Escola</label>
                <select id="school" v-model="currentStudent.school">
                  <option disabled value="">Selecione</option>
                  <option
                    v-for="school in schools"
                    :key="school.id"
                    :value="{ id: school.id }"
                  >
                    {{ school.name }}
                  </option>
                </select>
              </div>

              <div class="form-group">
                <label for="teacher">Professor</label>
                <select id="teacher" v-model="currentStudent.teacher">
                  <option disabled value="">Selecione</option>
                  <option
                    v-for="teacher in teachers"
                    :key="teacher.id"
                    :value="{ id: teacher.id }"
                  >
                    {{ teacher.name }}
                  </option>
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
  name: 'StudentsPage',
  components: { Footer },
  data() {
    return {
      searchQuery: '',
      isModalOpen: false,
      isEditing: false,
      isLinkMode: false,
      currentStudent: {},
      students: [],
      role: '',
      coordinatorSchoolId: null,
      colleges: [],
      schools: [],
      teachers: [],
      availableCourses: [],
      paginatedStudents: [],
      searchQueryModal: '',
      currentPage: 1,
      hasMore: false,
      selectedStudent: null,
    };
  },
  computed: {
    filteredStudents() {
      return this.students.filter((s) =>
        s.name?.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    canAddStudents() {
      return [
        'ROLE_ADMIN',
        'ADMIN',
        'ROLE_COORDINATOR_FACULTY',
        'COORDINATOR_FACULTY',
        'ROLE_COORDINATOR_SCHOOL',
        'COORDINATOR_SCHOOL',
        'ROLE_FACULTY_COORDINATOR',
        'ROLE_SCHOOL_COORDINATOR',
      ].includes(this.role);
    },
  },
  methods: {
    goToDashboard() {
      const token = localStorage.getItem('token');
      const role = this.parseJwt(token)?.role;
      switch (role) {
        case 'ROLE_ADMIN':
          this.$router.push('/dashboard');
          break;
        case 'ROLE_COORDINATOR_FACULTY':
          this.$router.push('/dashboard-coordinator-faculty');
          break;
        case 'ROLE_COORDINATOR_SCHOOL':
        case 'COORDINATOR_SCHOOL':
        case 'ROLE_SCHOOL_COORDINATOR':
          this.$router.push('/dashboard-coordinator-school');
          break;
        case 'ROLE_TEACHER':
          this.$router.push('/dashboard-professor');
          break;
        case 'ROLE_STUDENT':
          this.$router.push('/dashboard-student');
          break;
        default:
          this.$router.push('/login');
      }
    },
    openModal() {
      this.isModalOpen = true;
      if (this.role === 'ROLE_SCHOOL_COORDINATOR') {
        this.isLinkMode = true;
        this.selectedStudent = null;
        this.fetchStudentsPaginated(1);
      } else {
        this.isLinkMode = false;
        this.isEditing = false;
        this.currentStudent = {};
        this.availableCourses = [];
      }
    },
    closeModal() {
      this.isModalOpen = false;
      this.selectedStudent = null;
    },
    async loadCourses() {
      this.availableCourses = [];
      if (!this.currentStudent.college?.id) return;
      try {
        const response = await api.get(
          `/faculties/${this.currentStudent.college.id}/courses`
        );
        this.availableCourses = response.data.map((c) => ({
          id: c.id,
          name: c.name,
        }));
      } catch (error) {
        console.error('Erro ao carregar cursos da faculdade:', error);
      }
    },
    editStudent(index) {
      this.isEditing = true;
      const selected = { ...this.students[index] };
      this.currentStudent = {
        id: selected.id,
        name: selected.name,
        cpf: selected.cpf,
        email: selected.email,
        course: selected.courseId ? { id: selected.courseId } : null,
        college: selected.collegeId ? { id: selected.collegeId } : null,
        school: selected.schoolId ? { id: selected.schoolId } : null,
        teacher: selected.teacherId ? { id: selected.teacherId } : null,
      };
      this.isModalOpen = true;
      this.isLinkMode = false;
      if (this.currentStudent.college?.id) {
        this.loadCourses();
      }
    },
    async saveStudent() {
      if (this.role === 'ROLE_SCHOOL_COORDINATOR') {
        alert('Coordenadores escolares não podem cadastrar estudantes.');
        return;
      }

      const studentToSave = {
        ...this.currentStudent,
        course: this.currentStudent.course?.id
          ? { id: this.currentStudent.course.id }
          : null,
        college: this.currentStudent.college?.id
          ? { id: this.currentStudent.college.id }
          : null,
        school: this.currentStudent.school?.id
          ? { id: this.currentStudent.school.id }
          : null,
        teacher: this.currentStudent.teacher?.id
          ? { id: this.currentStudent.teacher.id }
          : null,
      };

      try {
        if (this.isEditing && this.currentStudent.id) {
          await api.put(`/students/${this.currentStudent.id}`, studentToSave);
        } else {
          await api.post('/students', studentToSave);
        }

        this.closeModal();
        await this.fetchStudents();
      } catch (error) {
        console.error('Erro ao salvar estudante:', error);
        alert(
          'Não foi possível salvar o estudante. Verifique os dados preenchidos.'
        );
      }
    },

    async deleteStudent(index) {
      const student = this.students[index];
      if (confirm(`Deseja excluir ${student.name}?`)) {
        await api.delete(`/students/${student.id}`);
        await this.fetchStudents();
      }
    },
    parseJwt(token) {
      if (!token) return {};
      try {
        const decoded = JSON.parse(atob(token.split('.')[1]));
        let role = decoded.role;
        if (!role && Array.isArray(decoded.authorities)) {
          const auth = decoded.authorities[0];
          role = typeof auth === 'object' ? auth.authority : auth;
        }
        if (role && !role.startsWith('ROLE_')) {
          role = `ROLE_${role}`;
        }
        return { ...decoded, role };
      } catch (e) {
        console.error('Erro ao decodificar JWT:', e);
        return {};
      }
    },
    async fetchStudents() {
      try {
        const response = await api.get('/students', {
          params: { paged: true, page: 0, size: 20 },
        });
        this.students = response.data.content || [];
      } catch (error) {
        console.error('Erro ao carregar estudantes:', error);
        alert('Erro ao carregar dados dos estudantes.');
      }
    },

    async fetchStudentsPaginated(page = 1) {
      try {
        const response = await api.get('/students', {
          params: {
            page,
            size: 20,
            name: this.searchQueryModal,
          },
        });
        this.paginatedStudents = response.data.content || [];
        this.currentPage = page;
        this.hasMore = !response.data.last;
      } catch (error) {
        console.error('Erro ao buscar estudantes paginados:', error);
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.fetchStudentsPaginated(this.currentPage - 1);
      }
    },
    nextPage() {
      if (this.hasMore) {
        this.fetchStudentsPaginated(this.currentPage + 1);
      }
    },
    selectStudent(student) {
      this.selectedStudent = student;
    },
    async confirmLinkStudent() {
      if (!this.selectedStudent) {
        alert('Selecione um aluno para vincular.');
        return;
      }
      if (!this.coordinatorSchoolId) {
        alert('ID da escola não identificado.');
        return;
      }
      try {
        await api.put(`/students/${this.selectedStudent.id}/assign-school`, {
          schoolId: this.coordinatorSchoolId,
        });
        alert(`Aluno ${this.selectedStudent.name} vinculado com sucesso.`);
        this.closeModal();
        this.fetchStudents();
      } catch (error) {
        console.error('Erro ao vincular aluno à escola:', error);
        alert('Falha ao vincular estudante.');
      }
    },
    async fetchCoordinatorData() {
      if (
        this.role === 'ROLE_COORDINATOR_SCHOOL' ||
        this.role === 'COORDINATOR_SCHOOL' ||
        this.role === 'ROLE_SCHOOL_COORDINATOR'
      ) {
        try {
          const res = await api.get('/coordinators/me');
          this.coordinatorSchoolId = res.data.school?.id;
        } catch (e) {
          console.error('Erro ao buscar escola do coordenador.', e);
        }
      }
    },
    async fetchSelectData() {
      try {
        const [facultiesRes, schoolsRes, teachersRes] = await Promise.all([
          api.get('/faculties'),
          api.get('/schools'),
          api.get('/teachers'),
        ]);
        this.colleges = facultiesRes.data;
        this.schools = schoolsRes.data;
        this.teachers = teachersRes.data;
      } catch (error) {
        console.error('Erro ao carregar dados auxiliares:', error);
      }
    },
  },
  created() {
    const token = localStorage.getItem('token');
    const decoded = this.parseJwt(token);
    this.role = decoded.role || '';

    this.fetchSelectData();

    this.fetchCoordinatorData().then(() => {
      this.fetchStudents(); // Só chama após obter o ID da escola (se for o caso)
    });
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

/* Cabeçalho e Ações */
.header-actions,
.actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.home-button {
  background-color: #00923f;
  color: white;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
}

.add-button {
  background-color: #00923f;
  color: white;
  padding: 8px 12px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
  font-weight: bold;
}

.search-input {
  width: 250px;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* ===== TABELA DE ESTUDANTES ===== */
.table-container {
  margin: 20px 0;
  overflow-x: auto;
}

.students-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.students-table th,
.students-table td {
  border: 1px solid #ccc;
  padding: 12px 16px;
  vertical-align: middle;
  height: 60px;
  line-height: 1.2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.students-table th {
  background-color: #00923f;
  color: white;
  text-align: center;
}

.actions-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.edit-button {
  background-color: #ccc;
  border: none;
  padding: 6px 12px;
  cursor: pointer;
  border-radius: 4px;
}

.delete-button {
  background-color: #ff6565;
  color: white;
  border: none;
  padding: 6px 12px;
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
  z-index: 999;
  padding: 20px;
  overflow-y: auto;
}

.modal-form-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 900px;
  width: 100%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

/* ===== FORMULÁRIO DO MODAL ===== */
.student-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 10px;
}

.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-weight: bold;
  margin-bottom: 5px;
}
.form-group input,
.form-group select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Botões do formulário */
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

/* Responsivo */
@media (max-width: 768px) {
  .student-form {
    grid-template-columns: 1fr;
  }
  .form-buttons {
    flex-direction: column;
    gap: 10px;
  }
}

/* ===== RODAPÉ FIXO ===== */
.footer-fixed {
  width: 100%;
  background-color: #00923f;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 998;
}
/* Evita que selects com textos longos quebrem o layout */
.form-group select {
  max-width: 100%;
  word-wrap: break-word;
  white-space: normal;
}

/* Garante que a linha de formulário não ultrapasse o modal */
.student-form {
  width: 100%;
  box-sizing: border-box;
}

/* Evita campos exageradamente largos */
.student-form .form-group input,
.student-form .form-group select {
  width: 100%;
  box-sizing: border-box;
}
</style>
