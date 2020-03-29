package com.adongs.annotation.extend.log;



import com.adongs.implement.logout.DefaultLogOutFormat;
import com.adongs.implement.logout.LogFormat;

import java.lang.annotation.*;

/**
 * 一个辅助日志输出工具
 * @author adong
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogTrace {
    /**
     * 格式化工厂
     * @return 格式化工厂
     */
    Class<? extends LogFormat> format() default DefaultLogOutFormat.class;

    /**
     * 标签,输出在内容最前面,如果标签为空,默认输出方法名称
     * @return 标签
     */
    String label() default "";

    /**
     * 自定义内容输出支持el
     * 例如:
     * \@Logout(content = "'test='+#test+' userName='+#user.getUserName()")
     * public String test1(String test,String test1,User user){
     *
     * }
     * 内置两个默认值 return(返回值)  exception(异常)
     * 获取返回值
     * \@Logout(content = "'return='+#return")
     * public String test1(String test,String test1,User user){
     *
     * }
     * 如果返回值是一个对象
     * \@Logout(content = "'return='+#return.getUserName()")
     * public User test1(String test,String test1,User user){
     *
     * }
     * 获取异常同理
     * @return 自定义内容输出支持el
     */
    String content() default "";

}
