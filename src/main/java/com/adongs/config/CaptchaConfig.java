package com.adongs.config;

import com.adongs.constant.CodeType;
import com.adongs.constant.FailureCondition;
import com.wf.captcha.base.Captcha;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 图片验证码
 * @author yudong
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.captcha")
public class CaptchaConfig {

    /**
     * 图片验证码路径
     */
    private String url = "/security/captcha";

    /**
     * 二维码宽高
     */
    private String widthHeight = "130X48";

    /**
     * 验证码尝试查找名称
     */
    private String [] codeTry = new String[] {"code","captcha","loginCode","loginCaptcha","userCode","userCaptcha"};
    /**
     * 登录用户名尝试查找名称
     */
    private String [] loginNameTry = new String[] {"name","user","email","mobile","phone","login","loginName","userName","userEmail","loginEmail","userMobile","loginMobile","userPhone","loginPhone","id","uid","userid","loginId"};
    /**
     * 验证码位数
     */
    private int digits = 4;
    /**
     * 验证码字符类型
     * -1 使用配置文件
     * 1 数字和字母混合
     * 2 纯数字
     * 3 纯字母
     * 4 纯大写字母
     * 5 纯小写字母
     * 6 数字和大写字母
     */
    private int characterType = 1;

    /**
     * 验证码类型
     */
    private CodeType codeType =  CodeType.GIF;

    /**
     * 字体
     */
    private int font = Captcha.FONT_1;
    /**
     * 判定条件
     */
    private FailureCondition [] condition = new  FailureCondition [] {FailureCondition.HTTP_STATUS_NOT_200,FailureCondition.THROW_EXCEPTION};

    /**
     * 默认处理器
     */
    private String processor = "default";


    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidthHeight() {
        return widthHeight;
    }

    public void setWidthHeight(String widthHeight) {
        this.widthHeight = widthHeight;
    }

    public String[] getCodeTry() {
        return codeTry;
    }

    public void setCodeTry(String[] codeTry) {
        this.codeTry = codeTry;
    }

    public String[] getLoginNameTry() {
        return loginNameTry;
    }

    public void setLoginNameTry(String[] loginNameTry) {
        this.loginNameTry = loginNameTry;
    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public int getCharacterType() {
        return characterType;
    }

    public void setCharacterType(int characterType) {
        this.characterType = characterType;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public FailureCondition[] getCondition() {
        return condition;
    }

    public void setCondition(FailureCondition[] condition) {
        this.condition = condition;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }
}
