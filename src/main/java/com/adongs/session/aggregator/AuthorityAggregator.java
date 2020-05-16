package com.adongs.session.aggregator;

import com.google.common.collect.Sets;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 权限聚集器 负责收集url的所有权限信息
 * @author yudong
 * @version 1.0
 */
@Component
public class AuthorityAggregator implements ApplicationListener<AuthorityEvent> {

  private final static Set<String> IGNORE = Sets.newHashSet();
  private final static Set<String> BLURRY_IGNORE = Sets.newHashSet();
  private final static Map<String, UrlAuthority> URL_AUTHORITY = new HashMap<>(2<<6);
  private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 载入数据
     * @param authorityEvent 通知事件
     */
  @Override
  public void onApplicationEvent(AuthorityEvent authorityEvent) {
       Map<String, UrlAuthority> stringUrlAuthorityMap = authorityEvent.get();
       Iterator<Map.Entry<String, UrlAuthority>> iterator = stringUrlAuthorityMap.entrySet().iterator();
      while (iterator.hasNext()){
           Map.Entry<String, UrlAuthority> next = iterator.next();
           UrlAuthority value = next.getValue();
           String key = next.getKey();
          if (value.ignore()){
               boolean blurryIgnore = isBlurryIgnore(key);
               if (blurryIgnore){
                   BLURRY_IGNORE.add(key);
               }else{
                   IGNORE.add(key);
               }
          }else{
              URL_AUTHORITY.put(key,value);
          }
      }
  }

    /**
     * 匹配url是否被忽略
     * @param url url
     * @return true忽略 false没有忽略
     */
  public boolean ignore(String url){
     boolean contains = IGNORE.contains(url);
     if (contains){return true;}
     Iterator<String> iterator = BLURRY_IGNORE.iterator();
     while (iterator.hasNext()){
         boolean matchStart = PATH_MATCHER.matchStart(iterator.next(),url);
         if (matchStart){
             return true;
         }
     }
     return false;
 }

    /**
     * 获取url的权限信息
     * @param url url
     * @return 权限信息
     */
 public UrlAuthority  authorityInfo(String url){
     String matchUrl = matchUrl(url);
     if (matchUrl==null){
         return new UrlAuthority(false);
     }
    return URL_AUTHORITY.get(matchUrl);
 }


    /**
     * 匹配路径
     * @param url url
     * @return 匹配路径
     */
  private String matchUrl(String url){
      Iterator<String> iterator = URL_AUTHORITY.keySet().iterator();
      while (iterator.hasNext()){
          String next = iterator.next();
          boolean matchStart = PATH_MATCHER.matchStart(next,url);
          if (matchStart){
              return next;
          }
      }
      return null;
  }

    /**
     * 判断是否为模糊匹配
     * @param url url
     * @return 是否模糊匹配
     */
  private boolean isBlurryIgnore(String url){
      return url.indexOf("*")!=-1;
  }

}
