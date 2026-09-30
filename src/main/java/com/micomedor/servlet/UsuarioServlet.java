package com.micomedor.servlet;

import com.micomedor.dao.UsuarioDAO;
import com.micomedor.modelo.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
// Tomcat 10 o superior: jakarta.servlet.*
// Tomcat 9 o inferior: cambia "jakarta" por "javax"

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Servicio web para registro e inicio de sesión
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String accion = req.getParameter("accion");
        String usuario = req.getParameter("usuario");
        String contrasena = req.getParameter("contrasena");
        PrintWriter out = resp.getWriter();

        if ("registro".equals(accion)) {
            boolean ok = dao.registrar(new Usuario(usuario, contrasena));
            resp.setStatus(ok ? 201 : 400);
            out.print("{\"mensaje\":\"" + (ok ? "Registro exitoso" : "Error en el registro") + "\"}");
        } else if ("login".equals(accion)) {
            boolean ok = dao.autenticar(usuario, contrasena);
            resp.setStatus(ok ? 200 : 401);
            out.print("{\"mensaje\":\"" + (ok ? "Autenticación satisfactoria" : "Error en la autenticación") + "\"}");
        } else {
            resp.setStatus(400);
            out.print("{\"mensaje\":\"Acción no válida\"}");
        }
    }
}

