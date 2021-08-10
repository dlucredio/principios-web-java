package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoUsuario;

public class SenhaConfirmadaValidator implements ConstraintValidator<SenhaConfirmada, NovoUsuario> {
    public boolean isValid(NovoUsuario nu, ConstraintValidatorContext context) {
        boolean valido=nu.getSenha().equals(nu.getConfirmarSenha());
        if(!valido) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode( "confirmarSenha" ).addConstraintViolation();
        }
        return valido;   
    }
}
