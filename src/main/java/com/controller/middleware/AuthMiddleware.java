package com.controller.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthMiddleware implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Simula a lógica de autenticação e verificação de papel
        String userRole = (String) request.getSession().getAttribute("userRole");

        if (userRole == null) {
            response.sendRedirect("/login");
            return false;
        }

        switch (userRole) {
            case "student":
                response.sendRedirect("/student/dashboard");
                break;
            case "teacher":
                response.sendRedirect("/teacher/dashboard");
                break;
            case "coordinator":
                response.sendRedirect("/coordinator/dashboard");
                break;
            case "admin":
                // Administrador pode acessar qualquer área
                return true;
            default:
                response.sendRedirect("/error");
                return false;
        }
        return false;
    }
}
