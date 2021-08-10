package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { EmailNaoCadastradoValidator.class })
public @interface EmailNaoCadastrado {

    String message() default "Este email já está cadastrado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}