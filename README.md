# security-manager-spring-boot-starter
一个安全框架工具,集成多种功能,适合中小项目,开箱即用

## maven
```xml
<dependency>
  <groupId>com.adongs</groupId>
  <artifactId>security-manager-spring-boot-starter</artifactId>
  <version>1.1</version>
</dependency>
```

## 注解介绍

|注解|功能描述|
|---|---|
@Certification|判断权限和角色
@Sightseer|游客
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

## @Sightseer
> 表示controller不验证用户


## @Certification
> 权限注解,标注在方法上,执行方法前会进行权限认证

### 参数描述
|参数名称|类型|说明|默认值
|---|---|---|---|
permissions|String[]|权限组|空
plogical|Certification.Logical|关系 <br/> Certification.Logical.AND 并且 <br/> Certification.Logical.OR 或|Certification.Logical.AND
roles|String[]|角色组|空
rlogical|Certification.Logical|关系 <br/> Certification.Logical.AND 并且 <br/> Certification.Logical.OR 或|Certification.Logical.AND


## @RateLimiters
> 限流,可以限制请求的访问数量,即每秒请求数量

### 参数描述
|参数名称|类型|说明|默认值
|---|---|---|---|
name|String|限流器名称|global
permits|double|获取令牌等待时间|0

## @Resubmit
> 防止重复提交,要求开启reques重复读取,请勿当幂等使用

|参数名称|类型|说明|默认值
|---|---|---|---|
time|long|重复提交时间判定时间(单位毫秒)|3000
refreshTime|boolean|多次请求是否刷新时间|true
uid|string|唯一标识,支持el表达式|默认值为空,将使用默认方式生成唯一id
userId|string|是否开启用户隔离,支持el表达式,设置为false标识关闭|默认为空将自动判断当前用户
processor|ResubmitProcessor|处理器,方便扩展|默认为DefaultResubmitProcessor

## @Sign
>盐值对比计算,要求开启reques重复读取

|参数名称|类型|说明|默认值
|---|---|---|---|
sign|String|盐值获取,支持El表达式|默认值为空
value|SignProcessor|盐值处理器,方便扩展|DefaultSignProcessor

## @Version
> 请求版本校验

|参数名称|类型|说明|默认值
|---|---|---|---|
value|String[]|允许版本|默认值为空

## @Logout(@LogWarn @LogTrace @LogInfo @LogFatal @LogError @LogDebug)
> 方法快速输出参数 异常 返回值

|参数名称|类型|说明|默认值
|---|---|---|---|
format|LogFormat|格式化处理器|DefaultLogOutFormat
label|String|标签,输出在内容最前面|默认为空
content|String|自定义内容输出,支持El表达式|默认为空

## @Decrypt
>加密数据,支持java包装类型和自定义内容,暂时不支持list和map

|参数名称|类型|说明|默认值
|---|---|---|---|
values|String[]|加密的名称 只支持返回值加密 只支持El表达式,返回值用#return开头|默认为空
processor|DecryptProcessor|加密处理器|DefaultDESDecryptProcessor

## @Decode
>解密数据,支持java包装类型和自定义内容,暂时不支持list和map

|参数名称|类型|说明|默认值
|---|---|---|---|
values|String[]|解密的名称 只支持方法参数解密 只支持El表达式|默认为空
processor|DecryptProcessor|解密处理器|DefaultDESDecryptProcessor

## @IgnoresField(@IgnoresFields)
> 字段忽略,只支持类被@Controller注解标注的方法上

|参数名称|类型|说明|默认值
|---|---|---|---|
type|Class|过滤类型|
include|String[]|保留字段|默认为空
filter|String[]|过滤字段|默认为空

@ResponseExcel
> 导出excel

|参数名称|类型|说明|默认值
|---|---|---|---|
data|String|数据提取位置,支持el表达式|空
name|String|导出文件名称,支持el表达式|导出数据
nameDatetime|String|在名称上添加时间戳|yyyy-MM-dd
nameDatetimePosition|int|时间戳的位置 -1(名称前面)  0(关闭时间戳)  1(名称后面)|1
sheetName|String|导出sheet名称,支持el表达式|sheet0
compression|boolean|是否压缩|false
compressionFormat|CompressionFormat|压缩格式,现在只支持zip,后期扩展|CompressionFormat.ZIP





