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
     * 请求验证码的id
     * @return 请求验证码的id
     */
    String requestId() default "";
    /**
     * 图片验证码请求路径
     * 为空则使用配置文件的路径
     * @return 图片验证码请求路径
     */
    String value() default "";

    /**
     * 验证码的位置,支持EL表达式,
     * 默认将获取header中的captcha如果不存在将获取Param中的captcha
     * 如果不填写将 默认尝试 (code captcha loginCode loginCaptcha userCode userCaptcha)
     * EL表达式: #object.code(#参数名称.属性)
     * 自动解析:例如(code 或者 captcha) ,只要给出字段名称即可,但是需要注意字段名称在参数或者参数对象中不能存在重复,
     * 否则会导致取值错误
     * @return 验证码位置
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
     * EL表达式: #object.code(#参数名称.属性)
     * 自动解析:例如(code 或者 captcha) ,只要给出字段名称即可,但是需要注意字段名称在参数或者参数对象中不能存在重复,
     * 否则会导致取值错误
     * @return 登录用户名,支持El表达式
     */
    String loginName() default "";

    /**
     * 验证码  宽X高
     * 不填写将使用配置文件
     * @return 验证码宽X高
     */
    String widthHeight() default "";

    /**
     * 验证码位数,不填写默认使用配置文件
     * @return 验证码位数
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
     * @return 字符类型
     */
    int characterType() default -1;

    /**
     * 验证码字体
     * @return 验证码字体
     */
    int font() default -1;
    /**
     * 验证码类型
     * @return 验证类型
     */
    CodeType codeType() default CodeType.CONFIG;

    /**
     * 判定失败的条件
     * @return 判断条件
     */
    FailureCondition[] condition() default {};

    /**
     * 条件判定表达式
     * @return 表达式
     */
    String expression() default "";

    /**
     * 异常判定为失败
     * @return 处理器
     */
    Class<?extends Throwable> [] abnormalJudgment() default {};
}
