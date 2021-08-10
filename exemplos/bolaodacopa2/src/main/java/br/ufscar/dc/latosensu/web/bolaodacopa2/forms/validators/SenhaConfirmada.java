package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SenhaConfirmadaValidator.class })
public @interface SenhaConfirmada {

    String message() default "A confirmação da senha não corresponde à senha";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}