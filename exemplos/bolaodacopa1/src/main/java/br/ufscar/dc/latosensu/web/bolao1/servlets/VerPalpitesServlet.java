package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.dao.PalpiteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/VerPalpitesServlet" })
public class VerPalpitesServlet extends HttpServlet {
    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/bolao1local");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PalpiteDAO pdao = new PalpiteDAO(dataSource);
        String selecao = req.getParameter("selecao");
        List<Palpite> todosPalpites = null;
        try {
            if (selecao == null) {
                todosPalpites = pdao.listarTodosPalpites();
            } else {
                todosPalpites = pdao.listarTodosPalpitesPorSelecao(selecao);
            }
            req.setAttribute("listaPalpites", todosPalpites);
            req.getRequestDispatcher("listaPalpites.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
