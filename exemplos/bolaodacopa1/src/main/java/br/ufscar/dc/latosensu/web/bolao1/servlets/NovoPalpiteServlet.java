package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import br.ufscar.dc.latosensu.web.bolao1.forms.NovoPalpiteFormBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/NovoPalpiteServlet" })
public class NovoPalpiteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        NovoPalpiteFormBean npfb = new NovoPalpiteFormBean();
        try {
            // Obs: BeanUtils é uma classe da biblioteca
            // Apache Commons BeanUtils
            // http://commons.apache.org/beanutils/
            BeanUtils.populate(npfb, req.getParameterMap());
            req.getSession().setAttribute("novoPalpite", npfb);
            List<String> mensagens = npfb.validar();
            if (mensagens == null) {
                req.getRequestDispatcher("confirmarPalpite.jsp").forward(req, resp);
            } else {
                req.setAttribute("mensagens", mensagens);
                req.getRequestDispatcher("palpiteForm.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
