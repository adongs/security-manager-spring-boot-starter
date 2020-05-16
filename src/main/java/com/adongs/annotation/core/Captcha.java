package com.adongs.annotation.core;

import com.adongs.constant.CodeType;
import com.adongs.constant.FailureCondition;

import java.lang.annotation.*;

/**
 * 验证码,负责登录或部分接口访问异常提供图片验证码进行验证
 * @author yudong
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Captcha {
    /**
     * 图片验证码请求路径
     * 为空则使用配置文件的路径
     * @return
     */
    String value() default "";

    /**
     * 验证码的位置,支持EL表达式,
     * 默认将获取header中的captcha如果不存在将获取Param中的captcha
     * 如果不填写将 默认尝试 (code captcha loginCode loginCaptcha userCode userCaptcha)
     * @return
     */
    String code() default "";

    /**
     * 登录用户名,支持El表达式
     * 默认尝试检查接口参数,将尝试检查名称为(
     *      name user email mobile phone login
     *      loginName userName  userEmail loginEmail
     *      userMobile loginMobile  userPhone  loginPhone
     *      id uid userid loginId
     * )
     */
    String loginName() default "";

    /**
     * 验证码  宽X高
     * 不填写将使用配置文件
     * @return
     */
    String widthHeight() default "";

    /**
     * 验证码位数,不填写默认使用配置文件
     * @return
     */
    String digits() default "";

    /**
     * 验证码字符类型
     * -1 使用配置文件
     * 1 数字和字母混合
     * 2 纯数字
     * 3 纯字母
     * 4 纯大写字母
     * 5 纯小写字母
     * 6 数字和大写字母
     * @return
     */
    int characterType() default -1;

    /**
     * 验证码字体
     * @return
     */
    int font() default -1;
    /**
     * 验证码类型
     * @return
     */
    CodeType codeType() default CodeType.CONFIG;

    /**
     * 判定失败的条件
     * @return
     */
    FailureCondition[] condition() default {FailureCondition.CONFIG};

    /**
     * 条件判定表达式
     * @return
     */
    String expression() default "";

    /**
     * 自定义失败判定处理器
     * @return
     */
    String processor() default "";
}
