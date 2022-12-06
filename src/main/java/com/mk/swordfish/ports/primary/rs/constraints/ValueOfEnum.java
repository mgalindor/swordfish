package com.mk.swordfish.ports.primary.rs.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {

  Class<? extends Enum<?>> value();

  String message() default "{javax.validation.constraints.ValueOfEnum.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
