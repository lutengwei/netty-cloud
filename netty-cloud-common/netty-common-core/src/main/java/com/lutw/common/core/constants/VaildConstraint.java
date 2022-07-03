package com.lutw.common.core.constants;

import com.lutw.common.core.annotation.VaildList;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

@Component
public class VaildConstraint implements ConstraintValidator<VaildList,Integer> {

    public Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(VaildList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        int[] value = constraintAnnotation.value();
        for (int i : value) {
            set.add(i);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (set.contains(value.intValue())) {
            return true;
        } else {
            return false;
        }
    }
}
