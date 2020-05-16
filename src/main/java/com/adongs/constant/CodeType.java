package com.adongs.constant;

import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

/**
 * 验证码类型
 * @author yudong
 * @version 1.0
 */
public enum  CodeType {
    /**
     * png图片
     */
    PNG{
        @Override
        public  Captcha captcha() {
            return new SpecCaptcha();
        }
    },
    /**
     * gif图片
     */
    GIF{
        @Override
        public Captcha captcha() {
            return new GifCaptcha();
        }
    },
    /**
     * 中文
     */
    CHINESE{
        @Override
        public Captcha captcha() {
            return new ChineseCaptcha();
        }
    },
    /**
     * 中文gif
     */
    CHINESE_GIT{
        @Override
        public Captcha captcha() {
            return new ChineseGifCaptcha();
        }
    },
    /**
     * 算数
     */
    CALCULATION{
        @Override
        public Captcha captcha() {
            return new ArithmeticCaptcha();
        }
    },
    /**
     * 采用配置文件值
     */
    CONFIG{
        @Override
        public Captcha captcha() {
            return new SpecCaptcha();
        }
    };

   public abstract Captcha captcha();

}