## 配置介绍

|配置名称|类型|说明|默认值
|---|---|---|---|
spring.security.manager.request.enabled|boolean|开启权限注解|true
spring.security.manager.request.token|String|请求token的key,在header中获取|token
spring.security.manager.request.tourists|boolean|是否开启游客模式(当token为null时允许访问)|false
spring.security.manager.request.version|String|请求版本号的key,在header中获取|version
spring.security.manager.request.timestamp|String|请求时间的key,在header中获取|timestamp
spring.security.manager.request.sign|String|盐值的key,在header中获取|sign
spring.security.manager.request.readers|boolean|是否开启 request重复读取|true
spring.security.manager.request.permitsPerSecond|double|限流数据,每秒访问量
spring.security.manager.request.resubmit.enabled|boolean|是否开启重复提交|true
spring.security.manager.request.resubmit.initCount|int|初始化容器数量|2048
spring.security.manager.request.resubmit.duration|long|过期时间单位毫秒|10000
spring.security.manager.des.key|String|des秘钥,必须大于等于16位
spring.security.manager.des.offset|String|偏移量大于等于8位
spring.security.manager.log.lineFeed|boolean|是否单行输出日志|true


## 文档

### 用户token和权限

> 简单的token校验以及权限控制

- 1.实现 DataSource 接口,自定义权限和用户token验证,DefaultDataSource为默认实现的用例,可以参考
建议写一个service

- 2.设置spring.security.manager.request.enabled为true,默认为true

- 3.编写一个controller,下面的接口如果没有合格token是无法访问
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    public User user(){
        return new User();
    }
}
```
- 4.想对接口设置访问权限或者访问角色,可以使用@Certification注解,如下要求用户必须同时拥有admin和user角色,同时权限必须有select和update
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    @Certification(roles = {"admin","user"},permissions = {"select","update"})
    public User user(){
        return new User();
    }
}
```
- 5.如果只想用户角色满足一个即可和权限满足一个就行,可以设置如下Certification.Logical.AND表示并且  Certification.Logical.OR表示或者
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    @Certification(roles = {"admin","user"},rlogical = Certification.Logical.OR,permissions = {"select","update"},plogical = Certification.Logical.OR)
    public User user(){
        return new User();
    }
}
```

- 6.想要获取当前登录者的信息
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    @Certification(roles = {"admin","user"},rlogical = Certification.Logical.OR,permissions = {"select","update"},plogical = Certification.Logical.OR)
    public User user(){
           //获取登录者的终端
            Terminal terminal = Terminal.get();
            //获取登录信息
            User user = terminal.getUser();
        return user;
    }
}
```
- 7.如果想要获取请求体
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    @Certification(roles = {"admin","user"},rlogical = Certification.Logical.OR,permissions = {"select","update"},plogical = Certification.Logical.OR)
    public User user(){
            HttpServletRequest request = Terminal.getRequest();
       return new User();
    }
}
```

- 如果不想这个接口校验token,需要标记为游客允许访问,在user方法上添加@Sightseer注解即可,
@Sightseer也可以添加在类上面,添加在类上面表示这个Controller下面所有的接口都不校验token,
优先级:方法@Certification > 方法@Sightseer > 类@Certification > 类@Sightseer
```java
@Controller
@RequestMapping("test")
public class TestController {

    @PostMapping("user")
    @Sightseer
    public User user(){
        return new User();
    }
}
```

```java
@Controller
@RequestMapping("test")
@Sightseer
public class TestController {

    //会校验token,和权限
    @PostMapping("user")
    @Certification(roles = {"admin","user"},rlogical = Certification.Logical.OR,permissions = {"select","update"},plogical = Certification.Logical.OR)
    public User user(){
        return new User();
    }
   //不会校验token
    @PostMapping("user1")
    public User user(){
        return new User();
    }

}
```


### 限流





### 下一版

@Lock
> 本地锁 等待编写

@RedisLock
> redis共享锁,等待编写

@ZookeeperLock
> Zookeeper共享锁,等待编写
