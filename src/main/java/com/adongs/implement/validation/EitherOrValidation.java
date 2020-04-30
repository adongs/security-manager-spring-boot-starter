package com.adongs.implement.validation;

import com.adongs.annotation.extend.validation.EitherOr;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 2个字段关联校验 二选一
 * @author yudong
 * @version 1.0
 */
public class EitherOrValidation implements ConstraintValidator<EitherOr, Object> {

    private String [] fields;
    private final static  PropertyUtilsBean PROPERTY_UTILS = BeanUtilsBean.getInstance().getPropertyUtils();


    @Override
    public void initialize(EitherOr constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (fields.length!=2){
            return false;
        }
        try {
            Object fields1 = PROPERTY_UTILS.getNestedProperty(value, fields[0]);
            Object fields2 = PROPERTY_UTILS.getNestedProperty(value, fields[1]);
           return !((fields1==null && fields2==null) || (fields1!=null && fields2!=null));
        }catch (Exception e){

        }
        return false;
    }
}
