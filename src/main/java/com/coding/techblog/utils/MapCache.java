package com.coding.techblog.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MapCache {

    private static final int DEFAULT_CACHES = 1024;

    private static final MapCache INS = new MapCache();

    public static MapCache single() {
        return INS;
    }


    private Map<String, CacheObject> cachePool;

    public MapCache() {
        this(DEFAULT_CACHES);
    }

    public MapCache(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }


    public <T> T get(String key) {
        CacheObject cacheObject = cachePool.get(key);
        if (null != cacheObject) {
            long cur = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > cur) {
                Object result = cacheObject.getValue();
                return (T) result;
            }
        }
        return null;
    }


    public <T> T hget(String key, String field) {
        key = key + ":" + field;
        return this.get(key);
    }


    public void set(String key, Object value) {
        this.set(key, value, -1);
    }


    public void set(String key, Object value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }


    public void hset(String key, String field, Object value) {
        this.hset(key, field, value, -1);
    }


    public void hset(String key, String field, Object value, long expired) {
        key = key + ":" + field;
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }


    public void del(String key) {
        cachePool.remove(key);
    }


    public void hdel(String key, String field) {
        key = key + ":" + field;
        this.del(key);
    }

    public void clean() {
        cachePool.clear();
    }

    static class CacheObject {
        private String key;
        private Object value;
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }
}