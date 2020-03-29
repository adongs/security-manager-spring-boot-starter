package com.adongs.utils.el;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.security.Key;
import java.util.Map;

/**
 * EL表达式解析
 * @author adong
 * @version 1.0
 */
public class ElAnalysis {

   private StandardEvaluationContext context = new StandardEvaluationContext();

    public ElAnalysis(Map<String,Object> params) {
        context.setVariables(params);
    }

    /**
     * el表达式解析
     * @param str el表达式
     * @return 获取到值
     */
    public String analysis(String str){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValue(context,String.class);
    }

    /**
     * 获取类型
     * @param str el表达式
     * @return 获取到的类型
     */
    public Class<?> type(String str){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValueType(context);
    }

    /**
     * el表达式解析
     * @param str el表达式
     * @param clazz 转换类型
     * @param <T> 指定类型
     * @return 指定类型
     */
    public <T>T analysis(String str,Class<T> clazz){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValue(context,clazz);
    }

    /**
     * 替换值
     * @param str el表达式
     * @param values 需要替换的值
     */
    public void reinstall(String str,Object values){
        ExpressionParser expressionParser = new SpelExpressionParser();
        expressionParser.parseExpression(str).setValue(context,context.lookupVariable(str),values);
    }

}
