package com.adongs.implement.validation;

import com.adongs.annotation.extend.validation.Group;
import com.google.common.collect.Sets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * 分组验证
 * @author yudong
 * @version 1.0
 */
public class GroupValidation implements ConstraintValidator<Group, Object> {

    private Set<String> groups;

    private boolean isNull;

    @Override
    public void initialize(Group constraintAnnotation) {
        groups = Sets.newHashSet(constraintAnnotation.values());
        isNull = constraintAnnotation.isNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value==null){
            return isNull;
        }
        return groups.contains(value.toString());
    }
}
