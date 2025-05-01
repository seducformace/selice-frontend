// src/utils/redirectByRole.js

export function getDashboardRouteByRole(role) {
  const normalizedRole = (role || '').toUpperCase();

  switch (normalizedRole) {
    case 'ROLE_ADMIN':
      return '/dashboard';

    case 'ROLE_COORDINATOR':
    case 'ROLE_COORDINATOR_FACULTY':
    case 'ROLE_FACULTY_COORDINATOR': // <- esta é a role real que aparece no token
      return '/dashboard-coordinator-faculty';

    case 'ROLE_COORDINATOR_SCHOOL':
    case 'ROLE_SCHOOL_COORDINATOR':
      return '/dashboard-coordinator-school';

    case 'ROLE_TEACHER':
      return '/dashboard-professor';

    case 'ROLE_STUDENT':
      return '/dashboard-student';

    default:
      return '/login'; // fallback seguro
  }
}
