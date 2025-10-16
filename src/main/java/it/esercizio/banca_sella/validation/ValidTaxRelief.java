package it.esercizio.banca_sella.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidTaxReliefValidator.class})
public @interface ValidTaxRelief {
    String message() default "taxRelief: naturalPersonBeneficiary and legalPersonBeneficiary must comply with beneficiaryType";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}