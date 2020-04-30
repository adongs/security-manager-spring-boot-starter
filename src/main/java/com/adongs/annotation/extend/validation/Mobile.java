package com.adongs.annotation.extend.validation;

import com.adongs.implement.validation.MobileValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@Constraint(validatedBy = { MobileValidation.class})
public @interface Mobile {
    /**
     * 是否为空
     * @return 返回是否允许为空
     */
    boolean isNull() default false;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}
