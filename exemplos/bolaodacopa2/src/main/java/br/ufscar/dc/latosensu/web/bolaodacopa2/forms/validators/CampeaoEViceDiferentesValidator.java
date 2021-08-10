package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoPalpite;

public class CampeaoEViceDiferentesValidator implements ConstraintValidator<CampeaoEViceDiferentes, NovoPalpite> {
    public boolean isValid(NovoPalpite novoPalpite, ConstraintValidatorContext context) {
        return !novoPalpite.getCampeao().equals(novoPalpite.getVice());
    }
}