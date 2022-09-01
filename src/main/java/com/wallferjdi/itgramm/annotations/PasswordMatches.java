package com.wallferjdi.itgramm.annotations;

import com.wallferjdi.itgramm.validation.EmailValidator;
import com.wallferjdi.itgramm.validation.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class )
public @interface PasswordMatches {
    String message() default "Password do not matches";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
