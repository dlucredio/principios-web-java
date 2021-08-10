package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.DadosLogin;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoUsuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioUsuario;

@Controller
@SessionAttributes("usuarioLogado")
@RequestMapping(path = "/usuario")
public class ControladorUsuario {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    @GetMapping(path = "/novo")
    public String cadastrar(Model model) {
        model.addAttribute("novoUsuario", new NovoUsuario());
        return "cadastrar";
    }

    @PostMapping(path = "/gravar")
    public String gravar(Model model, @Valid NovoUsuario form, BindingResult result) throws ParseException {
        if (result.hasErrors()) {
            return "cadastrar";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataDeNascimento = sdf.parse(form.getDataDeNascimento());
            Usuario u = new Usuario(form.getNome(), form.getEmail(), form.getSenha(), form.getTelefone(),
                    dataDeNascimento);
            repositorioUsuario.save(u);
            model.addAttribute("mensagem", "Usuário registrado com sucesso!");
            model.addAttribute("tipoMensagem", "sucesso");
            model.addAttribute("dadosLogin", new DadosLogin());
            return "login";
        }
    }

    @PostMapping(path = "/login")
    public String login(Model model, DadosLogin dadosLogin) {
        Usuario u = repositorioUsuario.findByEmail(dadosLogin.getEmail());
        if (u != null && u.getSenha().equals(dadosLogin.getSenha())) {
            model.addAttribute("usuarioLogado", u);
            return "principal";
        } else {
            model.addAttribute("mensagem", "Dados de login errados!");
            model.addAttribute("tipoMensagem", "erro");
            return "login";
        }
    }
}
