package com.sesac7.hellopet.domain.application.validation;

import com.sesac7.hellopet.domain.application.dto.request.FuturePlanInfoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FuturePlanInfoValidator implements
        ConstraintValidator<ValidFuturePlan, FuturePlanInfoRequest> {

    @Override
    public boolean isValid(FuturePlanInfoRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isHasFuturePlan()) {
            return value.getPlanDetails() != null && !value.getPlanDetails().trim().isEmpty();
        }

        return true;
    }
}
