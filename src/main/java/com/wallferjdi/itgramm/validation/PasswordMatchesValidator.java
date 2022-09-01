package com.wallferjdi.itgramm.validation;

import com.wallferjdi.itgramm.annotations.PasswordMatches;
import com.wallferjdi.itgramm.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches,Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) o;
        return signupRequest.getPassword().equals(signupRequest.getConfirmPassword());
    }
}
