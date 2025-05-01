// src/utils/authUtils.js
export function redirectToDashboard(router) {
  const token = localStorage.getItem('token');
  if (!token) {
    router.push('/login');
    return;
  }

  try {
    const role = JSON.parse(atob(token.split('.')[1]))?.role;

    switch (role) {
      case 'ROLE_ADMIN':
        router.push('/dashboard');
        break;
      case 'ROLE_COORDINATOR':
      case 'ROLE_SCHOOL_COORDINATOR':
        router.push('/dashboard-coordinator');
        break;
      case 'ROLE_TEACHER':
        router.push('/dashboard-professor');
        break;
      case 'ROLE_STUDENT':
        router.push('/dashboard-student');
        break;
      default:
        router.push('/login');
    }
  } catch (error) {
    console.error('Erro ao decodificar token:', error);
    router.push('/login');
  }
}
