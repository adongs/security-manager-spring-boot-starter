package com.adongs.implement.sightseer;

import com.adongs.auto.SecurityManagerConfig;
import com.google.common.collect.Sets;
import org.springframework.context.ApplicationListener;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author yudong
 * @version 1.0
 */
public class SightseerProcessor  implements ApplicationListener<SightseerEven> {


    private String commonPrefix = "";

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Set<String> antUrls = Sets.newHashSet();

    private Set<String> urls = Sets.newHashSet();

    public SightseerProcessor(SecurityManagerConfig config,String commonPrefix) {
        Set<String> ignoreUrl = config.getRequest().getIgnoreUrl();
        if (ignoreUrl!=null && !ignoreUrl.isEmpty()){
            antUrls.addAll(ignoreUrl);
        }
        if(!StringUtils.isEmpty(commonPrefix)) {
            int suffix = commonPrefix.lastIndexOf("/");
            if (suffix == -1) {
                commonPrefix = commonPrefix + "/";
            }
            int prefix = commonPrefix.indexOf("/");
            if (prefix == -1) {
                commonPrefix = "/" + commonPrefix;
            }
            this.commonPrefix = commonPrefix;
        }
    }


    /**
     * 判断是否为忽略路径
     * @param url 请求url
     * @return ture 忽略 false不忽略
     */
    public boolean isSightseer(String url){
        boolean contains = urls.contains(url);
        if (contains){
            return true;
        }
        for (String s : urls) {
            boolean match = antPathMatcher.match(s, url);
            if (match){
                return true;
            }
        }
       return false;
    }


    @Override
    public void onApplicationEvent(SightseerEven sightseerEven) {
        Set<String> source = (Set)sightseerEven.getSource();
        source.forEach(s->{
            urls.add(commonPrefix+s);
        });
    }



}
