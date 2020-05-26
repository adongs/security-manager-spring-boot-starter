package com.adongs.constant;

import com.adongs.implement.captcha.marker.Marker;
import com.adongs.session.terminal.Terminal;
import com.adongs.utils.el.ElAnalysis;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
 * 接口访问失败条件
 * @author yudong
 * @version 1.0
 */
public enum FailureCondition {
    /**
     * 方法抛出异常
     */
    THROW_EXCEPTION{
        @Override
        public void mark(Marker marker, String id, Throwable param, Class<? extends Throwable>... throwables) {
            for (Class<? extends Throwable> throwable : throwables) {
                if (throwable.isAssignableFrom(param.getClass())){
                    marker.marker(id, Terminal.getRequest());
                }
            }
        }

        @Override
        public void mark(Marker marker, String id, Map<String,Object> param, String el) {
        }
    },
    /**
     * 表达式判定
     */
    EXPRESSION_DECISION{
        @Override
        public void mark(Marker marker, String id, Throwable param, Class<? extends Throwable>... throwables) {

        }

        @Override
        public void mark(Marker marker, String id, Map<String,Object> param, String el) {
            ElAnalysis elAnalysis = new ElAnalysis(param);
            final Boolean analysis = elAnalysis.analysis(el, Boolean.class);
            if (analysis){
                marker.marker(id, Terminal.getRequest());
            }
        }
    };

    public abstract void mark(Marker marker,String id,Throwable param,Class<?extends Throwable> ... throwables);
    public abstract void mark(Marker marker,String id,Map<String,Object> param,String el);


}
