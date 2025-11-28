package com.library.library_proyect.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        
        // Rutas públicas que no requieren autenticación
        if (uri.equals("/login") || uri.equals("/registro") || 
            uri.startsWith("/css/") || uri.startsWith("/js/") || 
            uri.startsWith("/img/") || uri.startsWith("/static/")) {
            return true;
        }
        
        // Verificar si el usuario está logueado
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        // Control de acceso por rol
        String tipoUsuario = (String) session.getAttribute("usuarioTipo");
        
        // Rutas exclusivas para ESTUDIANTES
        if (uri.startsWith("/estudiante/")) {
            if (tipoUsuario == null || !tipoUsuario.equals("ESTUDIANTE")) {
                response.sendRedirect("/dashboard");
                return false;
            }
        }
        
        // Rutas exclusivas para BIBLIOTECARIOS
        if (uri.startsWith("/bibliotecario/")) {
            if (tipoUsuario == null || !tipoUsuario.equals("BIBLIOTECARIO")) {
                response.sendRedirect("/dashboard");
                return false;
            }
        }
        
        return true;
    }
}