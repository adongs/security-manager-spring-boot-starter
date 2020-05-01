## 简介
一个以权限为主的包装框架,集成多种常用功能,注解风格,适合中小项目,开箱即用

## 文档
 
[文档](https://github.com/adongs/security-manager-spring-boot-starter/wiki)


## 注解介绍

|注解|功能描述|
|---|---|
@Certification|判断权限和角色
@Sightseer|游客
@Resources|对资源授权
@RateLimiters|限流器
@Resubmit|防止重复提交
@Sign|盐值校验
@Version|版本校验
@Logout|自定义日志输出
@LogDebug|Debug日志输出
@LogError|Error日志输出
@LogFatal|Fatal日志输出
@LogInfo|Info日志输出
@LogTrace|Trace日志输出
@LogWarn|Warn日志输出
@Decrypt|加密
@Decode|解密
@IgnoresField|忽略字段响应
@IgnoresFields|@IgnoresField集合
@ResponseExcel|表格导出
@RequestExcel|表格导入
@RedisLock|redis实现分布式互斥锁
@ZookeeperLock|Zookeeper实现分布式互斥锁
@EitherOr|二选一验证注解
@Group|对组进行验证注解
@LeastNotNullQuantity|至少满足多少参数不为空验证注解
@Mobile|手机号验证注解



## 版本更新说明

## 1.0

> 1.实现权限
> 2.日志输出
> 3.盐值校验
> 4.防止重复提交
> 5.限流实现
> 6.实现字段忽略

## 1.2

> 1.实现加/解密
> 2.实现游客注解

## 1.3

> 1.实现excel导入导出

## 1.4

> 1.实现分布式锁

## 1.5
> 1.实现对资源授权
> 2.添加部分验证注解