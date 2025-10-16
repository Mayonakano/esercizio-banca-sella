package it.esercizio.banca_sella.validation;

import it.esercizio.banca_sella.dto.TaxRelief;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTaxReliefValidator implements ConstraintValidator<ValidTaxRelief, TaxRelief> {
    @Override
    public boolean isValid(TaxRelief value, ConstraintValidatorContext context) {
        if (value == null) return true; // handled elsewhere if required
        String type = value.getBeneficiaryType();
        boolean hasNatural = value.getNaturalPersonBeneficiary() != null;
        boolean hasLegal = value.getLegalPersonBeneficiary() != null;

        if (type == null) return true; // let @NotNull on field handle it

        boolean ok;
        if ("NATURAL_PERSON".equalsIgnoreCase(type)) {
            ok = hasNatural && !hasLegal;
            if (!ok) {
                buildViolation(context, "For beneficiaryType NATURAL_PERSON, naturalPersonBeneficiary is required and legalPersonBeneficiary must be null");
            }
            return ok;
        } else if ("LEGAL_PERSON".equalsIgnoreCase(type)) {
            ok = hasLegal && !hasNatural;
            if (!ok) {
                buildViolation(context, "For beneficiaryType LEGAL_PERSON, legalPersonBeneficiary is required and naturalPersonBeneficiary must be null");
            }
            return ok;
        }
        // Unknown type: do not fail here; schema validator may handle
        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}