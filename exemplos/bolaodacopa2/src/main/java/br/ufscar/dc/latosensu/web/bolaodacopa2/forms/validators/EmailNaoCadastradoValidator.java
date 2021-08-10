package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioUsuario;

public class EmailNaoCadastradoValidator implements ConstraintValidator<EmailNaoCadastrado, String> {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    public boolean isValid(String email, ConstraintValidatorContext context) {
        Usuario u = repositorioUsuario.findByEmail(email);
        if (u == null) {
            return true;
        } else {
            return false;
        }
    }
}