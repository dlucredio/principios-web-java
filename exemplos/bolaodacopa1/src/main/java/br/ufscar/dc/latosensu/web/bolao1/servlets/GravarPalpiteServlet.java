package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;
import br.ufscar.dc.latosensu.web.bolao1.dao.PalpiteDAO;
import br.ufscar.dc.latosensu.web.bolao1.dao.UsuarioDAO;
import br.ufscar.dc.latosensu.web.bolao1.forms.NovoPalpiteFormBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/GravarPalpiteServlet" })
public class GravarPalpiteServlet extends HttpServlet {
    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/bolao1local"); // Irá recuperar conforme context.xml
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NovoPalpiteFormBean npfb = (NovoPalpiteFormBean) req.getSession().getAttribute("novoPalpite");
        req.getSession().removeAttribute("novoPalpite");

        UsuarioDAO udao = new UsuarioDAO(dataSource);
        PalpiteDAO pdao = new PalpiteDAO(dataSource);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNascimento = null;
        try {
            dataNascimento = sdf.parse(npfb.getDataDeNascimento());
        } catch (ParseException e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
        try {
            Usuario u = new Usuario();
            u.setNome(npfb.getNome());
            u.setEmail(npfb.getEmail());
            u.setTelefone(npfb.getTelefone());
            u.setDataDeNascimento(dataNascimento);
            u = udao.gravarUsuario(u);
            Palpite p = new Palpite();
            p.setCampeao(npfb.getCampeao());
            p.setVice(npfb.getVice());
            p.setPalpiteiro(u);
            p = pdao.gravarPalpite(p);
            req.setAttribute("mensagem", "Obrigado pelo palpite!");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
