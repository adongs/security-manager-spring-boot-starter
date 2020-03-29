package com.security.manager.utils.el;

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
 * @date 2019/11/26 下午5:32
 * @modified by
 */
public class ElAnalysis {

   private StandardEvaluationContext context = new StandardEvaluationContext();

    public ElAnalysis(Map<String,Object> params) {
        context.setVariables(params);
    }

    /**
     * el表达式解析
     * @param str
     * @return
     */
    public String analysis(String str){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValue(context,String.class);
    }

    /**
     * 获取类型
     * @param str
     * @return
     */
    public Class<?> type(String str){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValueType(context);
    }

    /**
     * el表达式解析
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T analysis(String str,Class<T> clazz){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(str);
        return expression.getValue(context,clazz);
    }

    /**
     * 替换值
     * @param str
     * @param values
     */
    public void reinstall(String str,Object values){
        ExpressionParser expressionParser = new SpelExpressionParser();
        expressionParser.parseExpression(str).setValue(context,context.lookupVariable(str),values);
    }

}
