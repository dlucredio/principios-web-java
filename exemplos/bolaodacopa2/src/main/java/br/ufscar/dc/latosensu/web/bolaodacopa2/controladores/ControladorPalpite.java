package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import br.ufscar.dc.latosensu.web.bolaodacopa2.Bolaodacopa2Config;
import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Palpite;
import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoPalpite;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioPalpite;

@Controller
@RequestMapping(path = "/palpite")
public class ControladorPalpite {

    @Autowired
    Bolaodacopa2Config config;

    @Autowired
    RepositorioPalpite repositorioPalpite;

    @ModelAttribute(name = "selecoes")
    List<String> selecoes() {
        return config.getSelecoes();
    }

    @GetMapping(path = "/novo")
    public String novo(Model model, @SessionAttribute(name = "usuarioLogado") Usuario usuarioLogado) {
        Palpite p = repositorioPalpite.findByPalpiteiroEmail(usuarioLogado.getEmail());
        if (p != null) {
            model.addAttribute("novoPalpite", p);
            model.addAttribute("jaFezPalpiteAntes", true);
        } else {
            model.addAttribute("novoPalpite", new NovoPalpite());
        }
        return "palpiteForm";
    }

    @PostMapping(path = "/gravar")
    public String gravar(Model model, @Valid NovoPalpite form, BindingResult result,
            @SessionAttribute(name = "usuarioLogado") Usuario usuarioLogado) {
        if (result.hasErrors()) {
            return "palpiteForm";
        } else {
            Palpite p = repositorioPalpite.findByPalpiteiroEmail(usuarioLogado.getEmail());
            if (p == null) {
                p = new Palpite();
                p.setPalpiteiro(usuarioLogado);
            }
            p.setCampeao(form.getCampeao());
            p.setVice(form.getVice());

            repositorioPalpite.save(p);

            return "palpiteRegistrado";
        }
    }

    @GetMapping(path = "/ver")
    public String ver(Model model) {
        model.addAttribute("todosPalpites", repositorioPalpite.findAll());
        return "listaPalpites";
    }
}
