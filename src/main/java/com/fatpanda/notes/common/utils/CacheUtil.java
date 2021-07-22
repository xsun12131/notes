package com.fatpanda.notes.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author xyy
 * @date 2021/7/22
 */
public class CacheUtil {

    Cache<Object, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .expireAfterAccess(1, TimeUnit.SECONDS)
            .maximumSize(10)//最大条数
            .build();//定义cache

}
