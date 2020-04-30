package com.adongs.annotation.extend.validation;

import com.adongs.implement.validation.LeastNotNullQuantityValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 多字段关联校验 在选择的字段至少满足指定数量的字段不为空
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Constraint(validatedBy = { LeastNotNullQuantityValidation.class})
public @interface LeastNotNullQuantity {

    /**
     * 字段数组
     * @return 字段数组
     */
    String [] fields();

    /**
     * 至少有值的数量
     * @return 至少有值的数量
     */
    int count() default 1;

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
