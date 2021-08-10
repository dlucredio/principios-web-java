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
@Constraint(validatedBy = {FormatoDataValidator.class})
public @interface FormatoData {

    String message() default "Data deve seguir formato dd/mm/aaaa";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}