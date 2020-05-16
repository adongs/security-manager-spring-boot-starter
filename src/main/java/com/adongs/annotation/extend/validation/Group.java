package com.adongs.annotation.extend.validation;

import com.adongs.implement.validation.GroupValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证的值必须在指定组中
 * @author yudong
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Constraint(validatedBy = { GroupValidation.class})
public @interface Group {
    /**
     * 值数组
     * @return 值数组
     */
    String [] values();

    /**
     * 枚举类支付
     * @return
     */
    Class<? extends Enum> group() default Enum.class;
    /**
     * 是否为空
     * @return 是否为空
     */
    boolean isNull() default false;

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
