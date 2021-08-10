package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.DadosLogin;

@Controller
public class ControladorPrincipal {

    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("dadosLogin", new DadosLogin());
        return "login";
    }

    @GetMapping(path = "/principal")
    public String principal() {
        return "principal";
    }

    @GetMapping(path = "/sair")
    public String sair(Model model, SessionStatus status) {
        model.addAttribute("dadosLogin", new DadosLogin());
        status.setComplete();
        return "login";
    }
}
