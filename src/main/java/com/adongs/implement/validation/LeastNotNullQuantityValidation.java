package com.adongs.implement.validation;

import com.adongs.annotation.extend.validation.LeastNotNullQuantity;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 多字段关联校验 在选择的字段至少满足指定数量的字段不为空
 * @author yudong
 * @version 1.0
 */
public class LeastNotNullQuantityValidation implements ConstraintValidator<LeastNotNullQuantity, Object> {

    /**
     * 最低有值数量
     */
    private int count;
    /**
     * 字段名称
     */
    private String [] fields;

    private final static PropertyUtilsBean PROPERTY_UTILS = BeanUtilsBean.getInstance().getPropertyUtils();

    @Override
    public void initialize(LeastNotNullQuantity constraintAnnotation) {
            this.count = constraintAnnotation.count();
            this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (fields.length==0){
            return true;
        }
        int thisCount = 0;
        try {
            for (int i = 0, l = fields.length; i < l; i++) {
                String field = fields[i];
                Object object = PROPERTY_UTILS.getNestedProperty(value, field);
                if (object!=null){
                    thisCount++;
                }
            }
        }catch (Exception e){}
        return thisCount>=count;
    }
}
