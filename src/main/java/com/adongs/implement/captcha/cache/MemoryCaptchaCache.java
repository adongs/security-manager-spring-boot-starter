package com.adongs.implement.captcha.cache;

import com.adongs.config.CaptchaGlobalConfig;
import com.adongs.implement.captcha.model.CaptchaModel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 用内存作为图片验证码的缓存
 * @author yudong
 * @version 1.0
 */
public class MemoryCaptchaCache implements CaptchaCache{

    private final Map<String, CaptchaModel> captchaModelMap = Maps.newHashMap();

    private final LoadingCache<String,Integer> cache;

    public MemoryCaptchaCache(CaptchaGlobalConfig config) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(config.getMarkerCacheTime(), TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    @Override
    public void save(String id, String verCode, long time) {
        captchaModelMap.put(id,new CaptchaModel(verCode,System.currentTimeMillis()+time));
    }

    @Override
    public boolean ver(String id, String verCode) {
        final CaptchaModel captchaModel = captchaModelMap.get(id);
        if(captchaModel==null){
            return false;
        }
        final long time = captchaModel.getTime();
        if (time>System.currentTimeMillis() && verCode.toLowerCase().equals(captchaModel.getVerCode().toLowerCase())){
            captchaModelMap.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void addMarkerCount(String id) {
        final Integer count = cache.getIfPresent(id);
        if (count!=null){
            cache.put(id,count+1);
        }
    }

    @Override
    public int markerCount(String id) {
        try {
            return cache.get(id);
        } catch (ExecutionException e) {
            return 0;
        }
    }

    @Override
    public void clearMark(String id) {
        cache.invalidate(id);
    }
}
