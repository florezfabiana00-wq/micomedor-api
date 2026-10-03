package com.micomedor.servlet;

import com.micomedor.dao.BeneficiarioDAO;
import com.micomedor.modelo.Beneficiario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/beneficiario")
public class BeneficiarioServlet extends HttpServlet {

    private BeneficiarioDAO dao = new BeneficiarioDAO();

    // CONSULTAR BENEFICIARIOS
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Beneficiario> lista = dao.listarTodos();

        PrintWriter out = response.getWriter();

        out.print("[");

        for (int i = 0; i < lista.size(); i++) {

            Beneficiario b = lista.get(i);

            out.print("{");
            out.print("\"id\":" + b.getId() + ",");
            out.print("\"nombre\":\"" + b.getNombre() + "\",");
            out.print("\"documento\":\"" + b.getDocumento() + "\",");
            out.print("\"telefono\":\"" + b.getTelefono() + "\",");
            out.print("\"edad\":" + b.getEdad());
            out.print("}");

            if (i < lista.size() - 1) {
                out.print(",");
            }
        }

        out.print("]");

        response.setStatus(HttpServletResponse.SC_OK);
    }

    // REGISTRAR BENEFICIARIO
    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
                           throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String documento = request.getParameter("documento");
        String telefono = request.getParameter("telefono");
        String edadTexto = request.getParameter("edad");

        if (nombre == null || nombre.trim().isEmpty()
                || documento == null || documento.trim().isEmpty()
                || edadTexto == null || edadTexto.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().print(
                "{\"mensaje\":\"Los campos obligatorios están vacíos\"}"
            );

            return;
        }

        try {

            int edad = Integer.parseInt(edadTexto);

            Beneficiario beneficiario = new Beneficiario(
                nombre,
                documento,
                telefono,
                edad
            );

            boolean guardado = dao.guardar(beneficiario);

            if (guardado) {

                response.setStatus(HttpServletResponse.SC_CREATED);

                response.getWriter().print(
                    "{\"mensaje\":\"Beneficiario registrado correctamente\"}"
                );

            } else {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                response.getWriter().print(
                    "{\"mensaje\":\"No se pudo registrar el beneficiario\"}"
                );
            }

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().print(
                "{\"mensaje\":\"La edad debe ser un número\"}"
            );
        }
    }

    // ACTUALIZAR
    @Override
    protected void doPut(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));
            int edad = Integer.parseInt(request.getParameter("edad"));

            Beneficiario b = new Beneficiario();

            b.setId(id);
            b.setNombre(request.getParameter("nombre"));
            b.setDocumento(request.getParameter("documento"));
            b.setTelefono(request.getParameter("telefono"));
            b.setEdad(edad);

            boolean actualizado = dao.actualizar(b);

            if (actualizado) {

                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().print(
                    "{\"mensaje\":\"Beneficiario actualizado correctamente\"}"
                );

            } else {

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                response.getWriter().print(
                    "{\"mensaje\":\"Beneficiario no encontrado\"}"
                );
            }

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().print(
                "{\"mensaje\":\"Datos inválidos\"}"
            );
        }
    }

    // ELIMINAR
    @Override
    protected void doDelete(HttpServletRequest request,
                             HttpServletResponse response)
                             throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));

            boolean eliminado = dao.eliminar(id);

            if (eliminado) {

                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().print(
                    "{\"mensaje\":\"Beneficiario eliminado correctamente\"}"
                );

            } else {

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                response.getWriter().print(
                    "{\"mensaje\":\"Beneficiario no encontrado\"}"
                );
            }

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().print(
                "{\"mensaje\":\"ID inválido\"}"
            );
        }
    }
}
