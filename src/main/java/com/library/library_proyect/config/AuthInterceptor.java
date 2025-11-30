package com.library.library_proyect.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        if (uri.equals("/login") || uri.equals("/registro") ||
                uri.startsWith("/css/") || uri.startsWith("/js/") ||
                uri.startsWith("/img/") || uri.startsWith("/static/")) {
            return true;
        }

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("/login");
            return false;
        }

        String tipoUsuario = (String) session.getAttribute("usuarioTipo");

        // Rutas para ESTUDIANTES
        if (uri.startsWith("/estudiante/")) {
            if (tipoUsuario == null || !tipoUsuario.equals("ESTUDIANTE")) {
                response.sendRedirect("/dashboard");
                return false;
            }
        }

        // Rutas para BIBLIOTECARIOS
        if (uri.startsWith("/bibliotecario/")) {
            if (tipoUsuario == null || !tipoUsuario.equals("BIBLIOTECARIO")) {
                response.sendRedirect("/dashboard");
                return false;
            }
        }

        return true;
    }
}