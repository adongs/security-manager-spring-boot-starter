package com.adongs.implement.captcha;

import com.wf.captcha.base.Captcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * 二维码生成
 * @author yudong
 * @version 1.0
 */
public class CaptchaHandler implements CustomizeHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeHandler.class);

    private final CaptchaConfig captchaConfig;

    private final CaptchaCache captchaCache;

    public CaptchaHandler(CaptchaConfig captchaConfig, CaptchaCache captchaCache) {
        this.captchaConfig = captchaConfig;
        this.captchaCache = captchaCache;
    }

    @Override
    public Object links(HttpServletRequest request, HttpServletResponse response) {
        Captcha captcha = captcha();
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(),ioException);
        }
        return null;
    }

    private Captcha captcha(){
        final Captcha captcha = captchaConfig.getCodeType().captcha();
        captcha.setHeight(captchaConfig.getHeight());
        captcha.setWidth(captchaConfig.getWidth());
        captcha.setLen(captchaConfig.getDigits());
        captcha.setCharType(captchaConfig.getCharacterType());
        try {
            captcha.setFont(captchaConfig.getFont());
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(),ioException);
        } catch (FontFormatException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return captcha;
    }


}
