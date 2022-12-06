package com.mk.swordfish.ports.primary.rs.constraints;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

  private List<String> values;
  private String availableValues;

  @Override
  public void initialize(ValueOfEnum annotation) {
    values = Stream.of(annotation.value().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
    availableValues = values.stream().collect(Collectors.joining(","));
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    HibernateConstraintValidatorContext hibernateContext = context.unwrap(
        HibernateConstraintValidatorContext.class
    );

    hibernateContext.addExpressionVariable("availableValues", availableValues);
    return values.contains(value.toString());
  }
}
