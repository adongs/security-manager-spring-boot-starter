package com.adongs.implement.validation;

import com.adongs.annotation.extend.validation.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证手机号
 * @author yudong
 * @version 1.0
 */
public class MobileValidation implements ConstraintValidator<Mobile, Object> {

    private boolean isNull;

    private final static Pattern PATTERN = Pattern.compile("1[3456789]\\d{9}");


    @Override
    public void initialize(Mobile constraintAnnotation) {
          isNull = constraintAnnotation.isNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value==null){
            return isNull;
        }
        Matcher matcher = PATTERN.matcher(value.toString());
        return matcher.matches();
    }
}
