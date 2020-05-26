package com.adongs.implement.captcha.model;

import java.lang.reflect.Field;

/**
 * 解析模型
 * @author yudong
 * @version 1.0
 */
public class ResolveModel {
    private Field field;
    private String name;

    public ResolveModel(Field field, String name) {
        this.field = field;
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }
}
