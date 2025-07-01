package com.sesac7.hellopet.domain.application.validation;

import com.sesac7.hellopet.domain.application.dto.request.PetExperienceInfoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PetExperienceInfoValidator implements
        ConstraintValidator<ValidPetExperience, PetExperienceInfoRequest> {

    @Override
    public boolean isValid(PetExperienceInfoRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isHasPetExperience()) {
            return value.getExperienceDetails() != null && !value.getExperienceDetails().trim().isEmpty();
        }

        return true;
    }
}
