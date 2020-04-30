package com.adongs.annotation.extend.validation;

import com.adongs.implement.validation.EitherOrValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 2个字段关联校验 二选一
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Constraint(validatedBy = { EitherOrValidation.class})
public @interface EitherOr {

    /**
     * 字段数组
     * @return 字段数组
     */
    String [] fields();

    String message() default "";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
